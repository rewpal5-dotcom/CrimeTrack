package com.crimetrack.app.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

/** Sab kuch device par: reports, history, saved numbers aur settings. */
class Store(context: Context) {

    private val prefs = context.getSharedPreferences("crimetrack", Context.MODE_PRIVATE)

    var premium: Boolean
        get() = prefs.getBoolean(KEY_PREMIUM, false)
        set(value) { prefs.edit().putBoolean(KEY_PREMIUM, value).apply() }

    var darkTheme: Boolean
        get() = prefs.getBoolean(KEY_DARK, true)
        set(value) { prefs.edit().putBoolean(KEY_DARK, value).apply() }

    var serverUrl: String
        get() = prefs.getString(KEY_SERVER, "") ?: ""
        set(value) { prefs.edit().putString(KEY_SERVER, value.trim()).apply() }

    var apiKey: String
        get() = prefs.getString(KEY_APIKEY, "") ?: ""
        set(value) { prefs.edit().putString(KEY_APIKEY, value.trim()).apply() }

    // ---------- reports ----------

    fun allReports(): List<Report> {
        val raw = prefs.getString(KEY_REPORTS, null) ?: return emptyList()
        return try {
            val arr = JSONArray(raw)
            (0 until arr.length()).mapNotNull { i ->
                val o = arr.optJSONObject(i) ?: return@mapNotNull null
                Report(
                    id = o.optString("id", UUID.randomUUID().toString()),
                    number = o.optString("number"),
                    category = o.optString("category", "Spam"),
                    comment = o.optString("comment", ""),
                    reporter = o.optString("reporter", ""),
                    time = o.optLong("time", 0L)
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun reportsFor(number: String): List<Report> =
        allReports().filter { it.number == number }.sortedByDescending { it.time }

    fun addReport(report: Report) {
        val list = allReports().toMutableList()
        list.add(0, report)
        writeReports(list)
    }

    fun deleteReport(id: String) {
        writeReports(allReports().filterNot { it.id == id })
    }

    private fun writeReports(list: List<Report>) {
        val arr = JSONArray()
        list.forEach { r ->
            val o = JSONObject()
            o.put("id", r.id)
            o.put("number", r.number)
            o.put("category", r.category)
            o.put("comment", r.comment)
            o.put("reporter", r.reporter)
            o.put("time", r.time)
            arr.put(o)
        }
        prefs.edit().putString(KEY_REPORTS, arr.toString()).apply()
    }

    fun reportsJson(): String = prefs.getString(KEY_REPORTS, "[]") ?: "[]"

    fun stats(): Pair<Int, Int> {
        val all = allReports()
        return all.size to all.map { it.number }.distinct().size
    }

    // ---------- history ----------

    fun history(): List<HistoryItem> {
        val raw = prefs.getString(KEY_HISTORY, null) ?: return emptyList()
        return try {
            val arr = JSONArray(raw)
            (0 until arr.length()).mapNotNull { i ->
                val o = arr.optJSONObject(i) ?: return@mapNotNull null
                HistoryItem(
                    number = o.optString("number"),
                    time = o.optLong("time", 0L),
                    saved = o.optBoolean("saved", false)
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveHistory(items: List<HistoryItem>) {
        val arr = JSONArray()
        items.forEach { item ->
            val o = JSONObject()
            o.put("number", item.number)
            o.put("time", item.time)
            o.put("saved", item.saved)
            arr.put(o)
        }
        prefs.edit().putString(KEY_HISTORY, arr.toString()).apply()
    }

    fun savedNumbers(): Set<String> = history().filter { it.saved }.map { it.number }.toSet()

    fun clearHistory() {
        prefs.edit().remove(KEY_HISTORY).apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val KEY_REPORTS = "reports"
        const val KEY_HISTORY = "history"
        const val KEY_PREMIUM = "premium"
        const val KEY_DARK = "dark_theme"
        const val KEY_SERVER = "server_url"
        const val KEY_APIKEY = "api_key"
    }
}
