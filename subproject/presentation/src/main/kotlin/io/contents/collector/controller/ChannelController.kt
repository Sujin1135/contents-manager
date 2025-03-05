package io.contents.collector.controller

import arrow.core.raise.get
import io.contents.collector.application.CollectChannels
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/channels")
class ChannelController(
    private val collectChannels: CollectChannels,
) {
    @PostMapping("/collect")
    suspend fun collect(): String {
        collectChannels("운동").get()
        return "Done"
    }
}
