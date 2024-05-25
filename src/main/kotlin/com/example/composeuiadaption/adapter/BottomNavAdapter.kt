package com.example.composeuiadaption.adapter

import org.jetbrains.kotlin.psi.*

class BottomNavAdapter : KtVisitorVoid() {

    private var newKtFile: KtFile? = null
    private var alignFound = false
    private var modifierFound = false

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)

        if (property.name == "bottomNav") {
            property.children.forEach { propertyChild ->
                if (propertyChild is KtObjectLiteralExpression) {
                    propertyChild.children.forEach { objectLiteralChild ->
                        if (objectLiteralChild is KtCallExpression && objectLiteralChild.calleeExpression?.text == ".align") {
                            alignFound = true
                            newKtFile?.add(KtPsiFactory(property).createExpression(
                                ".align(Alignment.CenterVertically).offset(x = (-screenWidth / 2).dp, y = 0.dp)"
                            ))
                        } else {
                            newKtFile?.add(objectLiteralChild)
                        }
                    }
                }
            }
        } else {
            newKtFile?.add(property)
        }
    }

    override fun visitModifierList(modifierList: KtModifierList) {
        super.visitModifierList(modifierList)

        if (!alignFound) {
            modifierList.children.forEach { modifierChild ->
                if (modifierChild is KtCallExpression && modifierChild.calleeExpression?.text == "Modifier") {
                    modifierFound = true
                    val newModifierChild = KtPsiFactory(modifierList).createExpression(
                        modifierChild.text + ".align(Alignment.CenterVertically).offset(x = (-screenWidth / 2).dp, y = 0.dp)"
                    )
                    newKtFile?.addAfter(newModifierChild, modifierList)
                } else {
                    newKtFile?.add(modifierChild)
                }
            }
        }
    }

    fun convert(ktFile: KtFile): KtFile {
        newKtFile = KtPsiFactory(ktFile).createFile("class NewClass {n}")
        ktFile.accept(this)

        if (!alignFound && !modifierFound) {
            newKtFile?.addAfter(
                KtPsiFactory(ktFile).createExpression(".align(Alignment.CenterVertically).offset(x = (-screenWidth / 2).dp, y = 0.dp)"),
                newKtFile?.children?.filterIsInstance<KtProperty>()?.first { it.name == "bottomNav" }?.modifierList
            )
        }

        return newKtFile!!
    }
}
