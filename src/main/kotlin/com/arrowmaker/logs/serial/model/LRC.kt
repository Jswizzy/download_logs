package com.arrowmaker.logs.serial.model

object LRC {
    fun calculate(byteArray: ByteArray) = byteArray.fold(0) { lrc, n ->
        lrc xor n.toInt()
    }
}