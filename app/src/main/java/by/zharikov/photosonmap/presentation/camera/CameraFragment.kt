package by.zharikov.photosonmap.presentation.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import by.zharikov.photosonmap.R
import by.zharikov.photosonmap.databinding.FragmentCameraBinding
import by.zharikov.photosonmap.presentation.SharedViewModel
import by.zharikov.photosonmap.utils.bitmapToBase64
import by.zharikov.photosonmap.utils.collectLatestLifecycleFlow
import by.zharikov.photosonmap.utils.imageProxyToBitmap
import by.zharikov.photosonmap.utils.showSnackBar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraFragment : Fragment() {


    companion object {

        private val PERMISSION = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )


    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        var permissionGranted = true
        permissions.entries.forEach {
            if (it.key in PERMISSION && !it.value)
                permissionGranted = false
        }
        if (!permissionGranted) {
            viewModel.onEvent(event = CameraEvent.ShowError(Exception("Permissions not granted.")))

        } else {
            startCamera()
        }

    }

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: CameraViewModel by viewModels()


    private fun allPermissionGranted() = PERMISSION.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (allPermissionGranted()) {

            startCamera()
        } else {
            requestAllPermissions()
        }
        val clickAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.button_photo_animation)
        collectLatestLifecycleFlow(sharedViewModel.userState) { token ->
            viewModel.onEvent(event = CameraEvent.SetToken(token = token))
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        collectState()

        binding.imageCaptureButton.setOnClickListener {
            it.startAnimation(clickAnimation)
            takePhoto()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun collectState() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            when {
                state.msgError != null -> {
                    showSnackBar(msg = state.msgError, view = binding.root)
                    view?.findNavController()?.navigateUp()
                }
                state.data != null -> {
                    showSnackBar(msg = "Photo ${state.data.data.id} added", view = binding.root)
                    view?.findNavController()?.navigateUp()
                }
            }
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                @SuppressLint("MissingPermission")
                override fun onCaptureSuccess(image: ImageProxy) {
                    cameraExecutor.execute {
                        val bitmap = imageProxyToBitmap(image)
                        val base64 = bitmapToBase64(bitmap)
                        fusedLocationClient.lastLocation.addOnSuccessListener {
                            val latitude = it.latitude
                            val longitude = it.longitude
                            val currentTime = System.currentTimeMillis() / 1000

                            viewModel.onEvent(
                                event = CameraEvent.SendPhoto(
                                    base64Image = base64,
                                    latitude = latitude,
                                    longitude = longitude,
                                    currentTime = currentTime
                                )
                            )
                        }
                        image.close()
                    }

                }

                override fun onError(exception: ImageCaptureException) {
                    viewModel.onEvent(event = CameraEvent.ShowError(exception = exception))
                }
            }
        )

    }


    private fun requestAllPermissions() {
        requestPermissionLauncher.launch(PERMISSION)
    }

    private fun startCamera() {
        val cameraProvideFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProvideFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProvideFuture.get()
            val preview = Preview.Builder().setTargetResolution(Size(720, 1280)).build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            imageCapture =
                ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .setTargetResolution(Size(720, 1280))
                    .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                viewModel.onEvent(event = CameraEvent.ShowError(exception = e))
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }
}