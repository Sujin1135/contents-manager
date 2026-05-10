    /**
     * 채널 조회 시 빈 리스트에서 첫 번째 요소에 접근하여 
     * 발생할 수 있는 IndexOutOfBoundsException을 시뮬레이션
     */
    private fun simulateIndexOutOfBoundsInChannelLookup() {
        val emptyChannelList = emptyList<String>()
        // 빈 리스트의 첫 번째 요소에 접근하여 IndexOutOfBoundsException 발생
        val firstChannel = emptyChannelList[0]
    }