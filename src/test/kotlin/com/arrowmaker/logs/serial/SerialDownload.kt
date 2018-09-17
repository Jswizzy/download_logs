package com.arrowmaker.logs.serial

import com.arrowmaker.logs.serial.service.SerialPortService

fun main(args: Array<String>) {
    val service = SerialPortService.getSocket("COM9") { current, total-> println("Page $current of $total")}

    service.call()
}