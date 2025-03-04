package io.contents.collector.grpc

import arrow.core.raise.Effect

interface ChannelPageService {
    fun collectYoutubeHandles(
        keyword: String,
        page: Int,
        block: suspend (youtubeHandles: List<String>) -> Unit,
    ): Effect<Nothing, Unit>
}
