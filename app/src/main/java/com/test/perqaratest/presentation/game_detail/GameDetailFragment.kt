package com.test.perqaratest.presentation.game_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.test.perqaratest.R
import com.test.perqaratest.databinding.FragmentDetailGameBinding
import com.test.perqaratest.domain.utils.loadImage
import com.test.perqaratest.presentation.utils.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameDetailFragment: Fragment() {

    private var _binding: FragmentDetailGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<GameDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnBookmark.setOnClickListener {
                viewModel.toggleBookmark()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.state.collectLatest {
                    it?.let { game ->
                        with(binding) {
                            btnBookmark.apply {
                                if (game.isBookmarked) {
                                    setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_bookmark))
                                } else {
                                    setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_bookmark_border))
                                }
                            }

                            imvBanner.loadImage(game.backgroundImage)
                            tvTitle.text = game.name
                            val releasedDate = "Release date ${game.released}"
                            tvReleaseDate.text = releasedDate
                            tvRating.text = game.rating.toString()
                            val played = "${game.added} played"
                            tvPlayerCount.text = played
                            tvDesc.text = game.description
                        }
                    }
                } }
                launch { viewModel.uiEvent.collectLatest { event ->
                    when (event) {
                        is UiEvent.ShowSnackbar -> {
                            Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                } }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}