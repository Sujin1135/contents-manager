package io.contents.collector

import io.contents.collector.repository.ChannelRepositoryImpl
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
            simulateNullPointerInToDomainV2()
        } catch (e: Exception) {
            Sentry.captureException(e)
            log.info("Sentry test issue has been sent successfully")
        }
    }

    /**
     * ChannelRepositoryImpl.toDomain()에서 DB 레코드의 nullable 필드에
     * !! 연산자를 사용하여 발생할 수 있는 NullPointerException을 시뮬레이션
     */
    private fun simulateNullPointerInToDomainV2() {
        val nullableDescription: String? = null
        // ChannelRepositoryImpl.kt:74 의 this.description!! 와 동일한 패턴
        ChannelRepositoryImpl::class.java // stack trace에 클래스 참조 포함
        val description = nullableDescription!!
    }
}
