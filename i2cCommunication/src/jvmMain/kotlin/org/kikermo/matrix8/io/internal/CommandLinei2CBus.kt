package org.kikermo.matrix8.io.internal

import com.pi4j.exception.Pi4JException
import com.pi4j.io.i2c.I2C
import com.pi4j.io.i2c.I2CBus
import com.pi4j.io.i2c.I2CBusBase
import com.pi4j.io.i2c.I2CConfig
import java.util.concurrent.Callable

class CommandLinei2CBus(config: I2CConfig) : I2CBus, I2CBusBase(config) {
    override fun <R : Any?> execute(i2c: I2C?, action: Callable<R>?): R {
        return _execute<R>(i2c, Callable<R> {
            try {
                action!!.call()
            } catch (e: RuntimeException) {
                throw e
            } catch (e: Exception) {
                throw Pi4JException(
                    "Failed to execute action for device " + i2c?.device() + " on bus " + this.bus,
                    e
                )
            }
        })
    }
}