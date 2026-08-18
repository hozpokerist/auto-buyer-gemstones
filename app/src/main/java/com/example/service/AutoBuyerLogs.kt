package com.example.service

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date
import java.util.Locale

object AutoBuyerLogs {
    private val _logsFlow = MutableSharedFlow<String>(replay = 500)
    val logsFlow: SharedFlow<String> = _logsFlow.asSharedFlow()

    private val logHistory = Collections.synchronizedList(mutableListOf<String>())
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    suspend fun addLog(message: String) {
        val timeStamp = dateFormat.format(Date())
        val formatted = "[$timeStamp] $message"
        if (logHistory.size > 1000) {
            logHistory.removeAt(0)
        }
        logHistory.add(formatted)
        _logsFlow.emit(formatted)
    }

    fun addLogBlocking(message: String) {
        val timeStamp = dateFormat.format(Date())
        val formatted = "[$timeStamp] $message"
        if (logHistory.size > 1000) {
            logHistory.removeAt(0)
        }
        logHistory.add(formatted)
        _logsFlow.tryEmit(formatted)
    }

    fun getAllLogsText(): String {
        return synchronized(logHistory) {
            if (logHistory.isEmpty()) {
                _logsFlow.replayCache.joinToString("\n")
            } else {
                logHistory.joinToString("\n")
            }
        }
    }

    fun clearLogs() {
        logHistory.clear()
    }
}

