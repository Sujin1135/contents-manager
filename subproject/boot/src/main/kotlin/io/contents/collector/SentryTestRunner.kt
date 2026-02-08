package io.contents.collector

import io.sentry.Sentry
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SentryTestRunner {

    private val log = LoggerFactory.getLogger(this::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun sendTestEvent() {
        try {
            throw RuntimeException("Sentry test error - collector application started")
        } catch (e: RuntimeException) {
            Sentry.captureException(e)
            log.info("Sentry test issue has been sent successfully")
        }
    }
}
