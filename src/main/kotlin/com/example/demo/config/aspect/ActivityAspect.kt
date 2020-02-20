package com.example.demo.config.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class ActivityAspect {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Around("(@annotation(Activity) && execution(* *(..))) || Architecture.repository() || Architecture.service()")
    fun monitorActivity(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        val name = proceedingJoinPoint.staticPart.signature.toShortString()
        var state = "SUCCESS"
        logger.info("Activity start: $name")
        val startTime = System.currentTimeMillis()
        try {
            return proceedingJoinPoint.proceed()
        }
        catch (ex: Exception) {
            state = "ERROR"
            throw ex
        }
        finally {
            val timeElapsed = System.currentTimeMillis() - startTime
            logger.info("Activity end: $name ($state $timeElapsed ms)")
        }
    }
}
