package com.pucetec.exam2.controllers

import com.pucetec.exam2.dto.ParkingSpaceResponse
import com.pucetec.exam2.services.ParkingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/parking-spaces")
class ParkingSpaceController(
    private val parkingService: ParkingService
) {
    @GetMapping("/available")
    fun getAvailableSpaces(): List<ParkingSpaceResponse> {
        return parkingService.getAvailableSpaces()
    }
}
