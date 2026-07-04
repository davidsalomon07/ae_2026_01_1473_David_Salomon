package com.pucetec.exam2.dto

data class ParkingSpaceRequest(
    val code: String,
    val occupied: Boolean
)

data class ParkingSpaceResponse(
    val id: Long,
    val code: String,
    val occupied: Boolean
)
