package me.gegenbauer.catspy.ui.state

import me.gegenbauer.catspy.log.GLog
import me.gegenbauer.catspy.ui.EmptyStatePanel
import me.gegenbauer.catspy.ui.button.GButton
import me.gegenbauer.catspy.utils.loadIcon
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*

class EmptyStatePanel(private val content: JComponent, private val action: () -> Unit = {}) : JPanel() {
    private val emptyImage = GButton(loadIcon("empty_state.svg", w = 60, h = 60)).apply {
        preferredSize = Dimension(120, 120)
        background = EmptyStatePanel.iconBackground
    }
    private val emptyContainer = JPanel().apply {
        layout = GridBagLayout()
        add(emptyImage, GridBagConstraints())
    }

    var contentVisible: Boolean = false
        set(value) {
            if (value == field) return
            GLog.d(TAG, "[contentVisible] set $value")
            field = value
            if (value) {
                remove(emptyContainer)
                add(content, BorderLayout.CENTER)
            } else {
                remove(content)
                add(emptyContainer, BorderLayout.CENTER)
            }
            SwingUtilities.updateComponentTreeUI(this)
        }

    init {
        layout = BorderLayout()

        emptyImage.isBorderPainted = false
        emptyImage.addActionListener { action() }

        add(emptyContainer, BorderLayout.CENTER)

        content.transferHandler?.let {
            transferHandler = object : TransferHandler() {
                override fun canImport(support: TransferSupport?): Boolean {
                    return it.canImport(support)
                }

                override fun importData(support: TransferSupport?): Boolean {
                    return it.importData(support)
                }
            }
        }
    }

    companion object {
        private const val TAG = "EmptyStatePanel"
    }
}