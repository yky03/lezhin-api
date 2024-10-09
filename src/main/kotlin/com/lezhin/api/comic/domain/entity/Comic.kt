package com.lezhin.api.comic.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comic")
data class Comic(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "price", nullable = false)
    val price: Int,

    @Column(name = "is_free", nullable = false)
    var isFree: Boolean,

    @Column(name = "purchase_count", nullable = false)
    var purchaseCount: Int = 0,

    @Column(name = "view_count", nullable = false)
    var viewCount: Int = 0,

    @Column(name = "event_start_date", nullable = true)
    var eventStartDate: LocalDateTime? = null,

    @Column(name = "event_end_date", nullable = true)
    var eventEndDate: LocalDateTime? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
)
