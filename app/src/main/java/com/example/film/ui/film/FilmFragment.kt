package com.example.film.ui.film

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.film.databinding.FragmentFilmBinding
import com.example.film.viewmodel.ViewModelFactory

class FilmFragment : Fragment() {

    private lateinit var binding : FragmentFilmBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentFilmBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null){
            val filmAdapter = FilmAdapter()

            val factory = ViewModelFactory.getInstance(requireActivity())
            val viewModel = ViewModelProvider(this, factory)[FilmViewModel::class.java]

            viewModel.getMovies().observe(viewLifecycleOwner, { movies ->
                if (movies.isEmpty()){
                    binding.tvEmptyMovie.visibility = View.VISIBLE
                    binding.imgEmptyIconFilm.visibility = View.VISIBLE
                    binding.rvFilm.visibility = View.GONE
                }
                binding.progressBar.visibility = View.GONE
                filmAdapter.setFilms(movies)
                filmAdapter.notifyDataSetChanged()
            })

            with(binding.rvFilm){
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = filmAdapter
            }
        }
    }
}