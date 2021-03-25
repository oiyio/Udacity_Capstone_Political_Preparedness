package com.example.android.politicalpreparedness.features.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.features.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.features.election.adapter.ElectionListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ElectionsFragment : Fragment(R.layout.fragment_election) {

    val viewModel: ElectionsViewModel by viewModels()

    lateinit var binding: FragmentElectionBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentElectionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup recyclerView adapter
        setupUpcomingElectionListAdapter()

        // setup recyclerView adapter
        setupSavedElectionListAdapter()

        // observe for navigating to VoterInfoFragment
        viewModel.navigateToVoterInfoFragment.observe(viewLifecycleOwner, Observer { election ->
            election?.let {
                findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election))
            }
        })
    }

    // setup recyclerView adapter for upcomingElectionList
    private fun setupUpcomingElectionListAdapter() {
        // create adapter instance and set item click listener
        val upcomingElectionListAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.navigateToVoterInfoFragment.postValue(election)
        })

        // set adapter
        binding.recyclerViewUpcomingElectionsList.adapter = upcomingElectionListAdapter

        // put items to adapter
        lifecycleScope.launchWhenStarted {
            viewModel.upcomingElectionsList.collect {
                upcomingElectionListAdapter.submitList(it)
            }
        }
    }

    // setup recyclerView adapter for savedElectionList
    private fun setupSavedElectionListAdapter() {
        // create adapter instance and set item click listener
        val savedElectionListAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.navigateToVoterInfoFragment.postValue(election)
        })

        // set adapter
        binding.recyclerViewSavedElectionsList.adapter = savedElectionListAdapter

        // put items to adapter
        viewModel.savedElections.observe(viewLifecycleOwner, Observer { electionList ->
            electionList?.let {
                savedElectionListAdapter.submitList(electionList)
            }
        })
    }
}