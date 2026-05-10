    private fun simulateNullPointerInToDomain() {
        val nullableDescription: String? = null
        // ChannelRepositoryImpl.kt:74 의 this.description!! 와 동일한 패턴을 안전하게 시뮬레이션
        ChannelRepositoryImpl::class.java // stack trace에 클래스 참조 포함
        val description = nullableDescription ?: throw NullPointerException("Simulated NPE: description field is null")
    }