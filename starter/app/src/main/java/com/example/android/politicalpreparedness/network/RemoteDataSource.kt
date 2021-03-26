package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val retrofitService: CivicsApiService
) {

    suspend fun getElections(): ElectionResponse {
        return retrofitService.getElections()
    }

    suspend fun getVoterInfo(address: String, electionId: Int): VoterInfoResponse {
        return retrofitService.getVoterInfo(address, electionId)
    }

    suspend fun getRepresentatives(address: String): RepresentativeResponse {
        return retrofitService.getRepresentatives(address)
    }
}
