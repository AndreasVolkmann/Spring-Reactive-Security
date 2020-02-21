package com.example.demo.service

import com.example.demo.config.aspect.Activity
import kotlinx.coroutines.delay
import org.springframework.stereotype.Service

@Service
class DemoService {

    @Activity
    fun run(someBool: Boolean): String {
        Thread.sleep(100)
        if (someBool) throw RuntimeException("error")
        return "OK"
    }

    fun noAnnotation() {

    }

    fun test() {
//        javax.sql.rowset.RowSetMetaDataImpl().getCatalogName(2)
    }
}
