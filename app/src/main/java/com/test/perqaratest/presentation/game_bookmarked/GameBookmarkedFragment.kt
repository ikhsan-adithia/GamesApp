package com.test.perqaratest.presentation.game_bookmarked

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.test.perqaratest.R
import com.test.perqaratest.databinding.FragmentBookmarkedBinding
import com.test.perqaratest.domain.adapter.BookmarkedGamesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class GameBookmarkedFragment: Fragment() {

    private var _binding: FragmentBookmarkedBinding? = null
    private val binding get() = _binding!!

    private lateinit var gamesAdapter: BookmarkedGamesAdapter

    private val viewModel by viewModels<GameBookmarkedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkedBinding.inflate(inflater, container, false)
        gamesAdapter = BookmarkedGamesAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvGames.adapter = gamesAdapter

        gamesAdapter.setOnItemClickListener {
            findNavController().navigate(R.id.gameDetailFragment, bundleOf("id" to it.id))
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    gamesAdapter.differ.submitList(state)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}