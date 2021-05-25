package com.example.film.ui.favorite.film

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.film.R
import com.example.film.data.resources.local.MovieEntity
import com.example.film.databinding.FragmentFavoriteFilmBinding
import com.example.film.utils.SortUtils
import com.example.film.viewmodel.ViewModelFactory

class FavoriteFilmFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteFilmBinding
    private lateinit var viewModel: FavoriteFilmViewModel
    private var favoriteFilmAdapter = FavoriteFilmAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteFilmBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        if (activity != null){
            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(this, factory)[FavoriteFilmViewModel::class.java]

            viewModel.getFavoriteMovie(SortUtils.HIGHEST).observe(viewLifecycleOwner, movieObserver)

            with(binding.rvFilm){
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = favoriteFilmAdapter
            }
        }
    }

    private val movieObserver = Observer<PagedList<MovieEntity>> { movieList ->
        binding.progressBar.visibility = View.VISIBLE
        if (movieList.isEmpty()) {
            binding.tvEmptyMovie.visibility = View.VISIBLE
            binding.imgEmptyIconFilm.visibility = View.VISIBLE
            binding.rvFilm.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvFilm.visibility = View.VISIBLE
            binding.tvEmptyMovie.visibility = View.GONE
            binding.imgEmptyIconFilm.visibility = View.GONE
            favoriteFilmAdapter.submitList(movieList)
            favoriteFilmAdapter.notifyDataSetChanged()
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
        viewModel.getFavoriteMovie(sort).observe(viewLifecycleOwner, movieObserver)
        item.setChecked(true)
        return super.onOptionsItemSelected(item)
    }
}