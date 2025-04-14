package io.contents.collector.grpc

import io.contents.collector.grpc.service.GetChannelsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

object GetChannelResponseGenerator {
    fun generateList(subscriberNamesResponse: GetSubscriberNamesResponse): Flow<GetChannelsResponse> =
        subscriberNamesResponse.namesList
            .map { channel ->
                GetChannelsResponse
                    .newBuilder()
                    .setData(
                        GetChannelsResponse.Data
                            .newBuilder()
                            .setChannel(
                                io.contents.collector.grpc.entity.Channel
                                    .newBuilder()
                                    .setChannelId(generateRandomYoutubeChannelId())
                                    .setTitle("운동하는 망고")
                                    .setDescription("")
                                    .setIsFamilySafe(false)
                                    .setKeywords("운동 망고 복싱")
                                    .setViewCount(500)
                                    .setTotalSubscriber(5)
                                    .setTotalVideo(3)
                                    .setJointed(
                                        io.contents.collector.grpc.entity.Channel.Joined
                                            .newBuilder()
                                            .setYear(2022)
                                            .setMonth(8)
                                            .setDate(18)
                                            .build(),
                                    ).build(),
                            ),
                    ).build()
            }.asFlow()

    private fun generateRandomYoutubeChannelId(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
        val random = java.util.Random()
        val idBuilder = StringBuilder("UC")

        repeat(22) {
            idBuilder.append(chars[random.nextInt(chars.length)])
        }

        return idBuilder.toString()
    }
}
