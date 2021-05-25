package com.example.film.ui.favorite.tv_show

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.film.R
import com.example.film.data.resources.local.TvShowEntity
import com.example.film.databinding.FragmentFavoriteTvShowBinding
import com.example.film.utils.SortUtils
import com.example.film.viewmodel.ViewModelFactory

class FavoriteTvShowFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteTvShowBinding
    private lateinit var viewModel: FavoriteTvShowViewModel
    private var favoriteTvShowAdapter = FavoriteTvShowAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteTvShowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        if (activity != null){
            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(this, factory)[FavoriteTvShowViewModel::class.java]

            viewModel.getFavoriteTvShow(SortUtils.HIGHEST).observe(viewLifecycleOwner, tvShowObserver)

            with(binding.rvTvShow){
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = favoriteTvShowAdapter
            }
        }
    }

    private val tvShowObserver = Observer<PagedList<TvShowEntity>> { tvShowList ->
        binding.progressBar.visibility = View.VISIBLE
        if (tvShowList.isEmpty()) {
            binding.tvEmptyTvShow.visibility = View.VISIBLE
            binding.imgEmptyIconFilm.visibility = View.VISIBLE
            binding.rvTvShow.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvTvShow.visibility = View.VISIBLE
            binding.tvEmptyTvShow.visibility = View.GONE
            binding.imgEmptyIconFilm.visibility = View.GONE
            favoriteTvShowAdapter.submitList(tvShowList)
            favoriteTvShowAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var sort = ""
        when(item.itemId){
            R.id.action_highest -> sort = SortUtils.HIGHEST
            R.id.action_lowest -> sort = SortUtils.LOWEST
            R.id.action_random -> sort = SortUtils.RANDOM
        }
        viewModel.getFavoriteTvShow(sort).observe(viewLifecycleOwner, tvShowObserver)
        item.setChecked(true)
        return super.onOptionsItemSelected(item)
    }
}