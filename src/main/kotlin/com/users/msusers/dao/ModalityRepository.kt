package com.users.msusers.dao

import com.users.msusers.entity.Modality
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ModalityRepository  : JpaRepository<Modality, Long> {

    fun findByIdModality(idModality: Long): Modality
}