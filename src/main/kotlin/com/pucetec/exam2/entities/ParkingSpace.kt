package com.pucetec.exam2.entities

import jakarta.persistence.*

@Entity
@Table(name = "parking_spaces")
class ParkingSpace(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val code: String = "",

    val occupied: Boolean = false
)
