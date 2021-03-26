package com.example.android.politicalpreparedness.features.election

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.LocalDataSource
import com.example.android.politicalpreparedness.network.RemoteDataSource
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import javax.inject.Inject

class ElectionRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    fun getAllSavedElections(): LiveData<List<Election>> = localDataSource.getAllSavedElections()

    suspend fun getElections(): ElectionResponse {
        return remoteDataSource.getElections()
    }

    suspend fun getRepresentatives(address: String): RepresentativeResponse {
        return remoteDataSource.getRepresentatives(address)
    }
}
