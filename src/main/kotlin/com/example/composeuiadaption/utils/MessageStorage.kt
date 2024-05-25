package com.example.composeuiadaption.utils

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object MessageStorage {

    fun showNotification(message: String, project: Project?) {
        NotificationGroup("someId", NotificationDisplayType.BALLOON)
            .createNotification("Success", message, NotificationType.INFORMATION, null)
            .notify(project)
    }

    fun showSuccessNotification(project: Project?) {
        NotificationGroup("someId", NotificationDisplayType.BALLOON)
            .createNotification("Success", TITLE_SUCCESS, NotificationType.INFORMATION, null)
            .notify(project)
    }

    fun showErrorNotification(project: Project?) {
        NotificationGroup("someId", NotificationDisplayType.BALLOON)
            .createNotification("Error", TITLE_ERROR, NotificationType.ERROR, null)
            .notify(project)
    }

    fun showWarningNotification(project: Project?) {
        NotificationGroup("someId", NotificationDisplayType.BALLOON)
            .createNotification("Warning", TITLE_WARNING, NotificationType.WARNING, null)
            .notify(project)
    }
}

private const val TITLE_SUCCESS = "Files generated successfully"
private const val TITLE_WARNING = "Check that the fields are filled in correctly"
private const val TITLE_ERROR = "Something went wrong, files were not generated"