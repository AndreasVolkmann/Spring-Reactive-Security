package com.example.demo.repository

import com.example.demo.configuration.DevConfiguration
import com.example.demo.model.Project
import com.example.demo.model.ProjectFilter
import com.example.demo.model.ProjectStatus
import com.github.javafaker.Faker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.data.r2dbc.core.*
import org.springframework.stereotype.Component
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait

@Component
class ProjectRepository(
    private val client: DatabaseClient,
    private val operator: TransactionalOperator,
    private val devConfiguration: DevConfiguration
) {
    private val table = "projects"

    fun findAll(filter: ProjectFilter): Flow<Project> = client
        .execute("SELECT * FROM $table").asType<Project>().fetch().flow()

    suspend fun create(project: Project) =
        client.insert().into<Project>().table(table).using(project).await()

    @EventListener(ApplicationReadyEvent::class)
    fun init() = runBlocking {
        val amount = devConfiguration.amount
        val statusValues = ProjectStatus.values()
        val faker = Faker()
        val projects = (1..amount).map { i ->
            val status = faker.random().nextInt(0, 2)
            Project(i, faker.company().catchPhrase(), statusValues.first { it.ordinal == status })
        }
        operator.executeAndAwait {
            client.execute("CREATE TABLE IF NOT EXISTS $table (id int PRIMARY KEY, name varchar, status varchar);")
                .await()
            projects.forEach { create(it) }
        }
    }
}
