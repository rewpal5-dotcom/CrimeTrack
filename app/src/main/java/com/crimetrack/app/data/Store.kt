package com.crimetrack.app.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/** Chhoti local storage — history, saved records aur settings yahan rehti hain. */
class Store(context: Context) {

    private val prefs = context.getSharedPreferences("crimetrack", Context.MODE_PRIVATE)

    var premium: Boolean
        get() = prefs.getBoolean(KEY_PREMIUM, false)
        set(value) { prefs.edit().putBoolean(KEY_PREMIUM, value).apply() }

    var darkTheme: Boolean
        get() = prefs.getBoolean(KEY_DARK, true)
        set(value) { prefs.edit().putBoolean(KEY_DARK, value).apply() }

    var demoMode: Boolean
        get() = prefs.getBoolean(KEY_DEMO, true)
        set(value) { prefs.edit().putBoolean(KEY_DEMO, value).apply() }

    fun history(): List<HistoryItem> {
        val raw = prefs.getString(KEY_HISTORY, null) ?: return emptyList()
        return try {
            val arr = JSONArray(raw)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                HistoryItem(
                    number = o.optString("number"),
                    name = o.optString("name"),
                    time = o.optLong("time"),
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
            o.put("name", item.name)
            o.put("time", item.time)
            o.put("saved", item.saved)
            arr.put(o)
        }
        prefs.edit().putString(KEY_HISTORY, arr.toString()).apply()
    }

    fun clearHistory() {
        prefs.edit().remove(KEY_HISTORY).apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val KEY_HISTORY = "history"
        const val KEY_PREMIUM = "premium"
        const val KEY_DARK = "dark_theme"
        const val KEY_DEMO = "demo_mode"
    }
}
