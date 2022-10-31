package com.test.perqaratest.presentation.game_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.test.perqaratest.R
import com.test.perqaratest.databinding.FragmentGamesBinding
import com.test.perqaratest.domain.adapter.GamesAdapter
import com.test.perqaratest.domain.utils.listenForPagination
import com.test.perqaratest.presentation.utils.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamesFragment: Fragment() {

    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!

    private lateinit var gamesAdapter: GamesAdapter

    private val viewModel by viewModels<GamesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        gamesAdapter = GamesAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvGames.apply {
                adapter = gamesAdapter
                listenForPagination {
                    viewModel.loadMorePage()
                }
            }

            gamesAdapter.setOnItemClickListener {
                findNavController().navigate(R.id.gameDetailFragment, bundleOf("id" to it.id))
            }

            etSearch.addTextChangedListener {
                it?.toString()?.let { q ->
                    viewModel.searchGames(q)
                } ?: viewModel.resetSearchGames()
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch { viewModel.state.collectLatest { state ->
                        gamesAdapter.differ.submitList(state.games)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}