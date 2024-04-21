package com.users.msusers.entity

import jakarta.persistence.*

@Entity
@Table(name = "asignaciones")
class Assignation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    var assignationId: Long? = null

    @Column(name = "estado")
    var status: Boolean = true

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tutor")
    var tutorId: Person? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_relator")
    var relatorId: Person? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante")
    var studentId: Person? = null


}