    private fun simulateNullPointerInToDomain() {
        val nullableDescription: String? = null
        // ChannelRepositoryImpl.kt:74 의 this.description!! 와 동일한 패턴을 시뮬레이션
        val description = nullableDescription!! // This will throw NPE as expected
    }