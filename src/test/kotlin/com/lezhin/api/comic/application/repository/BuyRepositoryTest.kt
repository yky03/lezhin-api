package com.lezhin.api.comic.application.repository

import com.lezhin.api.comic.domain.entity.Buy
import com.lezhin.api.comic.domain.entity.Comic
import com.lezhin.api.comic.domain.entity.Member
import com.lezhin.api.comic.domain.entity.PaymentStatus
import com.lezhin.api.comic.domain.repository.BuyRepository
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class BuyRepositoryTest @Autowired constructor(
    val buyRepository: BuyRepository,
    val entityManager: TestEntityManager
) {

    private lateinit var comic: Comic
    private lateinit var member: Member

    @BeforeEach
    fun setUp() {
        // 작품과 회원 생성
        comic = Comic(title = "Test Comic", isFree = false, viewCount = 100, price = 1000, purchaseCount = 50)
        member = Member(username = "Test Member", isAdult = true)

        entityManager.persist(comic)
        entityManager.persist(member)
        entityManager.flush()  // DB에 반영

        // 구매 내역 저장
        val buy = Buy(member = member, comic = comic, paymentStatus = PaymentStatus.SUCCESS, amountPaid = 1000)
        entityManager.persist(buy)
        entityManager.flush()  // DB에 반영
    }

    @DisplayName("작품과 회원 ID로 구매 여부 확인")
    @Test
    fun testExistsByComicIdAndMemberId() {
        // when
        val exists = buyRepository.existsByComicIdAndMemberId(comic.id, member.id)

        // then
        assertTrue(exists, "해당 작품과 회원으로 구매 내역이 존재해야 합니다.")
    }

    @DisplayName("구매 내역이 없는 경우 구매 여부 확인")
    @Test
    fun testExistsByComicIdAndMemberIdWhenNoPurchase() {
        // when
        val nonExistent = buyRepository.existsByComicIdAndMemberId(comic.id, 999L)

        // then
        assertFalse(nonExistent, "존재하지 않는 회원 ID로는 구매 내역이 없어야 합니다.")
    }
}