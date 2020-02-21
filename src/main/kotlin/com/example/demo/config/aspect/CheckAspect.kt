package com.example.demo.config.aspect

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.DeclareError

@Aspect
class CheckAspect {

    @DeclareError("!Architecture.repository() && (call (* java.sql..*.*(..)) || call (* javax.sql..*.*(..)))")
    val jdbcOutsideRepository = "Only call jdbc code from within repositories."

}
