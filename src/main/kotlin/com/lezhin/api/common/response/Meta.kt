package com.lezhin.api.common.response

data class Meta(
    val currentPage: Int,
    val totalPageCount: Int,
    val pageSize: Int,
    val lastPage: Int
)