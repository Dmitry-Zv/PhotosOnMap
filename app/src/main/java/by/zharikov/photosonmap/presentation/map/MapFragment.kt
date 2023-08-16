package by.zharikov.photosonmap.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import by.zharikov.photosonmap.R
import by.zharikov.photosonmap.databinding.FragmentMapBinding
import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.presentation.SharedViewModel
import by.zharikov.photosonmap.presentation.map.cluster.ClusterBottomSheetFragment
import by.zharikov.photosonmap.utils.Constants.PHOTO_ARG
import by.zharikov.photosonmap.utils.collectLatestLifecycleFlow
import by.zharikov.photosonmap.utils.showSnackBar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
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
            fusedLocationClient.lastLocation.addOnSuccessListener {
                performCurrentLocation(location = it)
            }

        }

    }


    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: MapViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var clusterManager: ClusterManager<PhotoUi>


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

        Log.d("CLUSTER", "onViewCreated")
        mapFragment.getMapAsync(this)

        collectState()
        collectPhotoUi()
        takePhoto()

    }

    private fun collectPhotoUi() {
        collectLatestLifecycleFlow(sharedViewModel.photoUi) { photoUi ->
            if (photoUi == PhotoUi()) return@collectLatestLifecycleFlow
            else goToThePhotoFragment(photoUi = photoUi)
        }
    }

    private fun takePhoto() {
        binding.fab.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_mapFragment_to_cameraFragment2)

        }
    }

    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        clusterManager = ClusterManager(requireContext(), mMap)
        mMap.setOnMarkerClickListener(clusterManager)
        mMap.setOnCameraIdleListener(clusterManager)
        setMarkersClickListener()
        viewModel.onEvent(event = MapEvent.GetAllPhotos)

        if (allPermissionGranted()) {

            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener {
                performCurrentLocation(location = it)
            }

        } else {
            requestAllPermissions()
        }


    }


    private fun requestAllPermissions() {
        requestPermissionLauncher.launch(PERMISSION)
    }


    private fun performMinskLocation() {
        viewModel.onEvent(event = MapEvent.PerformMinskLocation)
    }

    @SuppressLint("MissingPermission")
    private fun performCurrentLocation(location: Location?) {

        if (location != null)
            viewModel.onEvent(
                event = MapEvent.PerformCurrentLocation(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )


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
        if (::mMap.isInitialized) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun setMarkersClickListener() {
        clusterManager.setOnClusterItemClickListener {

            goToThePhotoFragment(it)

            true
        }

        clusterManager.setOnClusterClickListener {
            ClusterBottomSheetFragment().show(childFragmentManager, "bottom_sheet_tag")
            sharedViewModel.setListOfPhotoUi(it.items.toList())
            true
        }

    }


    private fun goToThePhotoFragment(photoUi: PhotoUi?) {
        photoUi?.let {
            val bundle = bundleOf(PHOTO_ARG to photoUi)
            view?.findNavController()?.navigate(R.id.action_mapFragment_to_detailFragment, bundle)

        } ?: viewModel.onEvent(event = MapEvent.ShowError(Exception("PhotoUi is null")))
        sharedViewModel.setPhotoUi(photoUi = PhotoUi())
    }

    private fun setMarkers(listOfPhotoUi: List<PhotoUi>) {
        clusterManager.addItems(listOfPhotoUi)
        clusterManager.cluster()
    }

    override fun onDestroyView() {
        Log.d("CLUSTER", "onDestroy")
        clusterManager.clearItems()
        mMap.clear()
        super.onDestroyView()
        _binding = null
    }


}