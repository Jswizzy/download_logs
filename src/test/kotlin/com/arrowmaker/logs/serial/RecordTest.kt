package com.arrowmaker.logs.serial

import com.arrowmaker.logs.serial.model.Record
import okio.Buffer
import okio.ByteString
import org.junit.jupiter.api.Test

val record: ByteString = ByteString.decodeHex("FF000102030405060708090A0B0C0D0E0F1011")

class RecordTest {
    @Test
    fun `Record from buffer`() {
        val buffer = Buffer().write(record)

        val record = Record.from(buffer)

        println(record)
    }
}