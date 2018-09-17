package com.arrowmaker.logs.model

import com.arrowmaker.logs.serial.configuration.Parity
import com.arrowmaker.logs.serial.configuration.StopBit
import javafx.beans.property.Property
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import tornadofx.*

class SerialPort {
    private var port: String by property<String>()
    fun portProperty() = getProperty(SerialPort::port)

    private var baudRate: Int by property<Int>()
    fun baudRateProperty() = getProperty(SerialPort::baudRate)

    private var parity: Parity by property<Parity>()
    fun parityProperty() = getProperty(SerialPort::parity)

    private var dataBits: Int by property<Int>()
    fun dataBitsProperty() = getProperty(SerialPort::dataBits)

    private var stopBits: StopBit by property<StopBit>()
    fun stopBitsProperty() = getProperty(SerialPort::stopBits)
}

class SerialPortModel : ItemViewModel<SerialPort>(SerialPort()) {
    val port: StringProperty = bind { item?.portProperty() }
    val baudRate: Property<Int> = bind { item?.baudRateProperty() }
    val parity: Property<Parity> = bind { item?.parityProperty() }
    val dataBits: Property<Int> = bind { item?.dataBitsProperty() }
    val stopBits: Property<StopBit> = bind { item?.stopBitsProperty() }
    val logs = bind { SimpleStringProperty() }
}