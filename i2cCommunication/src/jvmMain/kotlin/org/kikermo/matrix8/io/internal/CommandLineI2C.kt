package org.kikermo.matrix8.io.internal

import com.pi4j.io.i2c.I2C
import com.pi4j.io.i2c.I2CBase
import com.pi4j.io.i2c.I2CConfig

class CommandLineI2C constructor(
    provider: CommandLineI2CProvider,
    config: I2CConfig
) : I2C, I2CBase<CommandLinei2CBus>(provider, config, CommandLinei2CBus(config)) {

    override fun write(b: Byte): Int {
        println("Not Implemented")
        return 0
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun write(data: ByteArray?, offset: Int, length: Int): Int {
        println("Executing i2c command")
        val hexData = data?.joinToString(separator = " ") { "0x${it.toHexString()}" }
        val command =
            "i2cset -y ${config.bus} 0x${config.device.toHexString().takeLast(2)} $hexData"
        println(command)
        val p = Runtime.getRuntime()
            .exec(command, null, null)
        return p.waitFor()
    }

    override fun read(): Int {
        println("Not Implemented")
        return 0
    }

    override fun read(buffer: ByteArray?, offset: Int, length: Int): Int {
        println("Not Implemented")
        return 0
    }

    override fun readRegister(register: Int): Int {
        println("Not Implemented")
        return 0
    }

    override fun readRegister(
        register: ByteArray?,
        buffer: ByteArray?,
        offset: Int,
        length: Int
    ): Int {
        println("Not Implemented")
        return 0
    }

    override fun readRegister(register: Int, buffer: ByteArray?, offset: Int, length: Int): Int {
        println("Not Implemented")
        return 0
    }

    override fun writeRegister(register: Int, b: Byte): Int {
        println("Not Implemented")
        return 0
    }

    override fun writeRegister(register: Int, data: ByteArray?, offset: Int, length: Int): Int {
        println("Not Implemented")
        return 0
    }

    override fun writeRegister(
        register: ByteArray?,
        data: ByteArray?,
        offset: Int,
        length: Int
    ): Int {
        println("Not Implemented")
        return 0
    }
}