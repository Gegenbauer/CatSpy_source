package me.gegenbauer.logviewer.ui

import java.awt.BorderLayout
import javax.swing.*

fun addHSeparator(target: JPanel, title: String) {
    val titleHtml = title.replace(" ", "&nbsp;")
    val separator = JSeparator(SwingConstants.HORIZONTAL)
    val label = JLabel("<html><b>$titleHtml</b></html>")
    val panel = JPanel(BorderLayout())
    val separatePanel = JPanel(BorderLayout())
    separatePanel.add(Box.createVerticalStrut(label.font.size / 2), BorderLayout.NORTH)
    separatePanel.add(separator, BorderLayout.CENTER)
    panel.add(label, BorderLayout.WEST)
    panel.add(separatePanel, BorderLayout.CENTER)
    target.add(panel)
}