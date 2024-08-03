package me.gegenbauer.catspy.log.filter

import me.gegenbauer.catspy.log.metadata.Column
import me.gegenbauer.catspy.view.filter.FilterItem

data class FilterInfo(
    val column: Column,
    val filterItem: FilterItem,
    val filter: ColumnFilter
)