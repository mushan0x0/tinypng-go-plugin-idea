package com.github.mushan0x0.tinypnggopluginidea

import com.intellij.notification.*
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import java.io.BufferedReader
import java.io.InputStreamReader


class OptimizeImageSizeAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        ProgressManager.getInstance().run(object : Task.Backgroundable(e.project, "Title") {
            override fun run(progressIndicator: ProgressIndicator) {
                progressIndicator.text = "compressing"
                val virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
                val process: Process = Runtime.getRuntime().exec(arrayOf("bash", "-l", "-c", "npx tinypng-go " + virtualFile?.canonicalPath))
//                ProcessBuilder("npx", "tinypng-go", virtualFile?.canonicalPath).start()
                val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
                process.waitFor();
                val group = NotificationGroup("", NotificationDisplayType.NONE, true)
                val notification: Notification = group.createNotification(
                    "Compression complete",
                    bufferedReader.use(BufferedReader::readText),
                    NotificationType.INFORMATION,
                    NotificationListener.URL_OPENING_LISTENER
                )
                Notifications.Bus.notify(notification)
                progressIndicator.fraction = 1.0
            }
        })
    }
}
