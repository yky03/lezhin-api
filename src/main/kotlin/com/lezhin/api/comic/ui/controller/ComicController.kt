package com.lezhin.api.comic.ui.controller

import com.lezhin.api.comic.application.service.ComicService
import com.lezhin.api.comic.ui.dto.response.ComicResponse
import com.lezhin.api.comic.ui.dto.response.PurchaseResponse
import com.lezhin.api.comic.ui.dto.response.ViewHistoryResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "레진 코믹스 API", description = "레진 코믹스 작품 API")
@RestController
@RequestMapping("/api/v1/comics")
class ComicController(
    private val comicService: ComicService
) {

    @Operation(summary = "작품 조회 이력", description = "특정 작품의 조회 이력을 가져옵니다.")
    @GetMapping("/{comicId}/history")
    fun getComicViewHistory(
        @Parameter(description = "작품 ID") @PathVariable comicId: Long,
        @Parameter(description = "현재 페이지 번호", required = false, example = "1") @RequestParam(defaultValue = "1") page: Int,
        @Parameter(description = "페이지 크기", required = false, example = "10") @RequestParam(defaultValue = "10") pageSize: Int
    ): ResponseEntity<ViewHistoryResponse> {
        val histories = comicService.getViewHistoryByComicId(comicId, page, pageSize)
        return ResponseEntity.ok(histories)
    }

    @Operation(summary = "인기 작품 조회", description = "조회수가 많은 작품 상위 10개를 가져옵니다.")
    @GetMapping("/popular")
    fun getPopularComics(): ResponseEntity<List<ComicResponse>> {
        val popularComics = comicService.getPopularComics()
        return ResponseEntity.ok(popularComics)
    }

    @Operation(summary = "작품 구매", description = "작품을 구매합니다.")
    @PostMapping("/{comicId}/purchase")
    fun purchaseComic(
        @Parameter(description = "작품 ID") @PathVariable comicId: Long,
        @Parameter(description = "사용자 ID") @RequestParam userId: Long
    ): ResponseEntity<PurchaseResponse> {
        val purchaseResponse = comicService.buyComic(comicId, userId)
        return ResponseEntity.ok(purchaseResponse)
    }

    @Operation(summary = "구매 인기 작품 조회", description = "구매수가 많은 작품 상위 10개를 가져옵니다.")
    @GetMapping("/popular/purchases")
    fun getMostPurchasedComics(): ResponseEntity<List<ComicResponse>> {
        val mostPurchasedComics = comicService.getMostPurchasedComics()
        return ResponseEntity.ok(mostPurchasedComics)
    }

    @Operation(summary = "작품 및 이력 삭제", description = "작품과 해당 조회 이력을 삭제합니다.")
    @DeleteMapping("/{comicId}")
    fun deleteComic(
        @Parameter(description = "작품 ID") @PathVariable comicId: Long
    ): ResponseEntity<String> {
        comicService.deleteComic(comicId)
        return ResponseEntity.ok("Comic $comicId 작품 및 이력 삭제 완료")
    }
}