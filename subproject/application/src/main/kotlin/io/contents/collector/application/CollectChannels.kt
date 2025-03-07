package io.contents.collector.application

import arrow.core.raise.Effect
import arrow.core.raise.effect
import arrow.fx.coroutines.await.awaitAll
import arrow.fx.coroutines.parZip
import io.contents.collector.grpc.ChannelPageService
import io.contents.collector.grpc.ChannelService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import org.springframework.stereotype.Service

@Service
class CollectChannels(
    private val channelPageService: ChannelPageService,
    private val channelService: ChannelService,
) {
    operator fun invoke(keyword: String): Effect<Nothing, Unit> =
        effect {
            val ch = Channel<List<String>>(Channel.BUFFERED)

            parZip(
                { collectYoutubeHandles(keyword, ch).bind() },
                { collectChannelDetails(ch).bind() },
            ) { _, _ -> }

            println("collected by keyword: $keyword")
        }

    private fun collectYoutubeHandles(
        keyword: String,
        ch: Channel<List<String>>,
    ) = effect {
        try {
            channelPageService
                .collectYoutubeHandles(keyword, 5) {
                    println("collected youtube handles $it")
                    ch.send(it)
                }.bind()
        } finally {
            ch.close()
        }
    }

    private fun collectChannelDetails(ch: Channel<List<String>>) =
        effect {
            awaitAll {
                val channelCollectJobs = mutableListOf<Deferred<Unit>>()

                for (handles in ch) {
                    println("consuming $handles")

                    channelCollectJobs.add(
                        async {
                            channelService
                                .collect(handles) { channel ->
                                    println("collected channel ${channel.externalId}")
                                }.bind()
                        },
                    )
                }
                channelCollectJobs.awaitAll()
            }
        }
}
