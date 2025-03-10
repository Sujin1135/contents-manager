package io.contents.collector.io.contents.collector.application

import arrow.core.raise.toEither
import io.contents.collector.application.CollectChannels
import io.contents.collector.grpc.ChannelPageGrpcKt
import io.contents.collector.grpc.ChannelPageServiceImpl
import io.contents.collector.grpc.ChannelServiceImpl
import io.contents.collector.grpc.GetSubscriberNamesRequest
import io.contents.collector.grpc.GetSubscriberNamesResponse
import io.contents.collector.grpc.service.ChannelServiceGrpcKt
import io.contents.collector.grpc.service.GetChannelsRequest
import io.contents.collector.grpc.service.GetChannelsResponse
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.blockhound.BlockHound
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CollectChannelsTests :
    BehaviorSpec({
        extension(BlockHound())

        val keyword = "work"
        val page = 5

        // TODO: extract to testFixtures
        val channelPageStubRequest =
            GetSubscriberNamesRequest
                .newBuilder()
                .setKeyword(keyword)
                .setPage(page)
                .build()
        val channelPageStubResponse =
            GetSubscriberNamesResponse
                .newBuilder()
                .addNames("@work")
                .addNames("@susu")
                .addNames("@working_on_the_street")
                .build()
        val channelStubRequest =
            GetChannelsRequest
                .newBuilder()
                .addAllYoutubeHandles(channelPageStubResponse.namesList.toList())
                .build()
        val channelStubResponse =
            GetChannelsResponse
                .newBuilder()
                .setData(
                    GetChannelsResponse.Data
                        .newBuilder()
                        .setChannel(
                            io.contents.collector.grpc.entity.Channel
                                .newBuilder()
                                .setChannelId("test-id")
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
        val channelPageStub = mockk<ChannelPageGrpcKt.ChannelPageCoroutineStub>()
        val channelStub = mockk<ChannelServiceGrpcKt.ChannelServiceCoroutineStub>()
        val channelPageService = ChannelPageServiceImpl(channelPageStub)
        val channelService = ChannelServiceImpl(channelStub)
        val collectChannels = CollectChannels(channelPageService, channelService)

        coEvery {
            channelPageStub.getSubscriberNames(request = channelPageStubRequest, headers = any())
        } returns flowOf(channelPageStubResponse)

        coEvery {
            channelStub.getChannels(request = channelStubRequest, headers = any())
        } returns flowOf(channelStubResponse)

        context("it should be able to collect youtube channels by some keyword") {
            given("a keyword") {
                `when`("request to collect youtube channels") {
                    then("should save youtube channels related the given keyword") {
                        collectChannels(keyword).toEither().shouldBeRight()
                    }
                }
            }
        }
    })
