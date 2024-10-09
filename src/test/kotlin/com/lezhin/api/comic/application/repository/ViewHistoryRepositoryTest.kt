package com.lezhin.api.comic.application.repository

import com.lezhin.api.comic.domain.entity.Comic
import com.lezhin.api.comic.domain.entity.Member
import com.lezhin.api.comic.domain.entity.ViewHistory
import com.lezhin.api.comic.domain.repository.ViewHistoryRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

@DataJpaTest
class ViewHistoryRepositoryTest @Autowired constructor(
    val viewHistoryRepository: ViewHistoryRepository,
    val entityManager: TestEntityManager
) {

    private lateinit var comic: Comic
    private lateinit var member: Member

    @BeforeEach
    fun setUp() {
        comic = Comic(title = "Test Comic", isFree = false, viewCount = 100, price = 1000, purchaseCount = 50)
        member = Member(username = "Test Member", isAdult = true)

        entityManager.persist(comic)
        entityManager.persist(member)
        entityManager.flush()

        // 조회 이력 생성 및 저장
        val viewHistory1 = ViewHistory(comic = comic, member = member)
        val viewHistory2 = ViewHistory(comic = comic, member = member)
        entityManager.persist(viewHistory1)
        entityManager.persist(viewHistory2)
        entityManager.flush()
    }

    @DisplayName("특정 작품에 대한 조회 이력 페이징 조회")
    @Test
    fun testFindByComicId() {
        // given
        val pageable = PageRequest.of(0, 10)

        // when
        val viewHistories: Page<ViewHistory> = viewHistoryRepository.findByComicId(comic.id, pageable)

        // then
        assertEquals(2, viewHistories.totalElements, "해당 작품의 조회 이력은 2개여야 합니다.")
        assertEquals(1, viewHistories.totalPages, "전체 페이지 수는 1페이지여야 합니다.")
    }

    @DisplayName("특정 작품에 대한 조회 이력 삭제")
    @Test
    fun testDeleteByComicId() {
        // when
        viewHistoryRepository.deleteByComicId(comic.id)

        // then
        val pageable = PageRequest.of(0, 10)
        val viewHistories: Page<ViewHistory> = viewHistoryRepository.findByComicId(comic.id, pageable)
        assertEquals(0, viewHistories.totalElements, "해당 작품의 조회 이력은 모두 삭제되어야 합니다.")
    }
}
