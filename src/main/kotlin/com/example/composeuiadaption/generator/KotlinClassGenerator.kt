package com.example.composeuiadaption.generator

import com.example.composeuiadaption.adapter.BottomNavAdapter
import com.example.composeuiadaption.adapter.CardTextAdapter
import com.example.composeuiadaption.adapter.ListAdapter
import org.jetbrains.kotlin.com.intellij.openapi.util.Key
import org.jetbrains.kotlin.psi.*
import java.io.File

class KotlinClassGenerator(
    private val originalClass: KtFile,
    private val newPath: String,
    private val hasList: Boolean,
    private val hasBottomNavView: Boolean,
    private val hasCardWithTextView: Boolean
) {
    private val directory = File("$newPath/generated/ui")

    private var isOk = false

    fun generate(): Boolean {
        if (!directory.exists()) directory.mkdirs()

        val newName = "${originalClass.name}Generated"
        val newClass = KtPsiFactory(originalClass).createFile(
            "class $newName {\n\n}"
        )

        if (!hasList && !hasBottomNavView && !hasCardWithTextView) {
            originalClass.text.lines().forEach {
                addNewLineToFile(newClass, "${it}\n")
            }
//            isOk = true
        }

        originalClass.text.lines().forEachIndexed { index, line ->
            if (line.contains("LazyColumn")) {
                val lazyColumnFile = createLazyColumnFile(index, originalClass)

                val adapterFile = ListAdapter().convert(lazyColumnFile)

                adapterFile.text.lines().forEach {
                    addNewLineToFile(newClass, "${it}\n")

                }

                return@forEachIndexed
            } else if (line.contains("BottomNavigation")) {
                val bottomNavFile = createBottomNavigationFile(index, originalClass)
                val adapterFile = BottomNavAdapter().convert(bottomNavFile)

                adapterFile.text.lines().forEach {
                    addNewLineToFile(newClass, "${it}\n")
                }

                return@forEachIndexed
            } else if (line.contains("Card")) {
                val cardFile = createCardFile(index, originalClass)

                val adapterFile = CardTextAdapter().convert(cardFile)

                adapterFile.text.lines().forEach {
                    addNewLineToFile(newClass, "${it}\n")
                }

                return@forEachIndexed
            } else if (hasList && hasBottomNavView) {
                if (line.contains("LazyColumn")) {
                    val lazyColumnFile = createLazyColumnFile(index, originalClass)

                    val adapterFile = ListAdapter().convert(lazyColumnFile)

                    adapterFile.text.lines().forEach {
                        addNewLineToFile(newClass, "${it}\n")
                    }

                    return@forEachIndexed
                } else if (line.contains("BottomNavigation")) {
                    val bottomNavFile = createBottomNavigationFile(index, originalClass)

                    val adapterFile = BottomNavAdapter().convert(bottomNavFile)

                    adapterFile.text.lines().forEach {
                        addNewLineToFile(newClass, "${it}\n")
                    }

                    return@forEachIndexed
                }
            } else if (hasList && hasCardWithTextView) {
                if (line.contains("LazyColumn")) {
                    val lazyColumnFile = createLazyColumnFile(index, originalClass)

                    val adapterFile = ListAdapter().convert(lazyColumnFile)

                    adapterFile.text.lines().forEach {
                        addNewLineToFile(newClass, "${it}\n")
                    }

                    return@forEachIndexed
                } else if (line.contains("Card")) {
                    val cardFile = createCardFile(index, originalClass)

                    val adapterFile = CardTextAdapter().convert(cardFile)

                    adapterFile.text.lines().forEach {
                        addNewLineToFile(newClass, "${it}\n")
                    }

                    return@forEachIndexed
                }
            } else if (hasBottomNavView && hasCardWithTextView) {
                if (line.contains("BottomNavigation")) {
                    val bottomNavFile = createBottomNavigationFile(index, originalClass)

                    val adapterFile = BottomNavAdapter().convert(bottomNavFile)

                    adapterFile.text.lines().forEach {
                        addNewLineToFile(newClass, "${it}\n")
                    }

                    return@forEachIndexed
                } else if (line.contains("Card")) {
                    val cardFile = createCardFile(index, originalClass)

                    val adapterFile = CardTextAdapter().convert(cardFile)

                    adapterFile.text.lines().forEach {
                        addNewLineToFile(newClass, "${it}\n")
                    }

                    return@forEachIndexed
                }
            }
            addNewLineToFile(newClass, "${line}\n")
            isOk=true
        }
        writeToFile("$newPath/generated/ui/${newName}Generated.kt", originalClass)
//        originalClass.writeToFile(File("$newPath/generated/ui/${newName}Generated.kt"))
        return isOk
    }

    private fun writeToFile(filePath: String, ktFile: KtFile) {
        File(filePath).writeText(ktFile.text)
    }

    private fun createLazyColumnFile(index: Int, originalClass: KtFile): KtFile {
        val lazyColumnFile = KtPsiFactory(originalClass).createFile("fun LazyColumn() {\n\n")
        val newLine = index + 1

        originalClass.text.lines().forEachIndexed { i, line ->
            if (i >= newLine) addNewLineToFile(lazyColumnFile, "${line}\\n")

        }

        return lazyColumnFile
    }

    private fun createBottomNavigationFile(index: Int, originalClass: KtFile): KtFile {
        val bottomNavFile = KtPsiFactory(originalClass).createFile("fun BottomNavigation() {\n\n")
        val newLine = index + 1

        originalClass.text.lines().forEachIndexed { i, line ->
            if (i >= newLine) addNewLineToFile(bottomNavFile, "${line}\\n")

        }

        return bottomNavFile
    }

    private fun createCardFile(index: Int, originalClass: KtFile): KtFile {
        val cardFile = KtPsiFactory(originalClass).createFile("fun Card() {\n\n")
        val newLine = index + 1

        originalClass.text.lines().forEachIndexed { i, line ->
            if (i >= newLine) addNewLineToFile(cardFile, "${line}\\n")
        }

        return cardFile
    }

    private fun addNewLineToFile(ktFile: KtFile, newLine: String) {
        val factory = KtPsiFactory(ktFile.project)
        val newContent = ktFile.text + "\n$newLine"
        val tempFile = File.createTempFile("temp", ".kt")
        tempFile.writeText(newContent)
        val newFile = factory.createFile(tempFile.readText())
        ktFile.putUserData(Key.create("MY_NEW_FILE"), newFile)
    }
}