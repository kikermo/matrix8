package org.kikermo.matrix8.io

import java.io.BufferedReader
import java.io.InputStreamReader

class CommandExample {

    fun enableWifiDirect() {
        executeCommand("./enableWifiDirect.sh")
    }

    fun disableWifiDirect() {
        executeCommand("./disableWifiDirect.sh")
    }

    private fun executeCommand(command: String) {
        val processBuilder = ProcessBuilder()
        processBuilder.command("bash", "-c", command).start()
    }

}