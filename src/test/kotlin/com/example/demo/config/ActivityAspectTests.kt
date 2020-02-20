package com.example.demo.config

import com.example.demo.service.DemoService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivityAspectTests(
    @Autowired private val demoService: DemoService
) {
    private val outContent = ByteArrayOutputStream()
    private val originalOut = System.out

    @BeforeEach fun beforeEach() {
        System.setOut(PrintStream(outContent))
    }

    @AfterEach fun afterEach() {
        System.setOut(originalOut)
        println(outContent)
        outContent.reset()
    }

    @Test fun activityShouldBeMonitored() {
        val expected = "DemoService.run(..) (SUCCESS"

        runBlocking { demoService.run(false) }

        val lines = outContent.toString().split('\n')
        assert(lines[1].contains(expected))
    }

    @Test fun `activity should be monitored when throwing`() {
        val expected = "DemoService.run(..) (ERROR"

        assertThrows<RuntimeException> {
            runBlocking { demoService.run(true) }
        }

        val lines = outContent.toString().split('\n')
        assert(lines[1].contains(expected))
    }

    @Test fun `service method should be monitored`() {
        val expected = "DemoService.noAnnotation() (SUCCESS"

        demoService.noAnnotation()

        val lines = outContent.toString().split('\n')
        assert(lines[1].contains(expected))
    }
}
