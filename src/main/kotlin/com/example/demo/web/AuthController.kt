package com.example.demo.web

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class AuthController {

    @RequestMapping("/user")
    fun user(user: Principal) = user
}
