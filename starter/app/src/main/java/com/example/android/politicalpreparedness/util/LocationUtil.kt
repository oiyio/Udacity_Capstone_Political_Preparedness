package com.example.android.politicalpreparedness.util


import android.content.IntentSender
import androidx.fragment.app.FragmentActivity
import com.example.android.politicalpreparedness.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar

/*
*  🔥 Uses the Location Client to check the current state of location settings, and gives the user
*  the opportunity to turn on location services within our app.
*/
fun checkDeviceLocationSettings(activity: FragmentActivity, resolve: Boolean = true, lambda: () -> Unit) {
    val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_LOW_POWER
    }
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

    val settingsClient = LocationServices.getSettingsClient(activity)
    val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())

    // add a failure listener
    locationSettingsResponseTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException && resolve) {
            // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
            try {
                // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                exception.startResolutionForResult(activity, REQUEST_TURN_DEVICE_LOCATION_ON)
            } catch (sendEx: IntentSender.SendIntentException) {
                // Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
            }
        } else {
            Snackbar.make(activity.findViewById(android.R.id.content), R.string.location_required_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) {
                        checkDeviceLocationSettings(
                                activity = activity,
                                lambda = lambda)
                    }.show()
        }
    }

    // add a complete listener
    locationSettingsResponseTask.addOnCompleteListener {
        if (it.isSuccessful) {
            lambda()
        }
    }
}

const val REQUEST_FOREGROUND_ONLY_PERMISSION_REQUEST_CODE = 34
const val REQUEST_BACKGROUND_ONLY_PERMISSION_REQUEST_CODE = 35
const val REQUEST_TURN_DEVICE_LOCATION_ON = 29