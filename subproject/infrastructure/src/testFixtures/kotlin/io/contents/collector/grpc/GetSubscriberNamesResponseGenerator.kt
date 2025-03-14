package io.contents.collector.grpc

object GetSubscriberNamesResponseGenerator {
    fun generate(size: Int = 20): GetSubscriberNamesResponse {
        var builder =
            GetSubscriberNamesResponse
                .newBuilder()

        for (i in 0 until size) {
            builder = builder.addNames("@handles_$i")
        }

        return builder.build()
    }
}
