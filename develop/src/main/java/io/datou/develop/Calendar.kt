package io.datou.develop

import java.util.Calendar

var Calendar.year: Int
    get() = get(Calendar.YEAR)
    set(value) {
        set(Calendar.YEAR, value)
    }

var Calendar.month: Int
    get() = get(Calendar.MONTH)
    set(value) {
        set(Calendar.MONTH, value)
    }

var Calendar.dayOfMonth: Int
    get() = get(Calendar.DAY_OF_MONTH)
    set(value) {
        set(Calendar.DAY_OF_MONTH, value)
    }

var Calendar.weekOfMonth: Int
    get() = get(Calendar.WEEK_OF_MONTH)
    set(value) {
        set(Calendar.WEEK_OF_MONTH, value)
    }

var Calendar.dayOfWeek: Int
    get() = get(Calendar.DAY_OF_WEEK)
    set(value) {
        set(Calendar.DAY_OF_WEEK, value)
    }

val Calendar.dayOfWeekFormat: String
    get() = when (dayOfWeek) {
        Calendar.SUNDAY -> "周日"
        Calendar.MONDAY -> "周一"
        Calendar.TUESDAY -> "周二"
        Calendar.WEDNESDAY -> "周三"
        Calendar.THURSDAY -> "周四"
        Calendar.FRIDAY -> "周五"
        Calendar.SATURDAY -> "周六"
        else -> throw IllegalArgumentException()
    }

val Calendar.weekOfMonthByCN: Int
    get() {
        weekOfMonth.run {
            return if (dayOfWeek == Calendar.SUNDAY && this > 1) {
                this - 1
            } else {
                this
            }
        }
    }

var Calendar.dayOfYear: Int
    get() = get(Calendar.DAY_OF_YEAR)
    set(value) {
        set(Calendar.DAY_OF_YEAR, value)
    }

var Calendar.hourOfDay: Int
    get() = get(Calendar.HOUR_OF_DAY)
    set(value) {
        set(Calendar.HOUR_OF_DAY, value)
    }

var Calendar.hour: Int
    get() = get(Calendar.HOUR)
    set(value) {
        set(Calendar.HOUR, value)
    }

var Calendar.minute: Int
    get() = get(Calendar.MINUTE)
    set(value) {
        set(Calendar.MINUTE, value)
    }

var Calendar.second: Int
    get() = get(Calendar.SECOND)
    set(value) {
        set(Calendar.SECOND, value)
    }

var Calendar.millisecond: Int
    get() = get(Calendar.MILLISECOND)
    set(value) {
        set(Calendar.MILLISECOND, value)
    }

val Calendar.monthRangeInYear
    get() = getActualMinimum(Calendar.MONTH)..
            getActualMaximum(Calendar.MONTH)

val Calendar.dayRangeInMonth
    get() = getActualMinimum(Calendar.DAY_OF_MONTH)..
            getActualMaximum(Calendar.DAY_OF_MONTH)

val Calendar.dayRangeInYear
    get() = getActualMinimum(Calendar.DAY_OF_YEAR)..
            getActualMaximum(Calendar.DAY_OF_YEAR)

fun Calendar.isSomeDay(calendar: Calendar): Boolean {
    return year == calendar.year
            && dayOfYear == calendar.dayOfYear
}

fun Calendar.isYesterday(calendar: Calendar): Boolean {
    return year == calendar.year
            && dayOfYear + 1 == calendar.dayOfYear
}

fun Calendar.isSomeWeek(calendar: Calendar): Boolean {
    return year == calendar.year
            && month == calendar.month
            && weekOfMonth == weekOfMonth
}

fun Calendar.isSomeWeekByCN(calendar: Calendar): Boolean {
    return year == calendar.year
            && month == calendar.month
            && weekOfMonthByCN == weekOfMonthByCN
}

fun Calendar.isSomeMonth(calendar: Calendar): Boolean {
    return year == calendar.year
            && month == calendar.month
}

fun Calendar.isSomeYeah(calendar: Calendar): Boolean {
    return year == calendar.year
}

