package com.example.android.politicalpreparedness.features.voterinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Election
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoterInfoViewModel @Inject constructor(
        private val repository: VoterInfoRepository
) : ViewModel() {

    lateinit var selectedElection: Election

    private val _followedElection = MutableLiveData<Election>()
    val followedElection: LiveData<Election>
        get() = _followedElection

    private val _administrationBody = MutableLiveData<AdministrationBody>()
    val administrationBody: LiveData<AdministrationBody>
        get() = _administrationBody

    /*
    * retrive voter info from api service
    * */
    fun getVoterInfo() {
        viewModelScope.launch {
            try {
                val response = repository.getVoterInfo(selectedElection.division.state, selectedElection.id)
                _administrationBody.value = response.state?.get(0)?.electionAdministrationBody
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /*
    * Checks from db whether the election is followed previously.
    * */
    fun isElectionFollowed() {
        viewModelScope.launch {
            _followedElection.value = repository.getSavedElectionById(selectedElection.id)
        }
    }

    fun followButtonClickListener() {
        viewModelScope.launch {
            if (_followedElection.value == null) {
                _followedElection.value = selectedElection
                repository.saveElection(selectedElection)
            } else {
                _followedElection.value = null
                repository.deleteElection(selectedElection)
            }
        }
    }

}