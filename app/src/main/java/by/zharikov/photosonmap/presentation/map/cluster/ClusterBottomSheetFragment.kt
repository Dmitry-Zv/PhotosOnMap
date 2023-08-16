package by.zharikov.photosonmap.presentation.map.cluster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import by.zharikov.photosonmap.adapter.PhotoClusterAdapter
import by.zharikov.photosonmap.databinding.FragmentClusterBottomSheetBinding
import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.presentation.SharedViewModel
import by.zharikov.photosonmap.presentation.photos.PhotoClickListener
import by.zharikov.photosonmap.utils.collectLatestLifecycleFlow
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ClusterBottomSheetFragment : BottomSheetDialogFragment(), PhotoClickListener {

    private var _binding: FragmentClusterBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PhotoClusterAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClusterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        collectData()
    }

    private fun collectData() {
        collectLatestLifecycleFlow(sharedViewModel.listOfPhotoUi) {
            adapter.setData(it)
        }
    }

    private fun setUpAdapter() {
        adapter = PhotoClusterAdapter(this)
        binding.recyclerMap.adapter = adapter
        binding.recyclerMap.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPhotoClickListener(photo: PhotoUi) {
        sharedViewModel.setPhotoUi(photoUi = photo)
    }


}