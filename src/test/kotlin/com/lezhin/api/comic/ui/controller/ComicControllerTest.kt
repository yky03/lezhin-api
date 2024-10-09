package com.lezhin.api.comic.ui.controller

import com.lezhin.api.comic.application.service.ComicService
import com.lezhin.api.comic.domain.entity.Comic
import com.lezhin.api.comic.domain.entity.Member
import com.lezhin.api.comic.domain.entity.ViewHistory
import com.lezhin.api.comic.ui.dto.response.ComicResponse
import com.lezhin.api.comic.ui.dto.response.PurchaseResponse
import com.lezhin.api.comic.ui.dto.response.ViewHistoryResponse
import com.lezhin.api.common.response.Meta
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
class ComicControllerTest {

    @Autowired
    private lateinit var wac: WebApplicationContext

    @MockBean
    private lateinit var comicService: ComicService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val AUTHORIZATION_KEY = "7725010aed35e854d819a3de7cac78aae6f054d14d9f95b3b9f5075ec956c77a"

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
    }

    @DisplayName("잘못된 인증키로 요청 시 실패 테스트")
    @Test
    fun authorizationKeyFailureTest() {
        val invalidAuthorizationKey = "invalid_key_xxxxxxxxxxxx"

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/comics/popular")
                .header("Authorization", invalidAuthorizationKey)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect { status().isUnauthorized }
            .andExpect { jsonPath("$.message").value("인증키가 유효하지 않습니다.") }
    }

    @DisplayName("작품 조회 이력 테스트")
    @Test
    fun getComicViewHistoryTest() {
        val comicId = 1L
        val page = 1
        val pageSize = 10

        // 실제 ViewHistory 객체를 생성
        val viewHistoryList = mutableListOf(
            ViewHistory(
                id = 1L,
                comic = Comic(id = comicId, title = "Test Comic", isFree = false, price = 1000),
                member = Member(id = 1L,  username = "Test Member"),
                viewedAt = LocalDateTime.now()
            )
        )

        val viewHistoryResponse = ViewHistoryResponse(
            meta = Meta(currentPage = page, totalPageCount = 1, pageSize = pageSize, lastPage = 1),
            data = viewHistoryList
        )

        // 서비스가 ViewHistoryResponse를 반환하도록 설정
        Mockito.`when`(comicService.getViewHistoryByComicId(comicId, page, pageSize))
            .thenReturn(viewHistoryResponse)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/comics/$comicId/history")
                .header("Authorization", AUTHORIZATION_KEY)
                .param("page", page.toString())
                .param("pageSize", pageSize.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect { status().isOk }
            .andExpect { jsonPath("$.meta.currentPage").value(page) }
            .andExpect { jsonPath("$.meta.totalPageCount").value(1) }
            .andExpect { jsonPath("$.data[0].comic.id").value(comicId) }
            .andExpect { jsonPath("$.data[0].member.id").value(1L) }
    }


    @DisplayName("인기 작품 조회 테스트")
    @Test
    fun getPopularComicsTest() {
        val popularComics = listOf(
            ComicResponse(id = 1, title = "문학소년", isFree = true)
        )

        Mockito.`when`(comicService.getPopularComics())
            .thenReturn(popularComics)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/comics/popular")
                .header("Authorization", AUTHORIZATION_KEY)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect { status().isOk }
            .andExpect { jsonPath("$[0].title").value("문학소년") }
    }

    @DisplayName("작품 구매 테스트")
    @Test
    fun purchaseComicTest() {
        val comicId = 1L
        val userId = 123L

        val purchaseResponse = PurchaseResponse(
            comicId = comicId,
            userId = userId,
            status = "SUCCESS"
        )

        Mockito.`when`(comicService.buyComic(comicId, userId))
            .thenReturn(purchaseResponse)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/comics/$comicId/purchase")
                .header("Authorization", AUTHORIZATION_KEY)
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect { status().isOk }
            .andExpect { jsonPath("$.comicId").value(comicId) }
            .andExpect { jsonPath("$.userId").value(userId) }
    }

    @DisplayName("구매 인기 작품 조회 테스트")
    @Test
    fun getMostPurchasedComicsTest() {
        val mostPurchasedComics = listOf(
            ComicResponse(id = 1, title = "문학소년", isFree = true)
        )

        Mockito.`when`(comicService.getMostPurchasedComics())
            .thenReturn(mostPurchasedComics)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/comics/popular/purchases")
                .header("Authorization", AUTHORIZATION_KEY)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect { status().isOk }
            .andExpect { jsonPath("$[0].title").value("구매 인기 작품") }
    }

    @DisplayName("작품 및 이력 삭제 테스트")
    @Test
    fun deleteComicTest() {
        val comicId = 1L

        Mockito.doNothing().`when`(comicService).deleteComic(comicId)

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/v1/comics/$comicId")
                .header("Authorization", AUTHORIZATION_KEY)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect { status().isOk }
            .andExpect { content().string("Comic $comicId 작품 및 이력 삭제 완료") }
    }
}