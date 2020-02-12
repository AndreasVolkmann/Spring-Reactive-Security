package com.example.demo.web

import com.example.demo.model.ProjectFilter
import com.example.demo.repository.ProjectRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/project")
class ProjectController(
    private val projectRepository: ProjectRepository
) {

    @PostMapping
    fun getProjects(@RequestBody filter: ProjectFilter) = projectRepository.findAll(filter)
}
