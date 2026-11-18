package com.crimetrack.app.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.crimetrack.app.data.HistoryItem
import com.crimetrack.app.data.Lookup
import com.crimetrack.app.data.NumberRecord
import com.crimetrack.app.data.Store
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** App ki screens. Bottom bar inhi me se 4 dikhata hai. */
enum class Screen { Home, Result, Details, MoreInfo, History, Premium, Settings }

data class UiState(
    val screen: Screen = Screen.Home,
    val query: String = "",
    val searching: Boolean = false,
    val record: NumberRecord? = null,
    val history: List<HistoryItem> = emptyList(),
    val premium: Boolean = false,
    val darkTheme: Boolean = true,
    val demoMode: Boolean = true,
    val message: String? = null
)

class AppViewModel(app: Application) : AndroidViewModel(app) {

    private val store = Store(app)
    private val backStack = ArrayDeque<Screen>()

    var state by mutableStateOf(
        UiState(
            history = store.history(),
            premium = store.premium,
            darkTheme = store.darkTheme,
            demoMode = store.demoMode
        )
    )
        private set

    // ---------- search ----------

    fun onQueryChange(value: String) {
        val digits = value.filter { it.isDigit() }.take(10)
        state = state.copy(query = digits)
    }

    fun search() {
        val number = state.query
        if (number.length != 10 || number.first() !in '6'..'9') {
            state = state.copy(message = "10 digit ka sahi mobile number daalein (6-9 se shuru)")
            return
        }
        if (state.searching) return

        state = state.copy(searching = true, message = null)
        viewModelScope.launch {
            delay(650)
            val record = Lookup.current.lookup(number)
            val alreadySaved = state.history.firstOrNull { it.number == number }?.saved ?: false
            val item = HistoryItem(
                number = number,
                name = record.name,
                time = System.currentTimeMillis(),
                saved = alreadySaved
            )
            val list = (listOf(item) + state.history.filter { it.number != number }).take(30)
            store.saveHistory(list)
            backStack.clear()
            state = state.copy(
                searching = false,
                record = record,
                history = list,
                screen = Screen.Result,
                query = ""
            )
        }
    }

    /** History ya suggestion chip se seedha record kholna. */
    fun openNumber(number: String) {
        viewModelScope.launch {
            val record = Lookup.current.lookup(number)
            val alreadySaved = state.history.firstOrNull { it.number == number }?.saved ?: false
            val item = HistoryItem(number, record.name, System.currentTimeMillis(), alreadySaved)
            val list = (listOf(item) + state.history.filter { it.number != number }).take(30)
            store.saveHistory(list)
            backStack.clear()
            state = state.copy(record = record, history = list, screen = Screen.Result)
        }
    }

    fun quickSearch(number: String) {
        state = state.copy(query = number)
        search()
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
        } else {
            if (state.screen != Screen.Home) {
                state = state.copy(screen = Screen.Home)
                true
            } else {
                false
            }
        }
    }

    // ---------- saved / history ----------

    fun toggleSave() {
        val record = state.record ?: return
        val isSaved = state.history.firstOrNull { it.number == record.number }?.saved ?: false
        val list = if (state.history.any { it.number == record.number }) {
            state.history.map {
                if (it.number == record.number) it.copy(saved = !isSaved) else it
            }
        } else {
            listOf(HistoryItem(record.number, record.name, System.currentTimeMillis(), true)) + state.history
        }
        store.saveHistory(list)
        state = state.copy(
            history = list,
            message = if (isSaved) "Contact se hata diya" else "Contacts me save ho gaya"
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

    // ---------- premium / settings ----------

    fun upgrade() {
        store.premium = true
        state = state.copy(premium = true, message = "Premium unlock ho gaya (demo)")
    }

    fun setDarkTheme(value: Boolean) {
        store.darkTheme = value
        state = state.copy(darkTheme = value)
    }

    fun setDemoMode(value: Boolean) {
        store.demoMode = value
        state = state.copy(demoMode = value)
    }

    fun clearAllData() {
        store.clearAll()
        backStack.clear()
        state = UiState(
            history = emptyList(),
            premium = false,
            darkTheme = true,
            demoMode = true,
            screen = Screen.Home,
            message = "Saara local data clear ho gaya"
        )
    }

    fun clearMessage() {
        state = state.copy(message = null)
    }
}
