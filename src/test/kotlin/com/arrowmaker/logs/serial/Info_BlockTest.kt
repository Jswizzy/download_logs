package com.arrowmaker.logs.serial

import com.arrowmaker.logs.serial.model.InfoBlock
import okio.Buffer
import okio.ByteString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

val info_block = ByteString.decodeHex("83170001330102fc0100000400ff770025010000000810")


class InfoBlockTest {
    @Test
    fun `Read InfoBlock`() {
        val buffer = Buffer().write(info_block)

        val infoBlock = InfoBlock.from(buffer)

        assertEquals(InfoBlock(524288, 16), infoBlock)
    }
}