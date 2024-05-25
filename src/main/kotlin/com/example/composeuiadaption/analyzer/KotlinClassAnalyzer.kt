package com.example.composeuiadaption.analyzer

import com.example.composeuiadaption.generator.KotlinClassGenerator
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtilCore
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile

import com.intellij.psi.PsiFileFactory
import com.intellij.openapi.fileTypes.FileTypeManager
import org.jetbrains.kotlin.psi.KtFunction

class KotlinClassAnalyzer(private val project: Project?, private val path: String) {
    fun analyzeFiles(): Boolean {
        val ktFiles = mutableListOf<KtFile>()
        val kotlinFileType = FileTypeManager.getInstance().getFileTypeByExtension("kt")

        project?.baseDir?.findFileByRelativePath(path)?.let { dir ->
            if (dir.isDirectory) {
                dir.children.forEach { file ->
                    if (file.extension == "kt") {

                        val psiFile = PsiFileFactory.getInstance(project).createFileFromText(
                            VfsUtilCore.virtualToIoFile(file).name,
                            kotlinFileType,
                            VfsUtilCore.virtualToIoFile(file).readText()
                        )

                        if (psiFile is KtFile) {
                            ktFiles.add(psiFile)
                        }
                    }
                }
            }
        }

        ktFiles.forEach { ktFile ->
            ktFile.declarations.filterIsInstance<KtClass>().forEach { ktClass ->
                ktClass.declarations.filterIsInstance<KtFunction>().forEach { ktFunction ->
                    if (ktFunction.annotationEntries.any { it.text == "@Composable" }) {
                        val composeFunAnalyzer = ComposeFunAnalyzer(ktClass)
                        val generator = KotlinClassGenerator(
                            ktFile, path,
                            composeFunAnalyzer.hasList,
                            composeFunAnalyzer.hasBottomNavView,
                            composeFunAnalyzer.hasCardWithTextView
                        )
                        generator.generate()
                    }
                }
            }
        }
        return true
    }
}
