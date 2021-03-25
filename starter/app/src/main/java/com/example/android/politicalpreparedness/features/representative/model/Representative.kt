package com.example.android.politicalpreparedness.features.representative.model

import com.example.android.politicalpreparedness.network.models.Office
import com.example.android.politicalpreparedness.network.models.Official

data class Representative(
        val official: Official,
        val office: Office
)