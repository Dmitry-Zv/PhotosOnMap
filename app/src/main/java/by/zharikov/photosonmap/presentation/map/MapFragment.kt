package by.zharikov.photosonmap.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import by.zharikov.photosonmap.R
import by.zharikov.photosonmap.databinding.FragmentMapBinding
import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.utils.Constants.PHOTO_ARG
import by.zharikov.photosonmap.utils.collectLatestLifecycleFlow
import by.zharikov.photosonmap.utils.showSnackBar
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {


    companion object {

        private val PERMISSION = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )


    }


    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        var permissionGranted = true
        permissions.entries.forEach {
            if (it.key in PERMISSION && !it.value)
                permissionGranted = false
        }
        if (!permissionGranted) {
            performMinskLocation()

        } else {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true

        }

    }


    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: MapViewModel by viewModels()

    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(5000)
        .build()


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            val location = result.lastLocation
            performCurrentLocation(location)
        }
    }

    private fun allPermissionGranted() = PERMISSION.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


        mapFragment.getMapAsync(this)

        collectState()

        takePhoto()

    }

    private fun takePhoto() {
        binding.fab.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_mapFragment_to_cameraFragment2)

        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (allPermissionGranted()) {

            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true

        } else {
            requestAllPermissions()
        }

        viewModel.onEvent(event = MapEvent.GetAllPhotos)
        setMarkersClickListener()


    }


    private fun requestAllPermissions() {
        requestPermissionLauncher.launch(PERMISSION)
    }


    private fun performMinskLocation() {
        viewModel.onEvent(event = MapEvent.PerformMinskLocation)
    }

    @SuppressLint("MissingPermission")
    private fun performCurrentLocation(location: Location?) {

        if (location != null) {
            viewModel.onEvent(
                event = MapEvent.PerformCurrentLocation(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
        }


    }

    private fun collectState() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            when {
                state.msgError != null -> showSnackBar(msg = state.msgError, view = binding.root)
                state.listOfPhotoUi.isNotEmpty() -> setMarkers(listOfPhotoUi = state.listOfPhotoUi)
                state.photoUi != null -> goToThePhotoFragment(photoUi = state.photoUi)
                state.location != null -> setUpMap(state.location)
            }
        }
    }

    private fun setUpMap(location: LatLng) {

        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun setMarkersClickListener() {
        mMap.setOnMarkerClickListener {

            val id = it.tag as? Int

            if (id != null) {
                viewModel.onEvent(event = MapEvent.GetPhotoById(id = id))
            } else {
                viewModel.onEvent(event = MapEvent.ShowError(Exception("Problem to cast tag to Int")))
            }
            true
        }
    }


    private fun goToThePhotoFragment(photoUi: PhotoUi) {
        val bundle = bundleOf(PHOTO_ARG to photoUi)
        view?.findNavController()?.navigate(R.id.action_mapFragment_to_detailFragment, bundle)
    }

    private fun setMarkers(listOfPhotoUi: List<PhotoUi>) {
        listOfPhotoUi.forEach { photoUi ->


            val coordinate = LatLng(photoUi.lat, photoUi.lng)

            val marker = mMap.addMarker(
                MarkerOptions().position(coordinate)
            )
            marker?.tag = photoUi.id
        }
    }


    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (allPermissionGranted()) fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}