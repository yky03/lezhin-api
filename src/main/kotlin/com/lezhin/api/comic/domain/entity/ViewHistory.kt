package com.lezhin.api.comic.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "view_history")
data class ViewHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comic_id", nullable = false)
    val comic: Comic,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @Column(name = "viewed_at", nullable = false)
    val viewedAt: LocalDateTime = LocalDateTime.now()
)

