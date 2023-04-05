package me.gegenbauer.logviewer.ui.dialog

import me.gegenbauer.logviewer.utils.Utils
import me.gegenbauer.logviewer.configuration.UIConfManager
import me.gegenbauer.logviewer.log.GLog
import me.gegenbauer.logviewer.manager.ColorManager
import me.gegenbauer.logviewer.resource.strings.STRINGS
import me.gegenbauer.logviewer.ui.MainUI
import me.gegenbauer.logviewer.ui.combobox.FilterComboBox
import me.gegenbauer.logviewer.utils.getEnum
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.*
import javax.swing.*

class FilterStyleDialog(private var parent: MainUI) : JDialog(parent, "${STRINGS.ui.filterStyle} ${STRINGS.ui.setting}", true),
    ActionListener {
    enum class ComboIdx(val value: Int) {
        LOG(0),
        TAG(1),
        PID(2),
        TID(3),
        BOLD(4),
        SIZE(5);
    }

    private val exampleLabel: JLabel = JLabel("Ex : ")
    private val exampleCombo: FilterComboBox = FilterComboBox(FilterComboBox.Mode.SINGLE_LINE_HIGHLIGHT, true)

    private val comboLabelArray = arrayOfNulls<ColorLabel>(ComboIdx.SIZE.value)
    private val styleComboArray = arrayOfNulls<JComboBox<String>>(ComboIdx.SIZE.value)

    private val confirmLabel: JLabel = JLabel("To apply \"Style\" need to restart")
    private val okBtn: JButton = JButton(STRINGS.ui.ok)
    private val cancelBtn: JButton = JButton(STRINGS.ui.cancel)

    private val titleLabelArray = arrayOfNulls<ColorLabel>(ColorManager.filterStyle.size)
    private val colorLabelArray = arrayOfNulls<ColorLabel>(ColorManager.filterStyle.size)
    private val mouseHandler = MouseHandler()
    private val prevColorArray = arrayOfNulls<String>(ColorManager.filterStyle.size)
    private var isNeedRestore = true

    init {
        okBtn.addActionListener(this)
        cancelBtn.addActionListener(this)

        exampleCombo.isEditable = true
        exampleCombo.preferredSize = Dimension(250, 30)
        exampleCombo.addItem("ABC|DEF|-GHI|JKL")

        val styleLabelPanel = JPanel()
        styleLabelPanel.layout = BoxLayout(styleLabelPanel, BoxLayout.Y_AXIS)

        val styleComboPanel = JPanel()
        styleComboPanel.layout = BoxLayout(styleComboPanel, BoxLayout.Y_AXIS)

        val rightWidth = 270
        for (idx in comboLabelArray.indices) {
            comboLabelArray[idx] = ColorLabel(idx)
            comboLabelArray[idx]!!.isOpaque = true
            comboLabelArray[idx]!!.horizontalAlignment = JLabel.LEFT
            comboLabelArray[idx]!!.foreground = Color.DARK_GRAY
            comboLabelArray[idx]!!.background = Color.WHITE

            comboLabelArray[idx]!!.verticalAlignment = JLabel.CENTER
            comboLabelArray[idx]!!.border = BorderFactory.createLineBorder(Color.BLACK)
            comboLabelArray[idx]!!.minimumSize = Dimension(200, 20)
            comboLabelArray[idx]!!.preferredSize = Dimension(200, 20)
            comboLabelArray[idx]!!.maximumSize = Dimension(200, 20)

            styleComboArray[idx] = JComboBox()
            styleComboArray[idx]!!.border = BorderFactory.createLineBorder(Color.BLACK)
            styleComboArray[idx]!!.minimumSize = Dimension(rightWidth, 20)
            styleComboArray[idx]!!.preferredSize = Dimension(rightWidth, 20)
            styleComboArray[idx]!!.maximumSize = Dimension(rightWidth, 20)
            styleComboArray[idx]!!.addItem("SINGLE LINE")
            styleComboArray[idx]!!.addItem("SINGLE LINE / HIGHLIGHT")
            styleComboArray[idx]!!.addItem("MULTI LINE")
            styleComboArray[idx]!!.addItem("MULTI LINE / HIGHLIGHT")
        }

        comboLabelArray[ComboIdx.LOG.value]!!.text = "Combo Style : Log"
        styleComboArray[ComboIdx.LOG.value]!!.selectedIndex = UIConfManager.uiConf.logFilterComboStyle.ordinal
        comboLabelArray[ComboIdx.TAG.value]!!.text = "Combo Style : Tag"
        styleComboArray[ComboIdx.TAG.value]!!.selectedIndex = UIConfManager.uiConf.tagFilterComboStyle.ordinal
        comboLabelArray[ComboIdx.PID.value]!!.text = "Combo Style : PID"
        styleComboArray[ComboIdx.PID.value]!!.selectedIndex = UIConfManager.uiConf.pidFilterComboStyle.ordinal
        comboLabelArray[ComboIdx.TID.value]!!.text = "Combo Style : TID"
        styleComboArray[ComboIdx.TID.value]!!.selectedIndex = UIConfManager.uiConf.tidFilterComboStyle.ordinal
        comboLabelArray[ComboIdx.BOLD.value]!!.text = "Combo Style : BOLD"
        styleComboArray[ComboIdx.BOLD.value]!!.selectedIndex = UIConfManager.uiConf.highlightComboStyle.ordinal

        for (idx in comboLabelArray.indices) {
            styleLabelPanel.add(comboLabelArray[idx])
            styleLabelPanel.add(Box.createRigidArea(Dimension(5, 3)))
            styleComboPanel.add(styleComboArray[idx])
            styleComboPanel.add(Box.createRigidArea(Dimension(5, 3)))
        }

        val stylePanel = JPanel()
        stylePanel.layout = FlowLayout(FlowLayout.LEFT, 0, 0)
        stylePanel.add(styleLabelPanel)
        stylePanel.add(styleComboPanel)

        val colorLabelPanel = JPanel()
        colorLabelPanel.layout = BoxLayout(colorLabelPanel, BoxLayout.Y_AXIS)

        val titleLabelPanel = JPanel()
        titleLabelPanel.layout = BoxLayout(titleLabelPanel, BoxLayout.Y_AXIS)

        for (idx in colorLabelArray.indices) {
            prevColorArray[idx] = ColorManager.filterStyle[idx].strColor
            colorLabelArray[idx] = ColorLabel(idx)
            colorLabelArray[idx]!!.text =
                " ${ColorManager.filterStyle[idx].name} ${ColorManager.filterStyle[idx].strColor} "
            colorLabelArray[idx]!!.toolTipText = colorLabelArray[idx]!!.text
            colorLabelArray[idx]!!.isOpaque = true
            colorLabelArray[idx]!!.horizontalAlignment = JLabel.LEFT

            colorLabelArray[idx]!!.verticalAlignment = JLabel.CENTER
            colorLabelArray[idx]!!.border = BorderFactory.createLineBorder(Color.BLACK)
            colorLabelArray[idx]!!.minimumSize = Dimension(rightWidth, 20)
            colorLabelArray[idx]!!.preferredSize = Dimension(rightWidth, 20)
            colorLabelArray[idx]!!.maximumSize = Dimension(rightWidth, 20)
            colorLabelArray[idx]!!.addMouseListener(mouseHandler)

            titleLabelArray[idx] = ColorLabel(idx)
            titleLabelArray[idx]!!.text = " ${ColorManager.filterStyle[idx].name}"
            titleLabelArray[idx]!!.toolTipText = colorLabelArray[idx]!!.text
            titleLabelArray[idx]!!.isOpaque = true
            titleLabelArray[idx]!!.horizontalAlignment = JLabel.LEFT
            titleLabelArray[idx]!!.foreground = Color.DARK_GRAY
            titleLabelArray[idx]!!.background = Color.WHITE

            titleLabelArray[idx]!!.verticalAlignment = JLabel.CENTER
            titleLabelArray[idx]!!.border = BorderFactory.createLineBorder(Color.BLACK)
            titleLabelArray[idx]!!.minimumSize = Dimension(200, 20)
            titleLabelArray[idx]!!.preferredSize = Dimension(200, 20)
            titleLabelArray[idx]!!.maximumSize = Dimension(200, 20)
            titleLabelArray[idx]!!.addMouseListener(mouseHandler)
        }

        for (order in colorLabelArray.indices) {
            for (idx in colorLabelArray.indices) {
                if (order == ColorManager.filterStyle[idx].order) {
                    colorLabelPanel.add(colorLabelArray[idx])
                    colorLabelPanel.add(Box.createRigidArea(Dimension(5, 3)))
                    titleLabelPanel.add(titleLabelArray[idx])
                    titleLabelPanel.add(Box.createRigidArea(Dimension(5, 3)))
                    break
                }
            }
        }

        val colorPanel = JPanel()
        colorPanel.layout = FlowLayout(FlowLayout.LEFT, 0, 0)
        colorPanel.add(titleLabelPanel)
        colorPanel.add(colorLabelPanel)

        updateLabelColor()

        val sizePanel = JPanel()
        sizePanel.add(exampleLabel)
        sizePanel.add(exampleCombo)

        val topPanel = JPanel(BorderLayout())
        topPanel.add(stylePanel, BorderLayout.CENTER)
        topPanel.add(sizePanel, BorderLayout.SOUTH)

        val confirmPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        confirmPanel.preferredSize = Dimension(300, 40)
        confirmPanel.alignmentX = JPanel.RIGHT_ALIGNMENT
        confirmPanel.add(confirmLabel)
        confirmPanel.add(okBtn)
        confirmPanel.add(cancelBtn)

        val bottomPanel = JPanel(BorderLayout())
        bottomPanel.add(topPanel, BorderLayout.NORTH)
        bottomPanel.add(colorPanel, BorderLayout.CENTER)
        bottomPanel.add(confirmPanel, BorderLayout.SOUTH)
        val panel = JPanel(BorderLayout())
        panel.add(bottomPanel, BorderLayout.CENTER)

        contentPane.add(panel)

        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                GLog.d(TAG, "exit Filter Style dialog, restore $isNeedRestore")

                if (isNeedRestore) {
                    for (idx in colorLabelArray.indices) {
                        ColorManager.filterStyle[idx].strColor = prevColorArray[idx]!!
                    }
                    ColorManager.applyFilterStyle()
                } else {
                    UIConfManager.uiConf.logFilterComboStyle = getEnum(styleComboArray[ComboIdx.LOG.value]!!.selectedIndex)
                    UIConfManager.uiConf.tagFilterComboStyle = getEnum(styleComboArray[ComboIdx.TAG.value]!!.selectedIndex)
                    UIConfManager.uiConf.pidFilterComboStyle = getEnum(styleComboArray[ComboIdx.PID.value]!!.selectedIndex)
                    UIConfManager.uiConf.tidFilterComboStyle = getEnum(styleComboArray[ComboIdx.TID.value]!!.selectedIndex)
                    UIConfManager.uiConf.highlightComboStyle = getEnum(styleComboArray[ComboIdx.BOLD.value]!!.selectedIndex)
                    UIConfManager.saveUI()
                }
            }
        })

        pack()
        Utils.installKeyStrokeEscClosing(this)
    }

    fun updateLabelColor() {
        val commonFg = Color.BLACK

        for (idx in colorLabelArray.indices) {
            colorLabelArray[idx]!!.foreground = commonFg
            colorLabelArray[idx]!!.background = Color.decode(ColorManager.filterStyle[idx].strColor)
        }
    }

    class ColorLabel(val idx: Int) : JLabel()

    override fun actionPerformed(e: ActionEvent) {
        if (e.source == okBtn) {
            isNeedRestore = false
            this.dispatchEvent(WindowEvent(this, WindowEvent.WINDOW_CLOSING))
        } else if (e.source == cancelBtn) {
            this.dispatchEvent(WindowEvent(this, WindowEvent.WINDOW_CLOSING))
        }
    }

    internal inner class MouseHandler : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            val colorChooser = JColorChooser()
            val panels = colorChooser.chooserPanels
            var rgbPanel: JPanel? = null
            for (panel in panels) {
                if (panel.displayName.contains("RGB", true)) {
                    rgbPanel = panel
                }
            }

            if (rgbPanel != null) {
                val tmpColorLabel = e!!.source as ColorLabel
                val idx = tmpColorLabel.idx
                val colorLabel = colorLabelArray[idx]!!
                colorChooser.color = colorLabel.background

                val ret = JOptionPane.showConfirmDialog(
                    this@FilterStyleDialog,
                    rgbPanel,
                    "Color Chooser",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                )
                if (ret == JOptionPane.OK_OPTION) {
                    val hex = "#" + Integer.toHexString(colorChooser.color.rgb).substring(2).uppercase()
                    colorLabel.text = " ${ColorManager.filterStyle[idx].name} $hex "
                    ColorManager.filterStyle[idx].strColor = hex
                    colorLabel.background = colorChooser.color
                    ColorManager.applyFilterStyle()
                    updateLabelColor()
                    val selectedItem = exampleCombo.selectedItem
                    exampleCombo.selectedItem = ""
                    exampleCombo.selectedItem = selectedItem
                }
            }

            super.mouseClicked(e)
        }
    }

    companion object {
        private const val TAG = "FilterStyleDialog"
    }
}
