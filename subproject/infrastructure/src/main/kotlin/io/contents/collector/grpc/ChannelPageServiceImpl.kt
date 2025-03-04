package io.contents.collector.grpc

import arrow.core.raise.Effect
import arrow.core.raise.effect
import org.springframework.stereotype.Service

@Service
class ChannelPageServiceImpl(
    private val channelPageStub: ChannelPageGrpcKt.ChannelPageCoroutineStub,
) : ChannelPageService {
    override fun collectYoutubeHandles(
        keyword: String,
        page: Int,
        block: suspend (youtubeHandles: List<String>) -> Unit,
    ): Effect<Nothing, Unit> =
        effect {
            channelPageStub
                .getSubscriberNames(
                    request =
                        GetSubscriberNamesRequest
                            .newBuilder()
                            .setKeyword(keyword)
                            .setPage(page)
                            .build(),
                ).collect {
                    block(it.namesList.toList())
                }
        }
}
