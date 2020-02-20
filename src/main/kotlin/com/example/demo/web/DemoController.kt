package com.example.demo.web

import com.example.demo.service.DemoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController(private val demoService: DemoService) {

    @GetMapping
    fun getRoot(): String = demoService.run(false)
}
