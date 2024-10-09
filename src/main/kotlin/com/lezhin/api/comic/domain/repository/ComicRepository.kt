package com.lezhin.api.comic.domain.repository

import com.lezhin.api.comic.domain.entity.Comic
import org.springframework.data.jpa.repository.JpaRepository

interface ComicRepository : JpaRepository<Comic, Long> {
    // 조회수가 많은 상위 10개의 작품 조회
    fun findTop10ByOrderByViewCountDesc(): List<Comic>

    // 구매수가 많은 상위 10개의 작품 조회
    fun findTop10ByOrderByPurchaseCountDesc(): List<Comic>
}
