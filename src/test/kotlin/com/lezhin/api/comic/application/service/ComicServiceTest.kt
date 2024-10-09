package com.lezhin.api.comic.application.service

import com.lezhin.api.comic.domain.entity.Comic
import com.lezhin.api.comic.domain.entity.Member
import com.lezhin.api.comic.domain.entity.ViewHistory
import com.lezhin.api.comic.domain.repository.BuyRepository
import com.lezhin.api.comic.domain.repository.ComicRepository
import com.lezhin.api.comic.domain.repository.MemberRepository
import com.lezhin.api.comic.domain.repository.ViewHistoryRepository
import com.lezhin.api.comic.ui.dto.response.ComicResponse
import com.lezhin.api.comic.ui.dto.response.PurchaseResponse
import com.lezhin.api.comic.ui.dto.response.ViewHistoryResponse
import com.lezhin.api.exception.ComicException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime

@SpringBootTest
class ComicServiceTest {

    private lateinit var comicService: ComicService

    private val comicRepository: ComicRepository = Mockito.mock(ComicRepository::class.java)
    private val viewHistoryRepository: ViewHistoryRepository = Mockito.mock(ViewHistoryRepository::class.java)
    private val buyRepository: BuyRepository = Mockito.mock(BuyRepository::class.java)
    private val memberRepository: MemberRepository = Mockito.mock(MemberRepository::class.java)

    @BeforeEach
    fun setUp() {
        comicService = ComicService(comicRepository, viewHistoryRepository, buyRepository, memberRepository)
    }

    @DisplayName("작품 조회 이력 API 테스트")
    @Test
    fun getViewHistoryByComicIdTest() {
        val comicId = 1L
        val page = 1
        val pageSize = 10

        val viewHistoryPage: Page<ViewHistory> = PageImpl(
            listOf(
                ViewHistory(
                    id = 1L,
                    comic = Comic(id = comicId, title = "Test Comic", isFree = false, price = 1000),
                    member = Member(id = 1L, username = "Test Member"),
                    viewedAt = LocalDateTime.now()
                )
            ),
            PageRequest.of(page - 1, pageSize),
            1
        )

        whenever(viewHistoryRepository.findByComicId(eq(comicId), any()))
            .thenReturn(viewHistoryPage)

        val response: ViewHistoryResponse = comicService.getViewHistoryByComicId(comicId, page, pageSize)

        assertEquals(page, response.meta.currentPage)
        assertEquals(1, response.meta.totalPageCount)
        assertEquals(pageSize, response.meta.pageSize)
        assertEquals(1, response.meta.lastPage)
        assertEquals(comicId, response.data[0].comic.id)
    }

    @DisplayName("인기 작품 조회 API 테스트")
    @Test
    fun getPopularComicsTest() {
        val comicList = listOf(
            Comic(id = 1L, title = "문학소년", isFree = true, price = 0)
        )

        whenever(comicRepository.findTop10ByOrderByViewCountDesc())
            .thenReturn(comicList)

        val response: List<ComicResponse> = comicService.getPopularComics()

        assertEquals(1, response.size)
        assertEquals("문학소년", response[0].title)
        assertTrue(response[0].isFree)
    }

    @DisplayName("작품 구매 API 테스트")
    @Test
    fun buyComicTest() {
        val comicId = 1L
        val userId = 123L
        val comic = Comic(id = comicId, title = "Test Comic", isFree = false, price = 1000)

        whenever(comicRepository.findById(comicId)).thenReturn(java.util.Optional.of(comic))
        whenever(buyRepository.existsByComicIdAndMemberId(comicId, userId)).thenReturn(false)
        whenever(memberRepository.findById(userId)).thenReturn(java.util.Optional.of(Member(id = userId, username = "Test Member")))
        whenever(buyRepository.save(any())).thenReturn(null)

        val response: PurchaseResponse = comicService.buyComic(comicId, userId)

        assertEquals("SUCCESS", response.status)
        assertEquals(comicId, response.comicId)
        assertEquals(userId, response.userId)
    }

    @DisplayName("작품 및 이력 삭제 API 테스트")
    @Test
    fun deleteComicTest() {
        val comicId = 1L

        Mockito.doNothing().`when`(comicRepository).deleteById(comicId)
        Mockito.doNothing().`when`(viewHistoryRepository).deleteByComicId(comicId)

        comicService.deleteComic(comicId)

        Mockito.verify(comicRepository).deleteById(comicId)
        Mockito.verify(viewHistoryRepository).deleteByComicId(comicId)
    }

    @DisplayName("이미 구매한 작품 처리 테스트")
    @Test
    fun buyComicAlreadyPurchasedTest() {
        val comicId = 1L
        val userId = 123L

        whenever(comicRepository.findById(comicId)).thenReturn(java.util.Optional.of(Comic(id = comicId, title = "Test Comic", isFree = false, price = 1000)))
        whenever(buyRepository.existsByComicIdAndMemberId(comicId, userId)).thenReturn(true)

        val exception = assertThrows(ComicException::class.java) {
            comicService.buyComic(comicId, userId)
        }

        assertEquals("이미 구매한 작품입니다. comicId: $comicId", exception.message)
    }
}
