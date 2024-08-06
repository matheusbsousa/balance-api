package com.home.balance.api.repositories

import com.home.balance.api.models.entities.Parameters
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParametersRepository: JpaRepository<Parameters, Long> {

}
