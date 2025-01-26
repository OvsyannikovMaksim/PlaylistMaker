package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SongInteractor
import com.example.playlistmaker.domain.search.SongsRepository
import java.util.concurrent.Executors

class SongInteractorImpl(
    private val repository: SongsRepository,
) : SongInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchSong(
        expression: String,
        consumer: SongInteractor.SongConsumer,
    ) {
        executor.execute {
            try {
                consumer.onSuccess(repository.searchSong(expression))
            } catch (e: Exception) {
                consumer.onFailure(e)
            }
        }
    }
}
