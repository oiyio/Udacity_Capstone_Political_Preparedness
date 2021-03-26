package com.example.android.politicalpreparedness.features.representative

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.features.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.util.checkDeviceLocationSettings
import com.example.android.politicalpreparedness.util.foregroundAndBackgroundLocationPermissionGranted
import com.example.android.politicalpreparedness.util.isPermissionGranted
import com.example.android.politicalpreparedness.util.requestBackgroundLocationPermission
import com.example.android.politicalpreparedness.util.requestForegroundLocationPermission
import com.example.android.politicalpreparedness.util.showSnackbarWithSettingsAction
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class RepresentativeFragment : Fragment(R.layout.fragment_representative) {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var binding: FragmentRepresentativeBinding

    val viewModel: RepresentativeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        binding = FragmentRepresentativeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRepresentativeAdapter()

        binding.buttonUseMyLocation.setOnClickListener {
            checkPermissions()
        }

        binding.buttonSearch.setOnClickListener {
            viewModel.address.value?.let {
                viewModel.getRepresentatives()
            }
        }

        binding.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.address.value?.state = binding.spinnerState.selectedItem as String
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.address.value?.state = binding.spinnerState.selectedItem as String
            }
        }
    }

    private fun setupRepresentativeAdapter() {
        val representativeListAdapter = RepresentativeListAdapter()
        binding.recyclerViewRepresentativeList.adapter = representativeListAdapter

        viewModel.representativeList.observe(
            viewLifecycleOwner,
            Observer { representativeList ->
                representativeList?.let {
                    representativeListAdapter.submitList(representativeList)
                }
            }
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (isPermissionGranted(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                checkDeviceLocationSettings(
                    activity = requireActivity(),
                    resolve = false,
                    lambda = {
                        getMyCurrentLocation()
                    }
                )
            } else {
                requestBackgroundLocationPermission(this)
            }
        } else {
            showSnackbarWithSettingsAction(requireActivity())
        }
    }

    /*
    * check permissions, if not granted request permission. If granted, get my current location and fill the editText fields.
    * */
    @SuppressLint("MissingPermission")
    private fun checkPermissions() {
        if (foregroundAndBackgroundLocationPermissionGranted(requireContext())) {
            checkDeviceLocationSettings(
                activity = requireActivity(),
                lambda = {
                    getMyCurrentLocation()
                }
            )
        } else {
            requestForegroundLocationPermission(this)
        }
    }

    /*
    * invoked only if all permissions are granted and location is turned on.
    * */
    @SuppressLint("MissingPermission")
    private fun getMyCurrentLocation() {
        if (foregroundAndBackgroundLocationPermissionGranted(requireContext())) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val address = geoCodeLocation(location)
                    viewModel.setAddress(address)
                    viewModel.getRepresentatives()
                }
            }
        }
    }

    /*
    * Gets the address w.r.t. latitude and longitude values.
    * */
    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare, address.subThoroughfare,
                    address.locality
                        ?: "",
                    address.adminArea, address.postalCode
                )
            }
            .first()
    }
}
