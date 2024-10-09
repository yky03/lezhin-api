package com.lezhin.api.comic.ui.dto.response

data class PurchaseResponse(
    val comicId: Long,
    val userId: Long,
    val status: String
)