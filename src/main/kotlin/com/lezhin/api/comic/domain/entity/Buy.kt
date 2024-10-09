package com.lezhin.api.comic.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "buy")
data class Buy(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comic_id", nullable = false)
    val comic: Comic,

    @Column(name = "purchase_date", nullable = false)
    val purchaseDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    val paymentStatus: PaymentStatus,

    @Column(name = "amount_paid", nullable = false)
    val amountPaid: Int
)

enum class PaymentStatus {
    SUCCESS, PENDING, FAILED
}
