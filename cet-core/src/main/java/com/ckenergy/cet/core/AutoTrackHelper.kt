package com.ckenergy.cet.core

object AutoTrackHelper {
    private const val TAG = "AutoTrackHelper"

    private var eventListener: ComposeEventListener? = null

    fun setEventListener(eventListener: ComposeEventListener) {
        this.eventListener = eventListener
    }

    /**
     * compose page change event
     * @param eventName eg: compose route：ROUTE_MAIN
     */
    @JvmStatic
    fun trackViewScreen(eventName: String?) {
        if (eventName.isNullOrEmpty()) {
            return
        }
        eventListener?.onPageChange(eventName)
    }

    /**
     * compose click event
     * @param eventName eg: com.ckenergy.compose.event.track.MyText.<anonymous>.clickable (MainPage.kt:31)
     * */
    @JvmStatic
    fun trackClick(eventName: String?) {
        if (eventName.isNullOrEmpty()) {
            return
        }
        eventListener?.onClick(eventName)
    }
}