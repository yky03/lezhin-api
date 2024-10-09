package com.lezhin.api.comic.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "member")
data class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val username: String,

    @Column(nullable = false)
    val isAdult: Boolean = false,
)

