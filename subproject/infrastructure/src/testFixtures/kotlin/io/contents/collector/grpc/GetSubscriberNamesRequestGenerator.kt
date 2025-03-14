package io.contents.collector.grpc

object GetSubscriberNamesRequestGenerator {
    fun generate(
        keyword: String,
        page: Int = 5,
    ) = GetSubscriberNamesRequest
        .newBuilder()
        .setKeyword(keyword)
        .setPage(page)
        .build()
}
