package me.gegenbauer.catspy.log.model

import me.gegenbauer.catspy.cache.PatternProvider
import me.gegenbauer.catspy.cache.PatternProvider.Companion.toPatternKey
import me.gegenbauer.catspy.common.log.FilterItem
import me.gegenbauer.catspy.common.log.LogLevel
import me.gegenbauer.catspy.common.log.toFilterItem
import me.gegenbauer.catspy.context.ServiceManager
import java.lang.StringBuilder
import java.util.*
import java.util.regex.Pattern

class LogcatRealTimeFilter(
    val filterLog: FilterItem,
    val filterTag: FilterItem,
    val filterPid: FilterItem,
    val filterTid: FilterItem,
    val filterLevel: LogLevel,
    val matchCase: Boolean = false
) : LogFilter<LogcatLogItem> {

    constructor(
        filterLog: String,
        filterTag: String,
        filterPid: String,
        filterTid: String,
        filterLevel: LogLevel,
        matchCase: Boolean = false
    ) : this(
        filterLog.toFilterItem(matchCase),
        filterTag.toFilterItem(matchCase),
        filterPid.toFilterItem(matchCase),
        filterTid.toFilterItem(matchCase),
        filterLevel,
        matchCase
    )

    override fun filter(item: LogcatLogItem): Boolean {
        if (this == emptyRealTimeFilter) {
            return true
        }
        if (item.level < this.filterLevel) {
            return false
        }
        val logLine = item.logLine
        val tag = item.tag
        val pid = item.pid
        val tid = item.tid

        return (matchHidePattern(filterLog.negativeFilter, logLine) &&
                matchShowPattern(filterLog.positiveFilter, logLine) &&
                matchHidePattern(filterTag.negativeFilter, tag) &&
                matchShowPattern(filterTag.positiveFilter, tag) &&
                matchHidePattern(filterPid.negativeFilter, pid) &&
                matchShowPattern(filterPid.positiveFilter, pid) &&
                matchHidePattern(filterTid.negativeFilter, tid) &&
                matchShowPattern(filterTid.positiveFilter, tid))
    }

    private fun matchShowPattern(pattern: Pattern, text: String): Boolean {
        if (pattern == PatternProvider.emptyPattern) return true
        return pattern.matcher(text).find()
    }

    private fun matchHidePattern(pattern: Pattern, text: String): Boolean {
        if (pattern == PatternProvider.emptyPattern) return true
        return pattern.matcher(text).find().not()
    }

    override fun hashCode(): Int {
        return Objects.hash(filterLog, filterTag, filterPid, filterTid, filterLevel, matchCase)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is LogcatRealTimeFilter) {
            return false
        }
        return filterLog == other.filterLog &&
                filterTag == other.filterTag &&
                filterPid == other.filterPid &&
                filterTid == other.filterTid &&
                filterLevel == other.filterLevel &&
                matchCase == other.matchCase
    }

    override fun toString(): String {
        return "LogcatRealTimeFilter(filterLog=$filterLog, filterTag=$filterTag, filterPid=$filterPid, " +
                "filterTid=$filterTid, filterLevel=$filterLevel, matchCase=$matchCase)"
    }

    companion object {
        val emptyRealTimeFilter = LogcatRealTimeFilter("", "", "", "", LogLevel.VERBOSE)
    }
}