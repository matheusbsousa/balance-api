package com.home.balance.api.utils

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class DateUtil {

    companion object {

        fun toLocalDate(date: Date): LocalDate {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        }

        fun getMonth(firstDay: Int, date: Date): Int {
            val localDate = toLocalDate(date)

            if (firstDay == 1) {
                return localDate.monthValue
            }

            return if (localDate.dayOfMonth < firstDay){
                localDate.minusMonths(1).monthValue
            }else{
                localDate.monthValue
            }

        }
    }
}