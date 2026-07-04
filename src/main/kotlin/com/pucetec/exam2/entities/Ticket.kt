package com.pucetec.exam2.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tickets")
class Ticket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val plate: String = "",

    val entryTime: LocalDateTime = LocalDateTime.now(),

    val exitTime: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_space_id", nullable = false)
    val parkingSpace: ParkingSpace = ParkingSpace()
)
