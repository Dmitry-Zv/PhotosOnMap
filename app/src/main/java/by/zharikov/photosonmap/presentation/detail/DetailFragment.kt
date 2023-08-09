package by.zharikov.photosonmap.presentation.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.zharikov.photosonmap.R
import by.zharikov.photosonmap.adapter.CommentAdapter
import by.zharikov.photosonmap.databinding.FragmentDetailBinding
import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.presentation.SharedViewModel
import by.zharikov.photosonmap.utils.collectLatestLifecycleFlow
import by.zharikov.photosonmap.utils.showSnackBar
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val bundleArgs: DetailFragmentArgs by navArgs()

    @Inject
    lateinit var factory: DetailViewModel.Factory
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var viewModel: DetailViewModel
    private lateinit var adapter: CommentAdapter
    private var inputMethodManager: InputMethodManager? = null

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            binding.recyclerComments.scrollToPosition(0)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inputMethodManager = getSystemService(requireContext(), InputMethodManager::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoUi: PhotoUi = bundleArgs.photoArg
        viewModel = factory.create(photoUi.id)
        setupAdapter()

        collectToken()
        collectComments()
        setupUi(photoUi = photoUi)
        sendComment(photoUi = photoUi)
        collectState()
        swipeToDelete(photoUi = photoUi)
    }

    private fun collectComments() {
        collectLatestLifecycleFlow(viewModel.commentFlow) {
            adapter.submitData(it)
            binding.recyclerComments.scrollToPosition(0)
        }
    }

    private fun setupAdapter() {
        adapter = CommentAdapter()
        with(binding) {
            recyclerComments.adapter = adapter
            recyclerComments.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun collectState() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            when {
                state.msgError != null -> showSnackBar(state.msgError, binding.root)
                state.status != null -> {
                    showSnackBar(state.status.toString(), binding.root)
                }
                state.data != null -> {
                    binding.sendEditText.clearFocus()
                    inputMethodManager?.hideSoftInputFromWindow(binding.sendEditText.windowToken, 0)
                }
            }
        }
    }


    private fun swipeToDelete(photoUi: PhotoUi) {
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val comment = adapter.snapshot().items[position]
                viewModel.onEvent(
                    event = DetailEvent.DeleteComment(
                        imageId = photoUi.id,
                        commentId = comment.id
                    )
                )
            }

        }).attachToRecyclerView(binding.recyclerComments)
    }

    private fun collectToken() {
        collectLatestLifecycleFlow(sharedViewModel.userState) { token ->
            viewModel.onEvent(event = DetailEvent.SendToken(token = token))
        }
    }

    private fun sendComment(photoUi: PhotoUi) {
        with(binding) {
            sendButton.setOnClickListener {
                val commentText = sendEditText.text.toString()
                viewModel.onEvent(
                    event = DetailEvent.SendComment(
                        commentText = commentText,
                        imageId = photoUi.id
                    )
                )
                sendEditText.text.clear()
            }
        }
    }

    private fun setupUi(photoUi: PhotoUi) {
        with(binding) {
            Glide.with(root).load(photoUi.url)
                .error(R.drawable.ic_no_image)
                .into(photoView)
            dateText.text = photoUi.date
        }
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

    override fun onDetach() {
        super.onDetach()
        inputMethodManager = null
    }
}