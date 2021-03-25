package com.example.android.politicalpreparedness.features.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.features.election.ElectionRepository
import com.example.android.politicalpreparedness.features.representative.model.Representative
import com.example.android.politicalpreparedness.network.models.Address
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepresentativeViewModel @Inject constructor(
        private val repository: ElectionRepository
) : ViewModel() {


    private val _representativeList = MutableLiveData<List<Representative>>()
    val representativeList: LiveData<List<Representative>>
        get() = _representativeList

    var address = MutableLiveData<Address>().apply {
        value = Address("", "", "", "", "")
    }

    var loadingVisible = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun getRepresentatives() {
        val addressStr = address.value!!.toFormattedString()

        viewModelScope.launch {
            try {
                loadingVisible.value = true
                val representativeResponse = repository.getRepresentatives(addressStr)

                _representativeList.value = representativeResponse.offices.flatMap { office ->
                    office.getRepresentatives(representativeResponse.officials)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                loadingVisible.value = false
            }
        }
    }

    fun setAddress(_address: Address) {
        address.value = _address
    }

}