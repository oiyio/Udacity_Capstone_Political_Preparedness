package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Election
import javax.inject.Inject

class LocalDataSource @Inject constructor(
        private val electionDao: ElectionDao
) {

    fun getAllSavedElections(): LiveData<List<Election>> = electionDao.getAllSavedElections()

    suspend fun getSavedElectionById(electionId: Int): Election {
        return electionDao.getSavedElectionById(electionId)
    }

    suspend fun insertElectionToDb(election: Election) {
        electionDao.insertElectionToDb(election)
    }

    suspend fun deleteElection(election: Election) {
        electionDao.deleteElection(election)
    }
}
