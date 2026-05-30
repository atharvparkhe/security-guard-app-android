package com.securityguard.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.securityguard.app.models.InwardEntry
import com.securityguard.app.models.InwardListResult
import com.securityguard.app.models.OutwardEntry
import com.securityguard.app.models.VisitorEntry
import com.securityguard.app.network.GateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

enum class GuardTab { IN, OUT, VISITORS }

@HiltViewModel
class GuardViewModel @Inject constructor(
    private val gateRepository: GateRepository,
) : ViewModel() {

    private val _tab = MutableStateFlow(GuardTab.IN)
    val tab: StateFlow<GuardTab> = _tab.asStateFlow()

    private val _inward = MutableStateFlow<InwardListResult?>(null)
    val inward: StateFlow<InwardListResult?> = _inward.asStateFlow()

    private val _outward = MutableStateFlow<List<OutwardEntry>>(emptyList())
    val outward: StateFlow<List<OutwardEntry>> = _outward.asStateFlow()

    private val _visitors = MutableStateFlow<List<VisitorEntry>>(emptyList())
    val visitors: StateFlow<List<VisitorEntry>> = _visitors.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun selectTab(tab: GuardTab) {
        _tab.value = tab
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _loading.value = true
            val today = gateRepository.todayQueryDate()
            when (_tab.value) {
                GuardTab.IN -> _inward.value = gateRepository.fetchInwardEntries(today, today)
                GuardTab.OUT -> _outward.value = gateRepository.fetchOutwardEntries(today, today)
                GuardTab.VISITORS -> _visitors.value = gateRepository.fetchVisitorEntries(today, today)
            }
            _loading.value = false
        }
    }

    fun loadInwardDetail(id: String, onLoaded: (InwardEntry) -> Unit) {
        viewModelScope.launch {
            onLoaded(gateRepository.fetchInwardEntry(id))
        }
    }

    fun allowInInward(id: String, inTime: Instant?, onDone: (InwardEntry) -> Unit) {
        viewModelScope.launch {
            onDone(gateRepository.allowInInward(id, inTime))
            refresh()
        }
    }

    fun markExitInward(id: String, outTime: Instant?, onDone: (InwardEntry) -> Unit) {
        viewModelScope.launch {
            onDone(gateRepository.markExitInward(id, outTime))
            refresh()
        }
    }

    init {
        refresh()
    }
}
