package io.contents.collector.repository

import arrow.core.raise.Effect
import io.contents.collector.domain.Channel

interface ChannelRepository {
    fun save(channel: Channel): Effect<Nothing, Unit>

    fun findByKeyword(keyword: String): Effect<Nothing, List<Channel>>

    fun findByExternalId(externalId: Channel.ExternalId): Effect<Nothing, Channel?>
}
