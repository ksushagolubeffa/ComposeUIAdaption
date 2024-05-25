package com.example.composeuiadaption.exception

open class PluginException(
    val header: String, message: String?
) : Exception(message)

class FileWriteException(
    message: String?
) : PluginException("File creation exception:", message)