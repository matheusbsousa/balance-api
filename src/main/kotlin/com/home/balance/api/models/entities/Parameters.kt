package com.home.balance.api.models.entities

import com.home.balance.api.utils.Constants
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "PARAMETERS", schema = Constants.SCHEMA)
class Parameters(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,
    var firstDay: Int,
    var unknownCategoryName: String
) {





}