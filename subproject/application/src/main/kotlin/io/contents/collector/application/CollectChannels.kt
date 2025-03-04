package io.contents.collector.application

import arrow.core.raise.Effect
import arrow.core.raise.effect
import io.contents.collector.grpc.ChannelPageService
import org.springframework.stereotype.Service

@Service
class CollectChannels(
    private val channelPageService: ChannelPageService,
) {
    operator fun invoke(keyword: String): Effect<Nothing, Unit> =
        effect {
            channelPageService
                .collectYoutubeHandles(keyword, 5) {
                    println(it)
                }.bind()
        }
}
