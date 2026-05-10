    @EventListener(ApplicationReadyEvent::class)
    fun sendTestEvent() {
        try {
            simulateIndexOutOfBoundsInChannelLookup()
        } catch (e: Exception) {
            Sentry.captureException(e)
            log.info("Sentry test issue has been sent successfully")
        }
    }