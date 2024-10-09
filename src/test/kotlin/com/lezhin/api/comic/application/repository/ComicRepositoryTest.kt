package com.lezhin.api.comic.application.repository

import com.lezhin.api.comic.domain.entity.Comic
import com.lezhin.api.comic.domain.repository.ComicRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class ComicRepositoryTest @Autowired constructor(
    val comicRepository: ComicRepository,
    val entityManager: TestEntityManager
) {

    @BeforeEach
    fun setUp() {
        val comics = listOf(
            Comic(title = "Comic 1", isFree = false, viewCount = 100, price = 1000, purchaseCount = 50),
            Comic(title = "Comic 2", isFree = true, viewCount = 200, price = 12000, purchaseCount = 150),
            Comic(title = "Comic 3", isFree = true, viewCount = 50, price = 3000,purchaseCount = 30),
            Comic(title = "Comic 4", isFree = false, viewCount = 300, price = 150, purchaseCount = 250),
            Comic(title = "Comic 5", isFree = false, viewCount = 400, price = 500, purchaseCount = 350),
            Comic(title = "Comic 6", isFree = true, viewCount = 500, price = 20, purchaseCount = 450),
            Comic(title = "Comic 7", isFree = false, viewCount = 150, price = 1000, purchaseCount = 120),
            Comic(title = "Comic 8", isFree = true, viewCount = 600, price = 10, purchaseCount = 550),
            Comic(title = "Comic 9", isFree = false, viewCount = 700, price = 900, purchaseCount = 650),
            Comic(title = "Comic 10", isFree = true, viewCount = 800, price = 5, purchaseCount = 750),
            Comic(title = "Comic 11", isFree = false, viewCount = 900, price = 9000, purchaseCount = 850)
        )

        comics.forEach { entityManager.persist(it) }
        entityManager.flush()
    }

    @DisplayName("조회수가 많은 상위 10개 작품 조회")
    @Test
    fun testFindTop10ByOrderByViewCountDesc() {
        // when
        val topComics = comicRepository.findTop10ByOrderByViewCountDesc()

        // then
        assertEquals(10, topComics.size)
        assertEquals("Comic 11", topComics[0].title)  // 가장 조회수가 높은 작품
        assertEquals(900, topComics[0].viewCount)
        assertEquals("Comic 1", topComics[9].title)  // 10번째로 조회수가 높은 작품
    }

    @DisplayName("구매수가 많은 상위 10개 작품 조회")
    @Test
    fun testFindTop10ByOrderByPurchaseCountDesc() {
        // when
        val topPurchasedComics = comicRepository.findTop10ByOrderByPurchaseCountDesc()

        // then
        assertEquals(10, topPurchasedComics.size)
        assertEquals("Comic 11", topPurchasedComics[0].title)  // 가장 구매수가 많은 작품
        assertEquals(850, topPurchasedComics[0].purchaseCount)
        assertEquals("Comic 1", topPurchasedComics[9].title)  // 10번째로 구매수가 많은 작품
    }
}
