package com.lezhin.api

import com.lezhin.api.comic.domain.entity.Comic
import com.lezhin.api.comic.domain.entity.Member
import com.lezhin.api.comic.domain.repository.ComicRepository
import com.lezhin.api.comic.domain.repository.MemberRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

@Configuration
class SampleDataInitializer {

    @Bean
    fun initializeSampleData(
        comicRepository: ComicRepository,
        memberRepository: MemberRepository
    ): CommandLineRunner {
        return CommandLineRunner {

            // Member 샘플 데이터 저장
            val member1 = Member(id = 1, username = "양기열", isAdult = true)
            val member2 = Member(id = 2, username = "김민철", isAdult = false)
            memberRepository.saveAll(listOf(member1, member2))

            // Comic 샘플 데이터 저장
            val comic1 = Comic(
                id = 1,
                title = "문학소년",
                price = 1500,
                isFree = true, // 무료 작품
                purchaseCount = 5300,
                viewCount = 9255,
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                eventStartDate = LocalDateTime.of(2024, 10, 1, 0, 0),
                eventEndDate = LocalDateTime.of(2024, 12, 31, 23, 59)
            )
            val comic2 = Comic(
                id = 2,
                title = "브라이노의 연구소",
                price = 2500,
                isFree = false, // 유료 작품
                purchaseCount = 500,
                viewCount = 700,
                createdAt = LocalDateTime.now(),
                updatedAt = null
            )
            comicRepository.saveAll(listOf(comic1, comic2))
            println("샘플 데이터 초기화 완료.")
        }
    }
}