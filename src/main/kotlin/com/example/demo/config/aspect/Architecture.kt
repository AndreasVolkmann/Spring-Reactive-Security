package com.example.demo.config.aspect

import org.aspectj.lang.annotation.Pointcut

class Architecture {

    @Pointcut("execution(* (@org.springframework.stereotype.Repository *).*(..))")
    fun service() = Unit

    @Pointcut("execution (* (@org.springframework.stereotype.Service *).*(..))")
    fun repository() = Unit

    @Pointcut("(@annotation(Activity) && execution(* *(..)))")
    fun activity() = Unit
}
