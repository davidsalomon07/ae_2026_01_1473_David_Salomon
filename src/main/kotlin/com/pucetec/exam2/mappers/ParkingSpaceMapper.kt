package com.pucetec.exam2.mappers

import com.pucetec.exam2.dto.ParkingSpaceRequest
import com.pucetec.exam2.dto.ParkingSpaceResponse
import com.pucetec.exam2.entities.ParkingSpace

fun ParkingSpaceRequest.toEntity() = ParkingSpace(
    code = this.code,
    occupied = this.occupied
)

fun ParkingSpace.toResponse() = ParkingSpaceResponse(
    id = this.id,
    code = this.code,
    occupied = this.occupied
)
