package com.example.composeuiadaption.action

import com.intellij.openapi.ui.DialogWrapper
import java.awt.GridLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class ScreenSizeDialog : DialogWrapper(true) {
    private val widthField = JTextField()
    private val heightField = JTextField()
    private val inchesField = JTextField()

    init {
        init()
        title = "Enter Screen Size"
    }

    override fun createCenterPanel(): JComponent {
        val dialogPanel = JPanel(GridLayout(3, 2))
        dialogPanel.add(JLabel("Width:"))
        dialogPanel.add(widthField)
        dialogPanel.add(JLabel("Height:"))
        dialogPanel.add(heightField)
        dialogPanel.add(JLabel("Inches:"))
        dialogPanel.add(inchesField)
        return dialogPanel
    }

    fun getScreenDimensions(): Triple<Int, Int, Double>? {
        val width = widthField.text.toIntOrNull()
        val height = heightField.text.toIntOrNull()
        val inches = inchesField.text.toDoubleOrNull()
        return if (width != null && height != null && inches != null) Triple(
            width,
            height,
            inches
        ) else null
    }
}