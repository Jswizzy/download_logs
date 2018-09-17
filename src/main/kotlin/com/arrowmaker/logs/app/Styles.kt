package com.arrowmaker.logs.app

import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles: Stylesheet() {
    companion object {
        // Define css classes
        val download by cssclass()
        val exit by cssclass()

        val primary = c("#283493")
        val onPrimary = c("#FFFFFF")
        val altPrimary = c("286493")
    }

    init {
        root {
            backgroundColor += Color.GHOSTWHITE
        }
        toolBar {
            prefHeight = 30.px
            backgroundColor += primary
        }
        comboBox {
            prefWidth = 100.px
        }
        label {
            fontWeight = FontWeight.BOLD
            fontSize = 14.px
        }
        exit {
            textFill = onPrimary
        }
        download {
            backgroundColor += altPrimary
            textFill = onPrimary
            fontWeight = FontWeight.BOLD
            fontSize = 14.px
        }
        s(".jfx-snackbar-content") {

            backgroundColor += altPrimary
        }
        s(".jfx-snackbar-toast") {
            textFill = Color.WHITE
            fontSize = 15.px
        }
        form {
            alignment = Pos.CENTER
        }
        textArea {
            backgroundColor += Color.WHITESMOKE
        }
    }
}