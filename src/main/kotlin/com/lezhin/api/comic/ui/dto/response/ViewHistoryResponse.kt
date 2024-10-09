package com.lezhin.api.comic.ui.dto.response

import com.lezhin.api.comic.domain.entity.ViewHistory
import com.lezhin.api.common.response.Meta

data class ViewHistoryResponse(
    val meta: Meta,
    val data: MutableList<ViewHistory>
)