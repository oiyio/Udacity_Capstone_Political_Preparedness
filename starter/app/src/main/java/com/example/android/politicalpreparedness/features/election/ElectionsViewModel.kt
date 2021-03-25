package com.example.android.politicalpreparedness.features.election


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElectionsViewModel @Inject constructor(
        private val repository: ElectionRepository
) : ViewModel() {

    val upcomingElectionsList = MutableStateFlow(listOf<Election>())

    val savedElections = repository.getAllSavedElections()

    val navigateToVoterInfoFragment = SingleLiveEvent<Election>()

    init {
        getElectionList()
    }

    private fun getElectionList() {
        viewModelScope.launch {
            try {
                val electionResponse = repository.getElections()
                upcomingElectionsList.value = electionResponse.elections
            } catch (e: Exception) {
            }
        }
    }
}