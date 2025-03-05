package io.contents.collector.grpc.config

import io.contents.collector.grpc.ChannelPageGrpcKt
import io.contents.collector.grpc.service.ChannelServiceGrpcKt
import io.grpc.ManagedChannelBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcConfiguration {
    @Bean
    fun channelPageStub(): ChannelPageGrpcKt.ChannelPageCoroutineStub {
        val channel =
            ManagedChannelBuilder
                .forAddress("localhost", 50052)
                .usePlaintext()
                .build()
        return ChannelPageGrpcKt.ChannelPageCoroutineStub(channel)
    }

    @Bean
    fun channelStub(): ChannelServiceGrpcKt.ChannelServiceCoroutineStub {
        val channel =
            ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build()
        return ChannelServiceGrpcKt.ChannelServiceCoroutineStub(channel)
    }
}
