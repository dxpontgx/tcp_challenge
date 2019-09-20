package com.dupont

import com.squareup.moshi.Moshi

private const val ACTIVATION_MESSAGE = "Activation classified\n"
private val readingParser = Moshi.Builder().build().adapter(Reading::class.java)
lateinit var server: Server
lateinit var classifier: ActivationClassifier

fun main() {
    println("What port is your server running on?")
    val port = readLine()?.toInt() ?: error("Error: invalid port.")
    classifier = ActivationClassifier { server.writeString(ACTIVATION_MESSAGE) }
    server = Server(port) { readString ->
        readingParser.fromJson(readString)
            ?.let(classifier::onDataRead)
            ?: error("Invalid JSON read: $readString")
    }
    server.start()
    print("Server closed! Goodbye.")
}