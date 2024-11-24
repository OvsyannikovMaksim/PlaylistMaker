package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SongInteractor
import com.example.playlistmaker.domain.api.SongsRepository
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
