package com.example.demo.model

data class ProjectFilter(
    val status: List<Int> = listOf(),
    val current: Int = 1,
    val pageSize: Int = 10
)
