package org.kikermo.matrix8.io.internal

import com.pi4j.common.Metadata
import com.pi4j.context.Context
import com.pi4j.context.ContextBuilder
import com.pi4j.io.i2c.I2C
import com.pi4j.io.i2c.I2CConfig
import com.pi4j.io.i2c.I2CProvider

class CommandLineI2CProvider : I2CProvider {
    override fun id() = "cmd-i2c"

    override fun name() = "Command Line I2C provider"

    override fun description() = "Command line i2c provider"

    override fun metadata() = Metadata.create()

    override fun initialize(context: Context?): I2CProvider {
        return this
    }

    override fun shutdown(context: Context?): I2CProvider {
        return this
    }

    override fun context(): Context {
        return ContextBuilder.newInstance().build()
    }

    override fun create(config: I2CConfig): I2C {
        return CommandLineI2C(this, config)
    }
}