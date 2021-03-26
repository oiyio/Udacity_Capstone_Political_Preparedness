package com.example.android.politicalpreparedness.features.voterinfo

import com.example.android.politicalpreparedness.database.LocalDataSource
import com.example.android.politicalpreparedness.network.RemoteDataSource
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import javax.inject.Inject

class VoterInfoRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getVoterInfo(address: String, electionId: Int): VoterInfoResponse {
        return remoteDataSource.getVoterInfo(address, electionId)
    }

    suspend fun getSavedElectionById(electionId: Int): Election {
        return localDataSource.getSavedElectionById(electionId)
    }

    suspend fun saveElection(election: Election) {
        localDataSource.insertElectionToDb(election)
    }

    suspend fun deleteElection(election: Election) {
        localDataSource.deleteElection(election)
    }
}
