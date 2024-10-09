package com.lezhin.api.comic.domain.repository

import com.lezhin.api.comic.domain.entity.Buy
import org.springframework.data.jpa.repository.JpaRepository

interface BuyRepository : JpaRepository<Buy, Long> {
    fun existsByComicIdAndMemberId(comicId: Long, memberId: Long): Boolean
}