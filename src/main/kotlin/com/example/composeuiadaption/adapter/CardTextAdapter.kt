package com.example.composeuiadaption.adapter

import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.*

class CardTextAdapter : KtVisitorVoid() {
    private var newFileContent = ""
    private var isInsideModifier = false
    private var isInsideColumn = false
    private var isFinished = false

    fun convert(file: KtFile): KtFile {
        visitFile(file)
        return KtPsiFactory(file.project).createFile(newFileContent)
    }

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)
        if (isFinished) return
        val propertyName = property.name ?: return
        if (propertyName == "modifier") {
            isInsideModifier = true
        }
    }

    override fun visitBlockExpression(expression: KtBlockExpression) {
        super.visitBlockExpression(expression)
        if (isFinished) return
        if (!isInsideColumn) {
            newFileContent += "Column(\n" +
                    "    modifier = Modifier.fillMaxSize(),\n" +
                    "    verticalArrangement = Arrangement.Center\n" +
                    ") {\n"
            isInsideColumn = true
        }
    }

    override fun visitKtElement(element: KtElement) {
        super.visitKtElement(element)

        if (isFinished) return

        if (isInsideModifier && element is KtBinaryExpression) {
            val lhs = element.left ?: return
            val lhsText = lhs.text as String
            if (lhsText == "Modifier") {
                val rhs = element.right ?: return
                val rhsText = rhs.text
                if (!rhsText.contains("fillMaxHeight")) {
                    val newRhsText = rhsText.replace("){", "){ .fillMaxHeight()\n")
                    newFileContent += lhsText + " " + newRhsText
                } else {
                    newFileContent += lhsText + " " + rhsText
                }
                isInsideModifier = false
            }
        } else {
            val elementText = element.text
            newFileContent += elementText
        }
    }
}

