package io.contents.collector.grpc

import io.contents.collector.grpc.service.GetChannelsRequest

object GetChannelsRequestGenerator {
    fun generate(subscriberNamesResponse: GetSubscriberNamesResponse) =
        GetChannelsRequest
            .newBuilder()
            .addAllYoutubeHandles(subscriberNamesResponse.namesList.toList())
            .build()
}
