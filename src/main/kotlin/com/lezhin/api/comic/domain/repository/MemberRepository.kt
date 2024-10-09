package com.lezhin.api.comic.domain.repository

import com.lezhin.api.comic.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>