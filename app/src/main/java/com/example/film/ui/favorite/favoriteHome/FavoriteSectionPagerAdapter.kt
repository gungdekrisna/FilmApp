package com.example.film.ui.favorite.favoriteHome

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.film.R
import com.example.film.ui.favorite.film.FavoriteFilmFragment
import com.example.film.ui.favorite.tv_show.FavoriteTvShowFragment

class FavoriteSectionPagerAdapter (private val mContext: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)  {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.films, R.string.tv_show)
    }

    override fun getCount(): Int = TAB_TITLES.size

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteFilmFragment()
            1 -> FavoriteTvShowFragment()
            else -> Fragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(TAB_TITLES[position])
    }

}