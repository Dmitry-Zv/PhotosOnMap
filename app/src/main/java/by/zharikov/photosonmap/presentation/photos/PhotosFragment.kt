package by.zharikov.photosonmap.presentation.photos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.zharikov.photosonmap.R
import by.zharikov.photosonmap.adapter.PhotoAdapter
import by.zharikov.photosonmap.databinding.FragmentPhotosBinding
import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.presentation.SharedViewModel
import by.zharikov.photosonmap.utils.Constants.PHOTO_ARG
import by.zharikov.photosonmap.utils.collectLatestLifecycleFlow
import by.zharikov.photosonmap.utils.showAlert
import by.zharikov.photosonmap.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosFragment : Fragment(), DeletePhotoListener, PhotoClickListener {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: PhotosViewModel by viewModels()
    private lateinit var adapter: PhotoAdapter

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            binding.recyclerPhotos.scrollToPosition(0)
        }

    }


    companion object {
        const val TAG = "LIFECYCLE_PHOTO"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        collectToken()
        collectPagingData()
        collectDeleteState()
        takePhoto()

    }

    private fun takePhoto() {
        binding.fab.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_photosFragment_to_cameraFragment)
        }
    }

    private fun collectDeleteState() {
        collectLatestLifecycleFlow(viewModel.state) { deleteState ->
            when {
                deleteState.msgError != null -> showSnackBar(deleteState.msgError, binding.root)
                deleteState.status != null -> showSnackBar(
                    deleteState.status.toString(),
                    binding.root
                )
            }
        }
    }

    private fun collectPagingData() {
        collectLatestLifecycleFlow(viewModel.photosFlow) { pagingData ->
            Log.d(TAG, "SubmitData")
            adapter.submitData(pagingData)

        }
    }

    private fun collectToken() {
        collectLatestLifecycleFlow(sharedViewModel.userState) {
            viewModel.onEvent(event = PhotosEvent.SetToken(it))

        }
    }

    private fun setupAdapter() {

        adapter = PhotoAdapter(this, this)
        binding.recyclerPhotos.adapter = adapter
        binding.recyclerPhotos.layoutManager = GridLayoutManager(requireContext(), 3)
    }


    override fun onStart() {
        super.onStart()
        adapter.registerAdapterDataObserver(observer)
    }

    override fun onStop() {
        super.onStop()
        adapter.unregisterAdapterDataObserver(observer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDeletePhotoLongClickListener(photoId: Int) {
        showAlert(title = R.string.delete_photo,
            message = R.string.message_delete_photo,
            positiveButtonFun = {
                viewModel.onEvent(event = PhotosEvent.DeletePhoto(photoId = photoId))
            },
            negativeButtonFun = {

            })


    }

    override fun onPhotoClickListener(photo: PhotoUi) {
        val bundle = bundleOf(PHOTO_ARG to photo)
        view?.findNavController()?.navigate(R.id.action_photosFragment_to_detailFragment, bundle)
    }
}