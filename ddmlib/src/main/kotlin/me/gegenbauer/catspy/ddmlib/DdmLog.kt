package me.gegenbauer.catspy.ddmlib

import me.gegenbauer.catspy.log.GLog
import me.gegenbauer.catspy.log.ILogger

object DdmLog : ILogger by GLog {
    var ddmDebug = false

    override fun v(tag: String, msg: String) {
        if (ddmDebug) {
            GLog.v(tag, msg)
        }
    }

    override fun d(tag: String, msg: String) {
        if (ddmDebug) {
            GLog.d(tag, msg)
        }
    }

    override fun i(tag: String, msg: String) {
        if (ddmDebug) {
            GLog.i(tag, msg)
        }
    }
}