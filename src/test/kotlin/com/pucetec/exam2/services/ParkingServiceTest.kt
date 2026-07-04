package com.pucetec.exam2.services

import com.pucetec.exam2.dto.TicketRequest
import com.pucetec.exam2.entities.ParkingSpace
import com.pucetec.exam2.entities.Ticket
import com.pucetec.exam2.exceptions.SpaceNotFoundException
import com.pucetec.exam2.exceptions.SpaceOccupiedException
import com.pucetec.exam2.exceptions.TicketClosedException
import com.pucetec.exam2.exceptions.TicketNotFoundException
import com.pucetec.exam2.repositories.ParkingSpaceRepository
import com.pucetec.exam2.repositories.TicketRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.verify
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class ParkingServiceTest {

    @Mock
    lateinit var parkingSpaceRepository: ParkingSpaceRepository

    @Mock
    lateinit var ticketRepository: TicketRepository

    @InjectMocks
    lateinit var parkingService: ParkingService

    @Test
    fun `getAvailableSpaces returns list of available spaces`() {
        val space = ParkingSpace(1L, "A-1", false)
        `when`(parkingSpaceRepository.findByOccupiedFalse()).thenReturn(listOf(space))

        val response = parkingService.getAvailableSpaces()

        assertEquals(1, response.size)
        assertEquals("A-1", response[0].code)
        assertFalse(response[0].occupied)
    }

    @Test
    fun `registerEntry succeeds when space is free`() {
        val request = TicketRequest("PBA-1234", 1L)
        val space = ParkingSpace(1L, "A-1", false)
        val savedTicket = Ticket(1L, "PBA-1234", LocalDateTime.now(), null, space)

        `when`(parkingSpaceRepository.findById(1L)).thenReturn(Optional.of(space))
        `when`(ticketRepository.save(any(Ticket::class.java))).thenReturn(savedTicket)

        val response = parkingService.registerEntry(request)

        assertEquals(1L, response.id)
        assertEquals("PBA-1234", response.plate)
        verify(parkingSpaceRepository, times(1)).save(any(ParkingSpace::class.java))
        verify(ticketRepository, times(1)).save(any(Ticket::class.java))
    }

    @Test
    fun `registerEntry throws SpaceNotFoundException when space does not exist`() {
        val request = TicketRequest("PBA-1234", 1L)
        `when`(parkingSpaceRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<SpaceNotFoundException> {
            parkingService.registerEntry(request)
        }
    }

    @Test
    fun `registerEntry throws SpaceOccupiedException when space is occupied`() {
        val request = TicketRequest("PBA-1234", 1L)
        val space = ParkingSpace(1L, "A-1", true)
        `when`(parkingSpaceRepository.findById(1L)).thenReturn(Optional.of(space))

        assertThrows<SpaceOccupiedException> {
            parkingService.registerEntry(request)
        }
    }

    @Test
    fun `registerExit succeeds when ticket is open`() {
        val space = ParkingSpace(1L, "A-1", true)
        val ticket = Ticket(10L, "PBA-1234", LocalDateTime.now().minusHours(2), null, space)
        val savedTicket = Ticket(10L, "PBA-1234", ticket.entryTime, LocalDateTime.now(), space)

        `when`(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket))
        `when`(ticketRepository.save(any(Ticket::class.java))).thenReturn(savedTicket)

        val response = parkingService.registerExit(10L)

        assertNotNull(response.exitTime)
        verify(parkingSpaceRepository, times(1)).save(any(ParkingSpace::class.java))
        verify(ticketRepository, times(1)).save(any(Ticket::class.java))
    }

    @Test
    fun `registerExit throws TicketNotFoundException when ticket does not exist`() {
        `when`(ticketRepository.findById(10L)).thenReturn(Optional.empty())

        assertThrows<TicketNotFoundException> {
            parkingService.registerExit(10L)
        }
    }

    @Test
    fun `registerExit throws TicketClosedException when ticket is already closed`() {
        val space = ParkingSpace(1L, "A-1", false)
        val ticket = Ticket(10L, "PBA-1234", LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), space)

        `when`(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket))

        assertThrows<TicketClosedException> {
            parkingService.registerExit(10L)
        }
    }
}
