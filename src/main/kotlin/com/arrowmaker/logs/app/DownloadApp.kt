package com.arrowmaker.logs.app

import com.arrowmaker.logs.view.DownloadView
import javafx.scene.image.Image
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*

class DownloadApp: App(DownloadView::class, Styles::class) {
    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.UNDECORATED)
        addStageIcon(Image("WinTec.png"))
        super.start(stage)
    }
}