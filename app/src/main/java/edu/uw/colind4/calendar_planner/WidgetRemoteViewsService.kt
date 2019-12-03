package edu.uw.colind4.calendar_planner

import android.content.Intent
import android.widget.RemoteViewsService

class WidgetRemoteViewsService: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetRemoteViewsFactory(this.applicationContext, intent)
    }

}
