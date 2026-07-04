package com.pucetec.exam2.services

import com.pucetec.exam2.dto.ParkingSpaceResponse
import com.pucetec.exam2.dto.TicketRequest
import com.pucetec.exam2.dto.TicketResponse
import com.pucetec.exam2.entities.ParkingSpace
import com.pucetec.exam2.entities.Ticket
import com.pucetec.exam2.exceptions.SpaceNotFoundException
import com.pucetec.exam2.exceptions.SpaceOccupiedException
import com.pucetec.exam2.exceptions.TicketClosedException
import com.pucetec.exam2.exceptions.TicketNotFoundException
import com.pucetec.exam2.mappers.toResponse
import com.pucetec.exam2.repositories.ParkingSpaceRepository
import com.pucetec.exam2.repositories.TicketRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class ParkingService(
    private val parkingSpaceRepository: ParkingSpaceRepository,
    private val ticketRepository: TicketRepository
) {
    private val logger = LoggerFactory.getLogger(ParkingService::class.java)

    @Transactional(readOnly = true)
    fun getAvailableSpaces(): List<ParkingSpaceResponse> {
        logger.info("Fetching all available parking spaces")
        return parkingSpaceRepository.findByOccupiedFalse().map { it.toResponse() }
    }

    fun registerEntry(request: TicketRequest): TicketResponse {
        logger.info("Registering entry for plate: {} at space: {}", request.plate, request.parkingSpaceId)

        val space = parkingSpaceRepository.findById(request.parkingSpaceId)
            .orElseThrow {
                logger.error("Parking space with ID {} not found", request.parkingSpaceId)
                SpaceNotFoundException("Parking space not found with ID: ${request.parkingSpaceId}")
            }

        if (space.occupied) {
            logger.error("Parking space with ID {} is already occupied", request.parkingSpaceId)
            throw SpaceOccupiedException("Parking space is already occupied")
        }

        val updatedSpace = ParkingSpace(
            id = space.id,
            code = space.code,
            occupied = true
        )
        parkingSpaceRepository.save(updatedSpace)

        val ticket = Ticket(
            plate = request.plate,
            entryTime = LocalDateTime.now(),
            parkingSpace = updatedSpace
        )
        val savedTicket = ticketRepository.save(ticket)

        logger.info("Ticket created successfully with ID: {}", savedTicket.id)
        return savedTicket.toResponse()
    }

    fun registerExit(ticketId: Long): TicketResponse {
        logger.info("Registering exit for ticket ID: {}", ticketId)

        val ticket = ticketRepository.findById(ticketId)
            .orElseThrow {
                logger.error("Ticket with ID {} not found", ticketId)
                TicketNotFoundException("Ticket not found with ID: $ticketId")
            }

        if (ticket.exitTime != null) {
            logger.error("Ticket with ID {} is already closed", ticketId)
            throw TicketClosedException("Ticket is already closed")
        }

        val space = ticket.parkingSpace
        val updatedSpace = ParkingSpace(
            id = space.id,
            code = space.code,
            occupied = false
        )
        parkingSpaceRepository.save(updatedSpace)

        val updatedTicket = Ticket(
            id = ticket.id,
            plate = ticket.plate,
            entryTime = ticket.entryTime,
            exitTime = LocalDateTime.now(),
            parkingSpace = updatedSpace
        )
        val savedTicket = ticketRepository.save(updatedTicket)

        logger.info("Exit registered successfully for ticket ID: {}", savedTicket.id)
        return savedTicket.toResponse()
    }
}
