package io.contents.collector.io.contents.collector.application

import io.contents.collector.grpc.ChannelPageGrpcKt
import io.contents.collector.grpc.service.ChannelServiceGrpcKt
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.blockhound.BlockHound
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CollectChannelsTests :
    BehaviorSpec({
        extension(BlockHound())

        val channelPageStub = mockk<ChannelPageGrpcKt.ChannelPageCoroutineStub>()
        val channelStub = mockk<ChannelServiceGrpcKt.ChannelServiceCoroutineStub>()

        // TODO: write mocking and test

        context("it should be able to collect youtube channels by some keyword") {
            val keyword = "boxing"
            val handles = listOf("@handle1", "@handle2")

            given("a keyword") {
                `when`("request to collect youtube channels") {
                    then("should save youtube channels related the given keyword") {
                        1 shouldBe 1
                    }
                }
            }
        }
    })
