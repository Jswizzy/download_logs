package com.arrowmaker.logs.serial

import com.arrowmaker.logs.serial.model.LRC
import com.arrowmaker.logs.serial.model.Page
import okio.Buffer
import okio.ByteString
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LRCTest {
    @Test
    fun `LRC for Record`() {
        //given
        val msg = "ba000001000bc18f3333aaaaaaaaaaaaaaaaaa3fdbaf484434a0452c1218c600a12bc19400003fdaf448446600452c1217c600a128c1c0cccc3fda8248446951452c121bc600a12ac1c599993fdaf148446a31452c121bc600a12ac1c733333fda4848446b31452c121bc600a12ac1c333333fda0c48446c00452c121bc600a12ac1c000003fda2448446cd1452c1219c600a12ac1b800003fd9f748446d80452c1219c600a12ac1b4cccd3fd9b848446e40452c1218c600a12ac1b0cccd3fd93348446ef1452c1219c600a12ac1b000003fd90448447f40452c121bc600a129c19e66663fd8d348447fd1452c1219c600a12ac192666689999923ffff4821bc1218c63355"
        val checksum = Page.from(Buffer().write(ByteString.decodeHex(msg))).lrc
        val record = msg.slice(2..517)

        //when
        val lrc = LRC.calculate(ByteString.decodeHex(record).toByteArray())

        println("lrc: $lrc checksum: $checksum")
        //then
        assertTrue(lrc == checksum)
    }

    @Test
    fun `LRC for Record with bad checksum`() {
        //given
        val msg = "ba0200c600a12ac19800001fc600a12dc16e6666aaaa4812cda0452c11fdc600a12ac1cf33332300ff4812cdc0452c11fdc600a12ac1ce66662300ff4812cde0452c11fdc600a12ac1cccccc2300ff4812ce00452c11fdc600a12ac1cccccc2300ff4812ce20452c11fdc600a12ac1cccccc2300ff4812ce40452c11fdc600a12ac1cd99992300ff4812ce60452c11fdc600a12ac1cd99992300ff4812ce80452c11fdc600a12ac1cd99992300ff4812cea0452c11fdc600a12ac1cc00002300ff4812cec0452c11fec600a12ac1c999992300ff4812cee0452c11fec600a12ac1c733332300ff4812cf00452c11fec600a12ac1c666662300ffaaaaaaaaaaaa2c1218fe55"

        val checksum = Page.from(Buffer().write(ByteString.decodeHex(msg))).lrc
        val record = msg.slice(2..517)

        //when
        val lrc = LRC.calculate(ByteString.decodeHex(record).toByteArray())

        println("lrc: $lrc checksum: $checksum")
        //then
        assertFalse(lrc == checksum)
    }
}