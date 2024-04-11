package com.users.msusers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient

class MsUsersApplication

fun main(args: Array<String>) {
	runApplication<MsUsersApplication>(*args)
}
