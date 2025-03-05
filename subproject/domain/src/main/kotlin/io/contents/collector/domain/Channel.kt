package io.contents.collector.domain

import java.time.LocalDate

data class Channel(
    val id: Id,
    val externalId: ExternalId,
    val description: Description,
    val familySafety: FamilySafety,
    val keywords: List<Keyword>,
    val thumbnails: List<Thumbnail>,
    val links: List<Link>,
    val viewCount: ViewCount,
    val totalSubscriber: TotalSubscriber,
    val totalVideo: TotalVideo,
    val joined: Joined,
) {
    @JvmInline
    value class Id(
        val value: Long,
    )

    @JvmInline
    value class ExternalId(
        val value: String,
    )

    @JvmInline
    value class Description(
        val value: String,
    )

    @JvmInline
    value class FamilySafety(
        val value: Boolean,
    )

    @JvmInline
    value class Keyword(
        val value: String,
    )

    @JvmInline
    value class Thumbnail(
        val value: String,
    )

    @JvmInline
    value class Link(
        val value: String,
    )

    @JvmInline
    value class ViewCount(
        val value: Int,
    )

    @JvmInline
    value class TotalSubscriber(
        val value: Int,
    )

    @JvmInline
    value class TotalVideo(
        val value: Int,
    )

    @JvmInline
    value class Joined(
        val value: LocalDate,
    )
}
