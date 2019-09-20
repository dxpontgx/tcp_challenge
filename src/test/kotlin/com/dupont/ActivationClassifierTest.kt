package com.dupont

import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assume
import org.junit.Before
import org.junit.Test
import java.lang.Exception


enum class Label {
    ACTIVATION, REST
}

class FullReading(val label: Label, timestamp: Long, data: Int) : Reading(data, timestamp)

private const val TEST_FILE_NAME = "test_data.txt"

class ActivationClassifierTest {

    @Before
    fun dataTest() {
        try {
            readFile(TEST_FILE_NAME)
        } catch (e: Exception) {
            Assume.assumeNoException(
                "Test data file: $TEST_FILE_NAME has been removed for privacy reasons." +
                        " Please reach out to get it, and place it in src/test/resources", e
            )
        }
    }

    @Test
    fun `end to end test`() {
        val deserializer = Moshi.Builder().build().adapter(FullReading::class.java)
        val testData = readFile(TEST_FILE_NAME)
            .lineSequence()
            .map { deserializer.fromJson(it) }
            .filterNotNull()
            .toList()

        var classifiedActivationCount = 0
        val testClassifier = ActivationClassifier { classifiedActivationCount++ }
        val activationCount = testData.filter { it.label == Label.ACTIVATION }.size

        testData.forEach(testClassifier::onDataRead)
        assertEquals(6, activationCount / classifiedActivationCount)
    }
}

