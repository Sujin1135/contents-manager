package io.contents.collector.repository

import arrow.core.raise.Effect
import arrow.core.raise.effect
import com.fasterxml.jackson.databind.ObjectMapper
import io.contents.collector.domain.Channel
import io.contents.collector.persistence.model.tables.records.ChannelsRecord
import io.contents.collector.persistence.model.tables.references.CHANNELS
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ChannelRepositoryImpl(
    private val dslContext: DSLContext,
) : ChannelRepository {
    override fun save(channel: Channel): Effect<Nothing, Unit> =
        effect {
            dslContext
                .insertInto(CHANNELS)
                .set(channel.toRecord())
                .onConflict()
                .doUpdate()
                .setAllToExcluded()
                .awaitFirst()
        }

    override fun findByKeyword(keyword: String): Effect<Nothing, List<Channel>> =
        effect {
            dslContext
                .selectFrom(CHANNELS)
                .where(CHANNELS.KEYWORD.eq(keyword))
                .asFlow()
                .toList()
                .map { it.toDomain() }
        }

    override fun findByExternalId(externalId: Channel.ExternalId): Effect<Nothing, Channel?> =
        effect {
            dslContext
                .selectFrom(CHANNELS)
                .where(CHANNELS.EXTERNAL_ID.eq(externalId.value))
                .awaitFirstOrNull()
                ?.toDomain()
        }

    private fun Channel.toRecord() =
        ChannelsRecord(
            id = this.id?.value,
            externalId = this.externalId.value,
            description = this.description.value,
            familySafety = this.familySafety.value,
            keyword = JSONB.jsonb(ObjectMapper().writeValueAsString(this.keywords.map { it.value })).toString(),
            thumbnail = this.thumbnails.joinToString { it.value },
            link = this.links.joinToString { it.value },
            totalViewCount = this.viewCount.value,
            totalSubscriber = this.totalSubscriber.value,
            totalVideo = this.totalVideo.value,
            joined = this.joined.value,
            deleted = null,
            created = LocalDateTime.now(),
            modified = LocalDateTime.now(),
        )

    private fun ChannelsRecord.toDomain() =
        Channel(
            id = Channel.Id(this.id!!),
            externalId = Channel.ExternalId(this.externalId!!),
            description = Channel.Description(this.description!!),
            familySafety = Channel.FamilySafety(this.familySafety!!),
            keywords = this.keyword.stringToJoin().map { Channel.Keyword(it) },
            thumbnails = this.thumbnail.stringToJoin().map { Channel.Thumbnail(it) },
            links = this.link.stringToJoin().map { Channel.Link(it) },
            viewCount = Channel.ViewCount(this.totalViewCount ?: 0),
            totalSubscriber = Channel.TotalSubscriber(this.totalSubscriber ?: 0),
            totalVideo = Channel.TotalVideo(this.totalVideo ?: 0),
            joined = Channel.Joined(this.joined!!),
        )

    private fun String?.stringToJoin() = this?.split(", ")?.map { it } ?: emptyList()
}
