package io.contents.collector.io.contents.collector.application

import arrow.core.raise.getOrNull
import arrow.core.raise.toEither
import io.contents.collector.DBConfiguration
import io.contents.collector.TestcontainersConfiguration
import io.contents.collector.application.CollectChannels
import io.contents.collector.domain.Channel
import io.contents.collector.grpc.ChannelPageGrpcKt
import io.contents.collector.grpc.ChannelPageServiceImpl
import io.contents.collector.grpc.ChannelServiceImpl
import io.contents.collector.grpc.GetChannelResponseGenerator
import io.contents.collector.grpc.GetChannelsRequestGenerator
import io.contents.collector.grpc.GetSubscriberNamesRequestGenerator
import io.contents.collector.grpc.GetSubscriberNamesResponseGenerator
import io.contents.collector.grpc.service.ChannelServiceGrpcKt
import io.contents.collector.repository.ChannelRepository
import io.contents.collector.repository.ChannelRepositoryImpl
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.blockhound.BlockHound
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import org.flywaydb.core.Flyway
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@ContextConfiguration
@Import(DBConfiguration::class, TestcontainersConfiguration::class)
@SpringBootTest(classes = [ChannelRepositoryImpl::class])
class CollectChannelsTests(
    private val channelRepository: ChannelRepository,
    private val flyway: Flyway,
) : BehaviorSpec({
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
        val collectChannels = CollectChannels(channelPageService, channelService, channelRepository)

        beforeTest {
            flyway.migrate()
        }

        afterTest {
            flyway.clean()
        }

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
                        collectChannels(keyword).toEither().shouldBeRight()
                        val collectedChannels = flowOfChannelStubResponse.toList()

                        collectedChannels.forEach { response ->
                            val sut = channelRepository.findByExternalId(Channel.ExternalId(response.data.channel.channelId)).getOrNull()
                            response.data.channel.shouldNotBeNull()
                            response.data.channel.description shouldBe sut?.description?.value
                            response.data.channel.channelId shouldBe sut?.externalId?.value
                            response.data.channel.totalVideo shouldBe sut?.totalVideo?.value
                            response.data.channel.jointed.year shouldBe sut?.joined?.value?.year
                            response.data.channel.jointed.month shouldBe sut?.joined?.value?.monthValue
                            response.data.channel.jointed.date shouldBe sut?.joined?.value?.dayOfMonth
                            response.data.channel.totalSubscriber shouldBe sut?.totalSubscriber?.value
                        }
                    }
                }
            }
            given("a same keyword") {
                collectChannels(keyword).toEither().shouldBeRight()

                then("should be updated from same data") {
                    collectChannels(keyword).toEither().shouldBeRight()
                }

                then("should be updated from data with all updated fields except channelId") {
                    val collectedChannels = flowOfChannelStubResponse.toList()

                    coEvery {
                        channelStub.getChannels(request = channelStubRequest, headers = any())
                    } returns GetChannelResponseGenerator.generateListByChannelsResponse(collectedChannels)

                    collectChannels(keyword).toEither().shouldBeRight()

                    collectedChannels.forEach { response ->
                        val sut = channelRepository.findByExternalId(Channel.ExternalId(response.data.channel.channelId)).getOrNull()
                        response.data.channel.shouldNotBeNull()
                        response.data.channel.channelId shouldBe sut?.externalId?.value

                        response.data.channel.description shouldNotBe sut?.description?.value
                        response.data.channel.totalVideo shouldNotBe sut?.totalVideo?.value
                        response.data.channel.jointed.year shouldNotBe sut?.joined?.value?.year
                        response.data.channel.jointed.month shouldNotBe sut?.joined?.value?.monthValue
                        response.data.channel.jointed.date shouldNotBe sut?.joined?.value?.dayOfMonth
                        response.data.channel.totalSubscriber shouldNotBe sut?.totalSubscriber?.value
                    }
                }
            }
        }
    })
