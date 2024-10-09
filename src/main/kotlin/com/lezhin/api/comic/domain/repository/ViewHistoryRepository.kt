package com.lezhin.api.comic.domain.repository

import com.lezhin.api.comic.domain.entity.ViewHistory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ViewHistoryRepository : JpaRepository<ViewHistory, Long> {
    // 특정 작품의 조회 이력 조회
    fun findByComicId(comicId: Long, pageable: Pageable): Page<ViewHistory>

    // 특정 작품의 조회 이력 삭제
    fun deleteByComicId(comicId: Long)
}
