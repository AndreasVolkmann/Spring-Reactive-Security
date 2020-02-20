package com.example.demo.config.aspect.compile

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.DeclareError

@Aspect
class CheckAspect {

    @DeclareError("(call (* java.sql..*.*(..)) || call (* javax.sql..*.*(..)))")
    val jdbcFromService = "Do not call jdbc code from services."

}
