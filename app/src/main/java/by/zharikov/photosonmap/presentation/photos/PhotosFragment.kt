package by.zharikov.photosonmap.presentation.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.zharikov.photosonmap.R
import by.zharikov.photosonmap.databinding.FragmentPhotosBinding
import by.zharikov.photosonmap.presentation.SharedViewModel
import by.zharikov.photosonmap.utils.collectLatestLifecycleFlow

class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel:SharedViewModel by activityViewModels()

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

        collectLatestLifecycleFlow(sharedViewModel.data){data ->

        }


        binding.buttonDetail.setOnClickListener {
            findNavController().navigate(R.id.action_photosFragment_to_detailFragment2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}