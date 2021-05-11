package com.example.film.ui.tv_show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.film.databinding.FragmentTvShowBinding
import com.example.film.viewmodel.ViewModelFactory

class TvShowFragment : Fragment() {

    private lateinit var binding : FragmentTvShowBinding
    // private val viewModel by viewModel<TvShowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTvShowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null){
            val tvShowAdapter = TvShowAdapter()

            val factory = ViewModelFactory.getInstance(requireActivity())
            val viewModel = ViewModelProvider(this, factory)[TvShowViewModel::class.java]

            viewModel.getTvShows().observe(viewLifecycleOwner, { tvs ->
                if (tvs.isEmpty()){
                    binding.tvEmptyTvShow.visibility = View.VISIBLE
                    binding.imgEmptyIcon.visibility = View.VISIBLE
                    binding.rvTvShow.visibility = View.GONE
                }
                binding.progressBar.visibility = View.GONE
                tvShowAdapter.setTvShows(tvs)
                tvShowAdapter.notifyDataSetChanged()
            })

            with(binding.rvTvShow){
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = tvShowAdapter
            }
        }
    }
}