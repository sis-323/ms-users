package com.users.msusers.api

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/test-file")
class TestFileAPI {

    companion object {
        private val logger = LoggerFactory.getLogger(TestFileAPI::class.java)
    }

    @PostMapping("/upload")
     fun uploadFile(
                    @RequestParam("requirements") requirements: List<MultipartFile>,
                    @RequestParam("userKcId") userKcId: String): ResponseEntity<String> {
        logger.info("Uploading file")
        logger.info("User KC ID: $userKcId")
        for (requirement in requirements) {
            logger.info("Requirement name: ${requirement.originalFilename}")
        }
        return ResponseEntity.ok().body("File uploaded")
    }
}