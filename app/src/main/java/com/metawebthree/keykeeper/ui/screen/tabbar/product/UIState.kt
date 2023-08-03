package com.metawebthree.keykeeper.ui.screen.tabbar.product

data class UIState(
    var loading: Boolean = false,
    var meridiemType: MeridiemType = MeridiemType.AM,
    var is24hours: Boolean = true,
) {

}

data class DateState(var date: String = "", var week: String = "") {
    companion object {
        val weekNames = listOf<String>("日", "一", "二", "三", "四", "俩", "六")
    }

    fun getWeekName(week: Int): String {
        return weekNames[week]
    }
}

data class TimeState(var hour: Int = 0, var minute: Int = 0, var second: Int = 0) {
    fun get24HoursStr(): String {
        return if (hour < 10) "0$hour" else hour.toString()
    }

    fun getMeridiemHourStr(): String {
        return (hour % 12).let {
            if (it < 10) "0$it" else it.toString()
        }
    }

    fun getMinuteStr(): String {
        return if (minute < 10) "0$minute" else minute.toString()
    }

    fun getSecondStr(): String {
        return if (second < 10) "0$second" else second.toString()
    }
}

enum class MeridiemType {
    AM,
    PM
}