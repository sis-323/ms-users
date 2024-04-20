package com.users.msusers.entity

import jakarta.persistence.*


@Entity
@Table(name = "persona")
class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    var idPerson : Long = 0

    @Column(name = "nombre")
    var name : String = ""

    @Column(name = "apellido_paterno")
    var lastName : String = ""

    @Column(name = "apellido_materno")
    var motherLastName : String = ""

    @Column(name = "correo")
    var email : String = ""

    @Column(name = "numero_celular")
    var phoneNumber : String = ""

    @Column(name = "estado")
    var status: Boolean = true

    @Column(name = "grupo")
    var group: String = ""

    @Column(name = "id_kc")
    var idKc: String = ""

    @OneToOne
    @JoinColumn(name = "id_modalidad")
    var modality: Modality? = null

    @Column(name = "semestre")
    var semester: String? = null

}