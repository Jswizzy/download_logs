package com.arrowmaker.logs.model

import javafx.beans.property.SimpleBooleanProperty
import tornadofx.*

class Format {
    val isCSVProperty = SimpleBooleanProperty(true)
    val isKMLProperty = SimpleBooleanProperty(false)
    val isGPXProperty = SimpleBooleanProperty(false)
    val isRawProperty = SimpleBooleanProperty(false)
}

class FormatModel : ItemViewModel<Format>() {
    val isKMLSelected = bind(Format::isKMLProperty)
    val isCSVSelected = bind(Format::isCSVProperty)
    val isGPXSelected = bind(Format::isGPXProperty)
    val isRawSelected = bind(Format::isRawProperty)
}