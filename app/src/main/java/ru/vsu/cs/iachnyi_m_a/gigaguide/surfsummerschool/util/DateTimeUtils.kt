package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.util

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    
    fun longToDateTimeString(timestamp: Long, format: String): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(Date(timestamp))
    }
    
    fun currentDateTimeToLong(): Long {
        return System.currentTimeMillis()
    }
}