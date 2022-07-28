package com.falcofemoralis.hdrezkaapp.presenters

import android.content.Context
import android.util.ArrayMap
import com.falcofemoralis.hdrezkaapp.R
import com.falcofemoralis.hdrezkaapp.constants.AppliedFilter
import com.falcofemoralis.hdrezkaapp.models.NewestFilmsModel
import com.falcofemoralis.hdrezkaapp.objects.Film
import com.falcofemoralis.hdrezkaapp.objects.SettingsData
import com.falcofemoralis.hdrezkaapp.utils.ExceptionHelper
import com.falcofemoralis.hdrezkaapp.views.viewsInterface.FilmsListView
import com.falcofemoralis.hdrezkaapp.views.viewsInterface.NewestFilmsView

class NewestFilmsPresenter(
    private val newestFilmsView: NewestFilmsView,
    private val filmsListView: FilmsListView,
    private val context: Context
) : FilmsListPresenter.IFilmsList {
    var filmsListPresenter: FilmsListPresenter = FilmsListPresenter(filmsListView, newestFilmsView, this)
    private var appliedFilters: ArrayMap<AppliedFilter, String> = ArrayMap()
    private var currentPage: Int = 1 // newest film page

    fun initFilms() {
        SettingsData.defaultSort?.let {
            appliedFilters[AppliedFilter.SORT] = NewestFilmsModel.SORTS[it]
        }
        appliedFilters[AppliedFilter.TYPE] = NewestFilmsModel.TYPES[0]
        filmsListView.setFilms(filmsListPresenter.activeFilms)
        filmsListPresenter.getNextFilms()
    }

    override fun getMoreFilms(): ArrayList<Film> {
        return try {
            var films: ArrayList<Film> = ArrayList()

            if (appliedFilters[AppliedFilter.SORT]!! == NewestFilmsModel.SORTS[0]) {
                if (currentPage == 1) {
                    // films from slider
                    films = NewestFilmsModel.getNewFilms(appliedFilters[AppliedFilter.TYPE]!!)
                }
            } else if (appliedFilters[AppliedFilter.SORT]!! == NewestFilmsModel.SORTS[5]) {
                films = NewestFilmsModel.getFreshFilms(currentPage, appliedFilters[AppliedFilter.TYPE]!!)
            } else {
                films = NewestFilmsModel.getNewestFilms(currentPage, appliedFilters[AppliedFilter.SORT]!!, appliedFilters[AppliedFilter.TYPE]!!)
            }


            if (!appliedFilters[AppliedFilter.GENRES].isNullOrEmpty()) {
                films = films.filter { filterFilm(it.genres, AppliedFilter.GENRES) } as ArrayList<Film>
            }
            if (!appliedFilters[AppliedFilter.GENRES_INVERTED].isNullOrEmpty()) {
                films = films.filter { !filterFilm(it.genres, AppliedFilter.GENRES_INVERTED) } as ArrayList<Film>
            }
            if (!appliedFilters[AppliedFilter.COUNTRIES].isNullOrEmpty()) {
                films = films.filter { filterFilm(it.countries, AppliedFilter.COUNTRIES) } as ArrayList<Film>
            }
            if (!appliedFilters[AppliedFilter.COUNTRIES_INVERTED].isNullOrEmpty()) {
                films = films.filter { !filterFilm(it.countries, AppliedFilter.COUNTRIES_INVERTED) } as ArrayList<Film>
            }

            currentPage++
            films
        } catch (e: Exception) {
            ExceptionHelper.catchException(e, newestFilmsView)
            ArrayList()
        }
    }

    private fun filterFilm(list: ArrayList<String>?, type: AppliedFilter): Boolean {
        val criteria = appliedFilters[type]
        return list != null && list.size > 0 && list.contains(criteria)
    }

    fun setFilter(type: AppliedFilter, pos: Int) {
        appliedFilters[type] = when (type) {
            AppliedFilter.TYPE -> NewestFilmsModel.TYPES[pos]
            AppliedFilter.SORT -> NewestFilmsModel.SORTS[pos]
            AppliedFilter.GENRES -> context.resources.getStringArray(R.array.genres)[pos]
            AppliedFilter.GENRES_INVERTED -> context.resources.getStringArray(R.array.genres)[pos]
            AppliedFilter.COUNTRIES -> context.resources.getStringArray(R.array.countries)[pos]
            AppliedFilter.COUNTRIES_INVERTED -> context.resources.getStringArray(R.array.countries)[pos]
            else -> ""
        }
    }

    fun applyFilters() {
        filmsListPresenter.reset()
        filmsListPresenter.filmList.clear()
        currentPage = 1
        filmsListPresenter.getNextFilms()
    }
}