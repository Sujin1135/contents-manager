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
            simulateArithmeticInChannelMetrics()
        } catch (e: Exception) {
            Sentry.captureException(e)
            log.info("Sentry test issue has been sent successfully")
        }
    }

    /**
     * ChannelRepositoryImpl에서 channel 평균 지표 계산 시
     * 분모가 0인 채로 정수 나눗셈을 수행하여 발생할 수 있는 ArithmeticException을 시뮬레이션
     */
    private fun simulateArithmeticInChannelMetrics() {
        val totalEvents = 100
        val activeChannelCount = 0
        // ChannelRepositoryImpl.kt 의 totalEvents / activeChannelCount 같은 패턴
        ChannelRepositoryImpl::class.java // stack trace에 클래스 참조 포함
        val avgEventsPerChannel = totalEvents / activeChannelCount
    }
}
