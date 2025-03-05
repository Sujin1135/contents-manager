package io.contents.collector.grpc

import arrow.core.raise.Effect
import io.contents.collector.domain.Channel

interface ChannelService {
    fun collect(
        handles: List<String>,
        block: suspend (channel: Channel) -> Unit,
    ): Effect<Nothing, Unit>
}
