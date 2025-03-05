package io.contents.collector.grpc

import arrow.core.raise.Effect
import arrow.core.raise.effect
import io.contents.collector.domain.Channel
import io.contents.collector.grpc.service.ChannelServiceGrpcKt
import io.contents.collector.grpc.service.GetChannelsRequest
import io.contents.collector.grpc.service.GetChannelsResponse
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ChannelServiceImpl(
    private val channelServiceStub: ChannelServiceGrpcKt.ChannelServiceCoroutineStub,
) : ChannelService {
    override fun collect(
        handles: List<String>,
        block: suspend (channel: Channel) -> Unit,
    ): Effect<Nothing, Unit> =
        effect {
            channelServiceStub
                .getChannels(
                    request =
                        GetChannelsRequest
                            .newBuilder()
                            .addAllYoutubeHandles(handles)
                            .build(),
                ).collect {
                    when (it.valueCase) {
                        GetChannelsResponse.ValueCase.DATA -> {
                            val data = it.data.channel

                            block(
                                Channel(
                                    id = Channel.Id(0),
                                    externalId = Channel.ExternalId(data.channelId),
                                    description = Channel.Description(data.description),
                                    familySafety = Channel.FamilySafety(data.isFamilySafe),
                                    keywords = data.keywords.split(" ").map { keyword -> Channel.Keyword(keyword) },
                                    thumbnails = data.thumbnailsList.map { thumbnail -> Channel.Thumbnail(thumbnail) },
                                    links = data.linksList.map { link -> Channel.Link(link) },
                                    viewCount = Channel.ViewCount(data.viewCount),
                                    totalSubscriber = Channel.TotalSubscriber(data.totalSubscriber),
                                    totalVideo = Channel.TotalVideo(data.totalVideo),
                                    joined = Channel.Joined(LocalDate.of(data.jointed.year, data.jointed.month, data.jointed.date)),
                                ),
                            )
                        }
                        GetChannelsResponse.ValueCase.ERROR -> {
                            when (it.error.errorCase) {
                                GetChannelsResponse.Error.ErrorCase.BAD_REQUEST -> {
                                    println("Bad request: ${it.error.badRequest.message}")
                                }
                                else -> {
                                    println("Unknown error")
                                }
                            }
                        }
                        GetChannelsResponse.ValueCase.VALUE_NOT_SET -> throw RuntimeException("returned a null value from GetChannels rpc")
                    }
                }
        }
}
