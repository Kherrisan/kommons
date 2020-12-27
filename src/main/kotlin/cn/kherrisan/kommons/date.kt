package cn.kherrisan.kommons

import org.apache.commons.lang3.time.DateFormatUtils
import java.util.Date

class KDate(time: Long) : Date(time) {
    override fun toString(): String = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(this)
    constructor() : this(currentMS())
}

fun Date.toKDate() = KDate(time)