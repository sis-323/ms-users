package com.users.msusers.entity

import jakarta.persistence.*

@Entity
@Table(name = "modalidades")
class Modality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modalidad")
    var idModality : Long = 0

    @Column(name = "modalidad")
    var modality : String = ""


}