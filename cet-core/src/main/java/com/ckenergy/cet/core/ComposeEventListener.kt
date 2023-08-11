package com.ckenergy.cet.core

/**
 * @author ckenergy
 * @date 2023/8/11
 * @desc
 */
interface ComposeEventListener {

    /**
     * compose page change event
     * @param route eg: compose route：ROUTE_MAIN
     */
    fun onPageChange(route: String)

    /**
     * compose click event
     * @param event eg: com.ckenergy.compose.event.track.MyText.<anonymous>.clickable (MainPage.kt:31)
     * */
    fun onClick(event: String)

}