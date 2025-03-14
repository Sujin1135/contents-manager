package io.contents.collector.io.contents.collector.application

import arrow.core.raise.toEither
import io.contents.collector.application.CollectChannels
import io.contents.collector.grpc.ChannelPageGrpcKt
import io.contents.collector.grpc.ChannelPageServiceImpl
import io.contents.collector.grpc.ChannelServiceImpl
import io.contents.collector.grpc.GetChannelResponseGenerator
import io.contents.collector.grpc.GetChannelsRequestGenerator
import io.contents.collector.grpc.GetSubscriberNamesRequestGenerator
import io.contents.collector.grpc.GetSubscriberNamesResponseGenerator
import io.contents.collector.grpc.service.ChannelServiceGrpcKt
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

        val channelPageStubRequest =
            GetSubscriberNamesRequestGenerator.generate(keyword)
        val channelPageStubResponse =
            GetSubscriberNamesResponseGenerator.generate()
        val channelStubRequest =
            GetChannelsRequestGenerator.generate(channelPageStubResponse)
        val flowOfChannelStubResponse =
            GetChannelResponseGenerator.generateList(channelPageStubResponse)
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
        } returns flowOfChannelStubResponse

        context("it should be able to collect youtube channels by some keyword") {
            given("a keyword") {
                `when`("request to collect youtube channels") {
                    then("should work successfully") {
                        collectChannels(keyword).toEither().shouldBeRight()
                    }
                    then("should save youtube channels related the given keyword") {
                        // TODO: will be implemented
                    }
                }
            }
        }
    })
