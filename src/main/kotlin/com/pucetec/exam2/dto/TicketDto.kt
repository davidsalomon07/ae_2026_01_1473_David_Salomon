package com.pucetec.exam2.dto

import java.time.LocalDateTime

data class TicketRequest(
    val plate: String,
    val parkingSpaceId: Long
)

data class TicketResponse(
    val id: Long,
    val plate: String,
    val entryTime: LocalDateTime,
    val exitTime: LocalDateTime?,
    val parkingSpace: ParkingSpaceResponse
)
