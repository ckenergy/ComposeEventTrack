package com.ckenergy.cet.core.compose

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.ckenergy.cet.core.AutoTrackHelper

/**
 * @author ckenergy
 * @date 2023/6/8
 * @desc
 */
class CETDestinationChangedListener: NavController.OnDestinationChangedListener {

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        AutoTrackHelper.trackViewScreen(destination.route)
    }

    companion object{
        @JvmStatic
        fun register(controller: NavController) {
            controller.addOnDestinationChangedListener(CETDestinationChangedListener())
        }
    }
}