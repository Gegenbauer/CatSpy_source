package me.gegenbauer.logviewer.ui.container

import javax.swing.JPanel

class WrapablePanel : JPanel() {
    init {
        layout = WrapableLayout(HORIZONTAL_GAP, VERTICAL_GAP)
    }

    companion object {
        private const val HORIZONTAL_GAP = 3
        private const val VERTICAL_GAP = 3
    }
}