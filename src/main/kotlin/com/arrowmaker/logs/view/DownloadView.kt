package com.arrowmaker.logs.view

import com.arrowmaker.logs.app.Styles.Companion.exit
import com.arrowmaker.logs.app.Styles.Companion.download
import com.arrowmaker.logs.controller.DownloadController
import com.arrowmaker.logs.model.FormatModel
import com.arrowmaker.logs.model.SerialPortModel
import com.jfoenix.controls.JFXSnackbar
import javafx.application.Platform
import javafx.geometry.Orientation
import javafx.scene.control.TextArea
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import kfoenix.jfxbutton
import kfoenix.jfxcheckbox
import kfoenix.jfxcombobox
import tornadofx.*


class DownloadView : View("MATS") {
    private val controller: DownloadController by inject()
    private val model: SerialPortModel by inject()
    private val formatModel: FormatModel by inject()

    private var logs: TextArea by singleAssign()
    private val status: TaskStatus by inject()

    //offsets for moving view around
    private var xOffset = 0.0
    private var yOffset = 0.0

    override val root = vbox {
        toolbar {
            label("MATS Log Download Manager") {
                textFill = Color.WHITE
            }
            hbox { hgrow = Priority.ALWAYS }
            jfxbutton("EXIT") {
                addClass(exit)
                action { Platform.exit() }
            }

            setOnMousePressed { event ->
                xOffset = event.sceneX
                yOffset = event.sceneY
            }

            setOnMouseDragged { event ->
                currentStage?.x = event.screenX - xOffset
                currentStage?.y = event.screenY - yOffset
            }
        }
        form {
            imageview("WinTec.png", lazyload = false)
            fieldset(labelPosition = Orientation.HORIZONTAL) {
                field("Select Port:") {
                    jfxcombobox<String>(model.port, controller.ports) {
                        selectionModel.selectFirst()
                        setOnShowing { controller.updatePorts() }
                        setOnAction {
                            val port = model.port.value
                            if (port != null) {
                                toast("Port: $port selected")
                            }
                        }
                        required()
                        whenDocked { requestFocus() }
                    }
                }
                squeezebox {
                    fold("Advanced Serial Port Options") {
                        form {
                            fieldset {
                                field("BaudRate:") {
                                    jfxcombobox(model.baudRate, controller.baudRates) {
                                        selectionModel.selectLast()
                                        setOnAction {
                                            toast("Baudrate: ${model.baudRate.value} Selected")
                                        }
                                        required()
                                    }
                                }
                                field("DataBits:") {
                                    jfxcombobox(model.dataBits, controller.dataBits) {
                                        selectionModel.selectFirst()
                                        setOnAction { toast("DataBits: ${model.dataBits.value} Selected") }
                                        required()
                                    }
                                }
                                field("StopBits:") {
                                    jfxcombobox(model.stopBits, controller.stopBits) {
                                        selectionModel.selectFirst()

                                        setOnAction { toast("StopBits: ${model.stopBits.value} Selected") }
                                        required()
                                    }
                                }
                                field("Parity:") {
                                    jfxcombobox(model.parity, controller.parities) {
                                        selectionModel.selectFirst()
                                        setOnAction { toast("Parity: ${model.parity.value} Selected") }
                                        required()
                                    }
                                }
                            }
                        }
                    }
                }
                add<ProgressView>()
                field("Outputs") {
                    jfxcheckbox(formatModel.isKMLSelected, "KML")
                    jfxcheckbox(formatModel.isCSVSelected, "CSV")
                    jfxcheckbox(formatModel.isGPXSelected, "GPX")
                    jfxcheckbox(formatModel.isRawSelected, "Hex")
                }
                jfxbutton("Download") {
                    useMaxWidth = true
                    addClass(download)
                    enableWhen {
                        model.valid
                                .and(!status.running)
                                .and(formatModel.isCSVSelected.toBinding()
                                        .or(formatModel.isKMLSelected.toBinding()
                                                .or(formatModel.isGPXSelected.toBinding()
                                                        .or(formatModel.isRawSelected.toBinding()))))
                    }
                    action {
                        model.commit { download() }
                    }
                }
            }
        }
        logs = textarea(model.logs) {
            prefRowCount = 5
            isEditable = false
            wrapTextProperty().value = true
            text = "To Download logs:\n1) Connect Tracker to USB\n2) Select log outputs\n3) Press download\n4) Power on Tracker."
        }
    }

    private fun download() {
        val fileChooser = FileChooser()

        //set extension filters
        val fileExt = FileChooser.ExtensionFilter("Any File", "*")
        fileChooser.extensionFilters.add(fileExt)

        val file = fileChooser.showSaveDialog(currentStage)

        if (file != null) {
            val listening = "Listening for Data, Plug in Tracker and power it on"
            toast(listening)
            log(listening)

            runAsync(true) {
                updateTitle("Waiting for Tracker to connect")
                updateMessage("Downloading Logs")

                with(model) {
                    val pages = controller.download(port.value, baudRate.value, dataBits.value, stopBits.value, parity.value,
                            if (formatModel.isRawSelected.value) {
                                file
                            } else {
                                null
                            }) { i: Int, total: Int ->
                        if (i == 0) log("Downloading Data from Tracker")
                        updateProgress(i.toLong(), total.toLong())
                        updateTitle("Downloading Page $i of $total")
                    }

                    status?.completed?.onChange {
                        updateTitle("Done Downloading logs")
                        log("Done Downloading")
                        if (formatModel.isKMLSelected.value) {
                            controller.kml(file, pages)
                            log("Saved KML to $file.kml")
                        }
                        if (formatModel.isCSVSelected.value) {
                            controller.csv(file, pages)
                            log("Saved CSV to $file.csv")
                        }
                        if (formatModel.isGPXSelected.value) {
                            controller.gpx(file, pages)
                            log("Saved GPX to $file.gpx")
                        }
                        updateTitle("Download Complete")
                    }
                }
            }
        }
    }


    class ProgressView : View() {
        private val status: TaskStatus by inject()

        override val root = vbox(4) {
            visibleWhen { status.running }
            style { borderColor += box(Color.LIGHTGREY, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT) }
            label(status.title).style { fontWeight = FontWeight.BOLD }
            hbox(4) {
                label(status.message)
                progressbar(status.progress)
                visibleWhen { status.running }
            }
        }
    }

    private fun toast(msg: String) {
        snackbar.show(msg, 2_000)
    }

    private fun log(string: String) {
        logs.appendText("\n$string")
    }

    private val snackbar: JFXSnackbar = JFXSnackbar(root)
}