package com.home.balance.api.repositories

import com.home.balance.api.models.entities.Limit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LimitRepository : JpaRepository<Limit, Long>{}


