package com.example.service

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AutoBuyerLogs {
    private val _logsFlow = MutableSharedFlow<String>(replay = 500)
    val logsFlow: SharedFlow<String> = _logsFlow.asSharedFlow()

    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    suspend fun addLog(message: String) {
        val timeStamp = dateFormat.format(Date())
        _logsFlow.emit("[$timeStamp] $message")
    }

    fun addLogBlocking(message: String) {
        val timeStamp = dateFormat.format(Date())
        _logsFlow.tryEmit("[$timeStamp] $message")
    }
}

