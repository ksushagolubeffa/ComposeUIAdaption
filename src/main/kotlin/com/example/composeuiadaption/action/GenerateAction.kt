package com.example.composeuiadaption.action

import com.example.composeuiadaption.analyzer.KotlinClassAnalyzer
import com.example.composeuiadaption.recognizer.DeviceRecognizer.deviceRecognizer
import com.example.composeuiadaption.utils.MessageStorage.showErrorNotification
import com.example.composeuiadaption.utils.MessageStorage.showNotification
import com.example.composeuiadaption.utils.MessageStorage.showSuccessNotification
import com.example.composeuiadaption.utils.MessageStorage.showWarningNotification
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import java.io.File

class GenerateAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val dialog = ScreenSizeDialog()
        dialog.show()
        if (dialog.isOK) {
            val screenDimensions = dialog.getScreenDimensions()
            if (screenDimensions != null) {
                val width = screenDimensions.first.toLong()
                val height = screenDimensions.second.toLong()
                val inches = screenDimensions.third
                val device = deviceRecognizer(width, height, inches)
                if(device=="Tablet"){
                    val virtualFile = e.dataContext.getData(PlatformDataKeys.VIRTUAL_FILE) ?: return
                    val folderPath = virtualFile.path
                    val directory = File("$folderPath/generated/ui")
                    if (!directory.exists()) directory.mkdirs()
                    File("$folderPath/generated/ui/GeneratedClass.kt").createNewFile()
                    val analyzer = KotlinClassAnalyzer(project = e.project, folderPath).analyzeFiles()

                    if(analyzer){
                        showSuccessNotification(e.project)
                    }else{
                        showErrorNotification(e.project)
                    }
                }
                else{
                    showWarningNotification(e.project)
                }
            }else{
                showWarningNotification(e.project)
            }
        }
    }
}


