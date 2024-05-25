package com.example.composeuiadaption.adapter

import org.jetbrains.kotlin.psi.KtFile

import org.jetbrains.kotlin.psi.*

class ListAdapter : KtVisitorVoid() {
    private var newFile = StringBuilder()
    private var inLazyColumn = false

    override fun visitKtFile(file: KtFile) {
        super.visitKtFile(file)
        newFile = StringBuilder(file.text).append(
            """
            var selectedItem = rememberSaveable { mutableStateOf("") }

            Column(Modifier.fillMaxSize()) {
                Row(Modifier.fillMaxSize()) {
                    // Список элементов
                    LazyColumn(Modifier.weight(1f)) {
            """.trimIndent()
        )
    }

    override fun visitDeclaration(declaration: KtDeclaration) {
        if (!inLazyColumn) {
            super.visitDeclaration(declaration)
            return
        }

        if (declaration is KtNamedFunction) {
            newFile.append("if (selectedItem.value != \"\") {\n")
            newFile.append("Column(Modifier\n")
            newFile.append("                    .fillMaxHeight()\n")
            newFile.append("                    .width(200.dp)\n")
            newFile.append("                    .background(MaterialTheme.colors.surface)) { }\n")
            newFile.append("}")
            newFile.append("}")
            newFile.append("}")
        } else {
            super.visitDeclaration(declaration)
        }
    }

    override fun visitKtElement(element: KtElement) {
        super.visitKtElement(element)
        if (element is KtCallExpression) {
            if (element.calleeExpression?.text == "LazyColumn") {
                inLazyColumn = true
            }
        }
    }

    fun convert(ktFile: KtFile): KtFile {
        visitKtFile(ktFile)
        return KtPsiFactory(ktFile.project).createFile(newFile.toString())
    }
}


