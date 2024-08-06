package com.home.balance.api.services

import com.home.balance.api.repositories.ParametersRepository
import org.springframework.stereotype.Service

@Service
class ParametersService(private val parametersRepository: ParametersRepository) {

    fun getFirstDayOfTheMonth(): Int {
        return parametersRepository.findAll()[0].firstDay
    }

    fun getUnknownCategoryName(): String {
        return parametersRepository.findAll()[0].unknownCategoryName
    }
}