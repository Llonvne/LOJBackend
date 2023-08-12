package cn.llonvne.lojbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LojBackendApplication

fun main(args: Array<String>) {
    val application =  runApplication<LojBackendApplication>(*args)
}
