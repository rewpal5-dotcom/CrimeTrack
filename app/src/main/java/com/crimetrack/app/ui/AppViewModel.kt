package com.crimetrack.app.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.crimetrack.app.data.HistoryItem
import com.crimetrack.app.data.Lookup
import com.crimetrack.app.data.NumberInfo
import com.crimetrack.app.data.PhoneUtil
import com.crimetrack.app.data.RemoteLookup
import com.crimetrack.app.data.Report
import com.crimetrack.app.data.Store
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

enum class Screen { Home, Result, Details, MoreInfo, History, Premium, Settings, Report }

data class Stats(val reports: Int = 0, val numbers: Int = 0)

data class UiState(
    val screen: Screen = Screen.Home,
    val query: String = "",
    val searching: Boolean = false,
    val info: NumberInfo? = null,
    val history: List<HistoryItem> = emptyList(),
    val premium: Boolean = false,
    val darkTheme: Boolean = true,
    val serverUrl: String = "",
    val apiKey: String = "",
    val stats: Stats = Stats(),
    val message: String? = null
)

class AppViewModel(app: Application) : AndroidViewModel(app) {

    private val store = Store(app)
    private val backStack = ArrayDeque<Screen>()

    var state by mutableStateOf(
        run {
            val (reports, numbers) = store.stats()
            UiState(
                history = store.history(),
                premium = store.premium,
                darkTheme = store.darkTheme,
                serverUrl = store.serverUrl,
                apiKey = store.apiKey,
                stats = Stats(reports, numbers)
            )
        }
    )
        private set

    // ---------- search ----------

    fun onQueryChange(value: String) {
        state = state.copy(query = PhoneUtil.digitsOnly(value))
    }

    fun search() {
        val number = state.query
        if (number.length != 10) {
            state = state.copy(message = "10 digit ka mobile number daalein")
            return
        }
        if (state.searching) return
        state = state.copy(searching = true, message = null)
        viewModelScope.launch {
            val info = loadInfo(number)
            if (!info.valid) {
                state = state.copy(searching = false, message = "Ye valid Indian mobile number nahi lagta")
                return@launch
            }
            delay(250)
            val saved = state.history.firstOrNull { it.number == number }?.saved ?: false
            val list = (listOf(HistoryItem(number, System.currentTimeMillis(), saved)) +
                state.history.filter { it.number != number }).take(40)
            store.saveHistory(list)
            backStack.clear()
            state = state.copy(
                searching = false,
                info = info,
                history = list,
                query = "",
                screen = Screen.Result
            )
        }
    }

    fun openNumber(number: String) {
        viewModelScope.launch {
            val info = loadInfo(number)
            val saved = state.history.firstOrNull { it.number == number }?.saved ?: false
            val list = (listOf(HistoryItem(number, System.currentTimeMillis(), saved)) +
                state.history.filter { it.number != number }).take(40)
            store.saveHistory(list)
            backStack.clear()
            state = state.copy(info = info, history = list, screen = Screen.Result)
        }
    }

    private suspend fun loadInfo(number: String): NumberInfo {
        val local = store.reportsFor(number)
        val remote = if (store.serverUrl.isNotBlank()) {
            withContext(Dispatchers.IO) { RemoteLookup.fetch(store.serverUrl, store.apiKey, number) }
        } else {
            emptyList()
        }
        val merged = (local + remote.filter { r -> local.none { it.id == r.id } })
            .sortedByDescending { it.time }
        return Lookup.build(number, merged)
    }

    // ---------- reports ----------

    fun addReport(category: String, comment: String, reporter: String) {
        val info = state.info ?: return
        val report = Report(
            id = UUID.randomUUID().toString(),
            number = info.number,
            category = category,
            comment = comment.trim(),
            reporter = reporter.trim(),
            time = System.currentTimeMillis()
        )
        store.addReport(report)
        refreshInfo()
        state = state.copy(
            screen = Screen.Details,
            message = "Report save ho gayi — risk score update ho gaya",
            stats = store.stats().let { Stats(it.first, it.second) }
        )
    }

    fun deleteReport(id: String) {
        store.deleteReport(id)
        refreshInfo()
        state = state.copy(
            message = "Report delete ho gayi",
            stats = store.stats().let { Stats(it.first, it.second) }
        )
    }

    private fun refreshInfo() {
        val info = state.info ?: return
        state = state.copy(info = Lookup.build(info.number, store.reportsFor(info.number)))
    }

    // ---------- navigation ----------

    fun go(screen: Screen) {
        if (screen == state.screen) return
        backStack.addLast(state.screen)
        state = state.copy(screen = screen)
    }

    fun selectTab(screen: Screen) {
        backStack.clear()
        state = state.copy(screen = screen)
    }

    fun back(): Boolean {
        val previous = backStack.removeLastOrNull()
        return if (previous != null) {
            state = state.copy(screen = previous)
            true
        } else if (state.screen != Screen.Home) {
            state = state.copy(screen = Screen.Home)
            true
        } else {
            false
        }
    }

    // ---------- saved / history ----------

    fun toggleSave() {
        val info = state.info ?: return
        val isSaved = state.history.firstOrNull { it.number == info.number }?.saved ?: false
        val list = if (state.history.any { it.number == info.number }) {
            state.history.map { if (it.number == info.number) it.copy(saved = !isSaved) else it }
        } else {
            listOf(HistoryItem(info.number, System.currentTimeMillis(), true)) + state.history
        }
        store.saveHistory(list)
        state = state.copy(
            history = list,
            message = if (isSaved) "Saved se hata diya" else "Saved list me add ho gaya"
        )
    }

    fun isSaved(number: String): Boolean =
        state.history.firstOrNull { it.number == number }?.saved ?: false

    fun removeHistory(number: String) {
        val list = state.history.filter { it.number != number }
        store.saveHistory(list)
        state = state.copy(history = list, message = "History se hata diya")
    }

    fun clearHistory() {
        store.clearHistory()
        state = state.copy(history = emptyList(), message = "Poori history clear ho gayi")
    }

    // ---------- settings ----------

    fun upgrade() {
        store.premium = true
        state = state.copy(premium = true, message = "Premium unlock ho gaya (demo)")
    }

    fun setDarkTheme(value: Boolean) {
        store.darkTheme = value
        state = state.copy(darkTheme = value)
    }

    fun setServerUrl(value: String) {
        store.serverUrl = value
        state = state.copy(serverUrl = value)
    }

    fun setApiKey(value: String) {
        store.apiKey = value
        state = state.copy(apiKey = value)
    }

    fun testServer() {
        val url = state.serverUrl
        if (url.isBlank()) {
            state = state.copy(message = "Pehle API URL daalein")
            return
        }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { RemoteLookup.fetch(url, state.apiKey, "9876543210") }
            state = state.copy(
                message = if (result.isEmpty()) "Server se koi report nahi mili (URL/response check karein)" else result.size.toString() + " reports mili — connection theek hai"
            )
        }
    }

    fun clearAllData() {
        store.clearAll()
        backStack.clear()
        state = UiState(
            screen = Screen.Home,
            history = emptyList(),
            premium = false,
            darkTheme = true,
            serverUrl = "",
            apiKey = "",
            stats = Stats(),
            message = "Saara data clear ho gaya"
        )
    }

    fun clearMessage() {
        state = state.copy(message = null)
    }

    fun reportsJson(): String = store.reportsJson()
}
