package com.dupont

import java.io.*
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class Server(
    private val serverPort: Int,
    private val timeoutMillis: Int = 10000,
    private val dataCallback: (String) -> Unit
) {
    private var socket: Socket? = null
    private var writer: Writer? = null
    private var reader: Reader? = null

    fun start() {
        println("Server connecting...")
        close()
        val newSocket = Socket()
            .apply { connect(InetSocketAddress(InetAddress.getLocalHost(), serverPort), timeoutMillis) }
            .also { socket = it }
        println("Server connected!")
        val input = newSocket.getInputStream()
        val output = newSocket.getOutputStream()
        writer = BufferedWriter(OutputStreamWriter(output))
        reader = BufferedReader(InputStreamReader(input))
            .apply { forEachLine(dataCallback) }
    }

    fun writeString(string: String) = writer?.write(string)

    private fun close() {
        writer?.close()
        writer = null
        reader?.close()
        socket?.close()
    }
}