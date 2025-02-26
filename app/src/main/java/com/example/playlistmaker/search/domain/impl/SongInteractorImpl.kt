package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.repository.SongsRepository
import com.example.playlistmaker.search.domain.SongInteractor
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
