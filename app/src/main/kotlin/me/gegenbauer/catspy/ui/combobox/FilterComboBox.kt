package me.gegenbauer.catspy.ui.combobox

import com.github.weisj.darklaf.settings.ThemeSettings
import com.github.weisj.darklaf.theme.Theme
import me.gegenbauer.catspy.databinding.bind.Bindings
import me.gegenbauer.catspy.databinding.bind.componentName
import me.gegenbauer.catspy.databinding.bind.withName
import me.gegenbauer.catspy.ui.combobox.highlight.CustomEditorDarkComboBoxUI
import me.gegenbauer.catspy.ui.combobox.highlight.HighlighterEditor
import me.gegenbauer.catspy.data.model.log.FilterItem
import me.gegenbauer.catspy.data.model.log.LogcatRealTimeFilter.Companion.toFilterItem
import me.gegenbauer.catspy.utils.DefaultDocumentListener
import me.gegenbauer.catspy.utils.applyTooltip
import java.awt.event.MouseEvent
import javax.swing.ComboBoxEditor
import javax.swing.JComponent
import javax.swing.ToolTipManager
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.plaf.ComboBoxUI
import javax.swing.plaf.basic.BasicComboBoxEditor
import javax.swing.text.JTextComponent

class FilterComboBox(private val enableHighlight: Boolean = true, private val tooltip: String? = null) :
    HistoryComboBox<String>() {

    var filterItem: FilterItem = FilterItem.emptyItem
    private val editorComponent: JTextComponent // create new instance when theme changed(setUI invoked)
        get() = getEditor().editorComponent as JTextComponent
    private var customEditor: ComboBoxEditor = BasicComboBoxEditor()
        set(value) {
            field = value
            setEditor(value)
        }

    var enabledTfTooltip = false
        set(value) {
            field = value
            if (value) {
                updateTooltip()
            }
        }
    private var errorMsg: String = ""
        set(value) {
            field = value
            updateTooltip(value.isNotEmpty())
        }
    private val currentContentChangeListener = object : DefaultDocumentListener() {
        override fun contentUpdate(content: String) {
            filterItem = content.toFilterItem()
            errorMsg = filterItem.errorMessage
        }
    }

    init {
        toolTipText = tooltip
        configureEditorComponent(editorComponent)
        updateUI()
    }

    override fun setUI(ui: ComboBoxUI?) {
        customEditor = if (enableHighlight) {
            HighlighterEditor()
        } else {
            BasicComboBoxEditor()
        }
        super.setUI(CustomEditorDarkComboBoxUI(customEditor.apply {
            Bindings.rebind(customEditor.editorComponent as JComponent, this.editorComponent as JComponent)
            configureEditorComponent(customEditor.editorComponent as JTextComponent)
        }))
    }

    private fun configureEditorComponent(editorComponent: JTextComponent) {
        editorComponent applyTooltip tooltip
        editorComponent withName this@FilterComboBox.componentName
        editorComponent.document.addDocumentListener(currentContentChangeListener)
    }

    override fun getEditor(): ComboBoxEditor {
        return customEditor
    }

    fun addTooltipUpdateListener() {
        editorComponent.document.addDocumentListener(DocumentHandler())
    }

    fun updateTooltip() {
        updateTooltip(false)
    }

    private fun updateTooltip(isShow: Boolean) {
        if (!enabledTfTooltip) {
            return
        }

        if (errorMsg.isNotEmpty()) {
            var tooltip = "<html><b>"
            tooltip += if (Theme.isDark(ThemeSettings.getInstance().theme)) {
                "<font size=5 color=#C07070>$errorMsg</font>"
            } else {
                "<font size=5 color=#FF0000>$errorMsg</font>"
            }
            tooltip += "</b></html>"
            editorComponent.toolTipText = tooltip
        } else {
            val includeStr = updateToolTipStrToHtml(filterItem.positiveFilter.pattern())
            val excludeStr = updateToolTipStrToHtml(filterItem.negativeFilter.pattern())

            var tooltip = "<html><b>$toolTipText</b><br>"
            if (Theme.isDark(ThemeSettings.getInstance().theme)) {
                tooltip += "<font>INCLUDE : </font>\"<font size=5 color=#7070C0>$includeStr</font>\"<br>"
                tooltip += "<font>EXCLUDE : </font>\"<font size=5 color=#C07070>$excludeStr</font>\"<br>"
            } else {
                tooltip += "<font>INCLUDE : </font>\"<font size=5 color=#0000FF>$includeStr</font>\"<br>"
                tooltip += "<font>EXCLUDE : </font>\"<font size=5 color=#FF0000>$excludeStr</font>\"<br>"
            }
            tooltip += "</html>"
            editorComponent.toolTipText = tooltip
        }

        if (isShow) {
            ToolTipManager.sharedInstance().mouseMoved(MouseEvent(editorComponent, 0, 0, 0, 0, 0, 0, false))
        }
    }

    private inner class DocumentHandler : DocumentListener {
        override fun insertUpdate(e: DocumentEvent) {
            if (enabledTfTooltip && !isPopupVisible) {
                updateTooltip()
            }
        }

        override fun removeUpdate(e: DocumentEvent) {
            if (enabledTfTooltip && !isPopupVisible) {
                updateTooltip()
            }
        }

        override fun changedUpdate(e: DocumentEvent) {
            // do nothing
        }

    }

    fun getAllItems(): List<HistoryItem<String>> {
        return mutableListOf<HistoryItem<String>>().apply {
            for (i in 0 until itemCount) {
                add(getItemAt(i))
            }
        }
    }

    override fun addItem(item: HistoryItem<String>?) {
        if (item == null || item.content.isEmpty()) return
        super.addItem(item)
    }

    fun addItem(item: String?) {
        if (item.isNullOrEmpty()) return
        super.addItem(HistoryItem(item))
    }

    companion object {

        private fun updateToolTipStrToHtml(toolTipStr: String): String {
            if (toolTipStr.isEmpty()) return toolTipStr
            return toolTipStr.replace("&#09", "&amp;#09")
                .replace("\t", "&#09;")
                .replace("&nbsp", "&amp;nbsp")
                .replace(" ", "&nbsp;")
                .replace("|", "<font color=#303030><b>|</b></font>")
        }
    }
}

fun filterComboBox(enableHighlight: Boolean = true, tooltip: String? = null): FilterComboBox {
    val comboBox = FilterComboBox(enableHighlight, tooltip)
    comboBox.isEditable = true
    comboBox.addTooltipUpdateListener()
    return comboBox
}

fun darkComboBox(tooltip: String? = null): FilterComboBox {
    return filterComboBox(enableHighlight = false, tooltip)
}
