package me.gegenbauer.logviewer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.gegenbauer.logviewer.concurrency.AppScope
import me.gegenbauer.logviewer.concurrency.UI
import me.gegenbauer.logviewer.databinding.Bindings
import me.gegenbauer.logviewer.databinding.ObservableViewModelProperty
import me.gegenbauer.logviewer.databinding.adapter.listProperty
import me.gegenbauer.logviewer.databinding.adapter.selectedIndexProperty
import me.gegenbauer.logviewer.log.GLog
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JFrame
import javax.swing.JPanel

fun main() {
    AppScope.launch(Dispatchers.UI) {
        val frame = JFrame()
        GLog.DEBUG = true
        frame.size = Dimension(500, 500)
        val panel = JPanel()
        val bt = JButton("C")
        panel.add(bt)
        val cb = JComboBox<String>()
        cb.preferredSize = Dimension(200, 30)
        panel.add(cb)
        val vm = ComboBoxViewModel()
        bt.addActionListener {
            vm.items.updateValue(listOf("A", "B", "C"))
        }
        Bindings.bind(listProperty(cb), vm.items)
        Bindings.bind(selectedIndexProperty(cb), vm.selectedIndex)
        vm.selectedIndex.addObserver {
            println("Selected index: $it")
        }
        frame.add(panel)
        frame.isVisible = true
    }
}

class ComboBoxViewModel {
    val items = ObservableViewModelProperty<List<String>>()
    val selectedIndex = ObservableViewModelProperty<Int>()
}