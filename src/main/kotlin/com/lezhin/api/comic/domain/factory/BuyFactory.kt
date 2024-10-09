package com.lezhin.api.comic.domain.factory

import com.lezhin.api.comic.domain.entity.Buy
import com.lezhin.api.comic.domain.entity.Comic
import com.lezhin.api.comic.domain.entity.Member
import com.lezhin.api.comic.domain.entity.PaymentStatus

object BuyFactory {
    fun createBuy(member: Member, comic: Comic, paymentStatus: PaymentStatus, amountPaid: Int): Buy {
        return Buy(
            member = member,
            comic = comic,
            paymentStatus = paymentStatus,
            amountPaid = amountPaid
        )
    }
}