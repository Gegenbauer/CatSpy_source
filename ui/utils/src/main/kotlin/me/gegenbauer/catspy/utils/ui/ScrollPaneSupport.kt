package me.gegenbauer.catspy.utils.ui

import java.awt.Scrollbar
import java.awt.event.MouseWheelEvent
import javax.swing.JScrollBar
import javax.swing.JScrollPane

interface ScrollToEndListenerSupport {
    fun addOnScrollToEndListener(listener: OnScrollToEndListener)

    fun removeOnScrollToEndListener(listener: OnScrollToEndListener) {
        // default no-op
    }
}

fun interface OnScrollToEndListener {

    fun onScrollToEnd(event: MouseWheelEvent)
}

fun JScrollPane.addOnScrollToEndListener(listener: OnScrollToEndListener) {
    addMouseWheelListener {
        val source = it.source
        source as JScrollPane
        if (!source.horizontalScrollBar.isVisible && !source.verticalScrollBar.isVisible) {
            listener.onScrollToEnd(it)
            return@addMouseWheelListener
        }
        val isScrollHorizontally = source.horizontalScrollBar.isVisible
        val isScrollDown = it.wheelRotation > 0
        val scrollBar = if (isScrollHorizontally) source.horizontalScrollBar else source.verticalScrollBar
        if (isScrollBarAtEnd(scrollBar, isScrollDown)) {
            listener.onScrollToEnd(it)
        }
    }
}

class ScrollEventDelegator(private val scrollPane: JScrollPane) : OnScrollToEndListener {
    override fun onScrollToEnd(event: MouseWheelEvent) {
        val newEvent = MouseWheelEvent(
            scrollPane,
            event.id,
            event.`when`,
            event.modifiers,
            event.x,
            event.y,
            event.clickCount,
            event.isPopupTrigger,
            event.scrollType,
            event.scrollAmount,
            event.wheelRotation
        )
        scrollPane.dispatchEvent(newEvent)
    }
}

private fun isScrollBarAtEnd(scrollBar: JScrollBar, downOrUp: Boolean): Boolean {
    return if (downOrUp) {
        scrollBar.value + scrollBar.visibleAmount >= scrollBar.maximum
    } else {
        scrollBar.value == 0
    }
}
