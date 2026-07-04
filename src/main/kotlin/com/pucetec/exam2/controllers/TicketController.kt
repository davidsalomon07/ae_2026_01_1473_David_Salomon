package com.pucetec.exam2.controllers

import com.pucetec.exam2.dto.TicketRequest
import com.pucetec.exam2.dto.TicketResponse
import com.pucetec.exam2.services.ParkingService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tickets")
class TicketController(
    private val parkingService: ParkingService
) {
    @PostMapping("/entry")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerEntry(@RequestBody request: TicketRequest): TicketResponse {
        return parkingService.registerEntry(request)
    }

    @PutMapping("/{id}/exit")
    fun registerExit(@PathVariable id: Long): TicketResponse {
        return parkingService.registerExit(id)
    }
}
