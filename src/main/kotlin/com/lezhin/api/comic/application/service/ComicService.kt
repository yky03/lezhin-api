package com.lezhin.api.comic.application.service

import com.lezhin.api.comic.domain.factory.BuyFactory
import com.lezhin.api.comic.domain.entity.Comic
import com.lezhin.api.comic.domain.entity.PaymentStatus
import com.lezhin.api.comic.domain.repository.BuyRepository
import com.lezhin.api.comic.domain.repository.ComicRepository
import com.lezhin.api.comic.domain.repository.MemberRepository
import com.lezhin.api.comic.domain.repository.ViewHistoryRepository
import com.lezhin.api.comic.ui.dto.response.ComicResponse
import com.lezhin.api.comic.ui.dto.response.PurchaseResponse
import com.lezhin.api.comic.ui.dto.response.ViewHistoryResponse
import com.lezhin.api.common.response.Meta
import com.lezhin.api.exception.ComicException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.math.ceil

@Service
class ComicService(
    private val comicRepository: ComicRepository,
    private val viewHistoryRepository: ViewHistoryRepository,
    private val buyRepository: BuyRepository,
    private val memberRepository: MemberRepository,
) {

    // 1. 작품 조회 이력 API
    fun getViewHistoryByComicId(comicId: Long, page: Int, pageSize: Int): ViewHistoryResponse {
        val pageable: Pageable = PageRequest.of(page - 1, pageSize)
        val viewHistoriesPage = viewHistoryRepository.findByComicId(comicId, pageable)

        // 전체 조회 이력 수 및 페이징된 조회 이력 리스트 반환
        val totalCount = viewHistoriesPage.totalElements
        val totalPages = viewHistoriesPage.totalPages
        val lastPage = ceil(totalCount.toDouble() / pageSize).toInt() // lastPage 계산

        return ViewHistoryResponse(
            meta = Meta(
                currentPage = page,
                totalPageCount = totalPages,
                pageSize = pageSize,
                lastPage = lastPage
            ),
            data = viewHistoriesPage.content
        )
    }

    // 2. 인기 작품 조회 API (조회수가 많은 작품 상위 10개)
    fun getPopularComics(): List<ComicResponse> {
        val popularComics = comicRepository.findTop10ByOrderByViewCountDesc()
        return popularComics.map { ComicResponse(it.id, it.title, it.isFree) }
    }

    // 3. 작품 구매 API
    fun buyComic(comicId: Long, userId: Long): PurchaseResponse {
        // 1. 작품 조회 (존재하지 않을 경우 예외 처리)
        val comic = comicRepository.findById(comicId).orElse(null)
            ?: throw ComicException("ERROR_0001", "없는 작품 번호 입니다. comicId: $comicId")

        // 2. 중복 구매 체크 (이미 구매한 작품인지 확인)
        if (buyRepository.existsByComicIdAndMemberId(comicId, userId)) {
            throw ComicException("ERROR_0002", "이미 구매한 작품입니다. comicId: $comicId")
        }

        // 3. 이벤트 기간에 따른 무료/유료 여부 확인
        val isFree = isFreeDuringEvent(comic)

        if (!isFree) {
            // 4. 결제 처리 로직 (유료 작품일 경우)
            val paymentResult = processPayment(comic.price, userId)

            if (!paymentResult) {
                throw ComicException("ERROR_0003", "결제 실패")
            }
        }

        // 5. 구매 내역 저장
        savePurchaseRecord(comic, userId)

        return PurchaseResponse(comicId, userId, "SUCCESS")
    }

    private fun isFreeDuringEvent(comic: Comic): Boolean {
        val today = LocalDateTime.now()

        val eventStart = comic.eventStartDate
        val eventEnd = comic.eventEndDate

        return if (eventStart != null && eventEnd != null) {
            today.isAfter(eventStart) && today.isBefore(eventEnd.plusDays(1))
        } else {
            false
        }
    }

    private fun processPayment(price: Int, userId: Long): Boolean {
        // pg 연동 및 결제 성공 간주
        return true
    }

    private fun savePurchaseRecord(comic: Comic, userId: Long) {
        val member = memberRepository.findById(userId).get()
        val buyRecord = BuyFactory.createBuy(member, comic, PaymentStatus.SUCCESS, comic.price)
        buyRepository.save(buyRecord)
    }

    // 4. 구매 인기 작품 조회 API (구매수가 많은 작품 상위 10개)
    fun getMostPurchasedComics(): List<ComicResponse> {
        val mostPurchasedComics = comicRepository.findTop10ByOrderByPurchaseCountDesc()
        return mostPurchasedComics.map { ComicResponse(it.id, it.title, it.isFree) }
    }

    // 5. 작품 및 이력 삭제 API
    fun deleteComic(comicId: Long) {
        // 작품 삭제
        comicRepository.deleteById(comicId)
        // 조회 이력 삭제
        viewHistoryRepository.deleteByComicId(comicId)
    }
}
