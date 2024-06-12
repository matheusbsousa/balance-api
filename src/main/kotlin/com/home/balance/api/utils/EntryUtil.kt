package com.home.balance.api.utils

class EntryUtil {

    fun extractDescription(description: String): String {
        return description.trim()
            .replace("_", " ")
            .lowercase()
            .replaceFirstChar(Char::titlecase)
    }

    fun extractDouble(string: String): Double {
        if (string.isNotEmpty()) {
            return string
                .replace("\"", "")
                .replace("R$", "")
                .replace("(", "")
                .replace(")", "")
                .replace(",", "").toDouble()
        }

        return 0.0
    }

}