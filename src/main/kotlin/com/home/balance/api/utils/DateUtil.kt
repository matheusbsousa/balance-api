package com.home.balance.api.utils

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class DateUtil {

    companion object {

        fun toLocalDate(date: Date): LocalDate {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }
}