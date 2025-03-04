package io.contents.collector.grpc.config

import io.contents.collector.grpc.ChannelPageGrpcKt
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
}
