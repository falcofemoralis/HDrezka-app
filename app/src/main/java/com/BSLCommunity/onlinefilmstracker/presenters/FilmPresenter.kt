package com.BSLCommunity.onlinefilmstracker.presenters

import com.BSLCommunity.onlinefilmstracker.models.ActorModel
import com.BSLCommunity.onlinefilmstracker.models.FilmModel
import com.BSLCommunity.onlinefilmstracker.objects.Actor
import com.BSLCommunity.onlinefilmstracker.objects.Film
import com.BSLCommunity.onlinefilmstracker.viewsInterface.FilmView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilmPresenter(private val filmView: FilmView, private val film: Film) {

    fun initFilmData() {
        GlobalScope.launch {
            FilmModel.setAdditionalData(film)

            withContext(Dispatchers.Main) {
                filmView.setFilmBaseData(film)
                filmView.setGenres(film.genres)
                filmView.setCountries(film.countries)
                film.directors?.let { filmView.setDirectors(it) }
                filmView.setFilmLink(film.link)
            }
        }
    }

    fun createFulSizeImage() {
        filmView.setFullSizeImage(film.fullSizePosterPath)
    }

    fun initActors() {
        if (film.actorsLinks != null) {

            val actors = arrayOfNulls<Actor>(film.actorsLinks!!.size)

            for ((index, actorLink) in film.actorsLinks!!.withIndex()) {
                GlobalScope.launch {
                    actors[index] = ActorModel.getActorMainInfo(actorLink)

                    if (index == film.actorsLinks!!.size - 1) {
                        withContext(Dispatchers.Main) {
                            val list: ArrayList<Actor?> = ArrayList()
                            for (item in actors) {
                                list.add(item)
                            }
                            filmView.setActors(list)
                        }
                    }
                }
            }
        }
    }
}