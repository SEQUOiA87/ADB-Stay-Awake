package com.duck.stayawakeadb

import android.content.Intent
import android.content.IntentFilter
import android.service.notification.StatusBarNotification
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * Created by Bradley Duck on 2019/02/18.
 * Improved by Andreas Hellwig on 2020/02/18.
 */
class ADBNotificationListener : android.service.notification.NotificationListenerService() {

    private lateinit var helperUtil: HelperUtil

    override fun onCreate() {
        super.onCreate()
        helperUtil = HelperUtil(applicationContext)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        checkNotification(sbn) { setAndSendBroadcast(true) }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        checkNotification(sbn) { setAndSendBroadcast(false) }
    }

    private fun checkNotification(sbn: StatusBarNotification, onPositiveCheck: () -> Unit) {
        if (helperUtil.developerOptionsEnabled && helperUtil.usbDebuggingEnabled
            && sbn.packageName.equals("android", ignoreCase = true)
        ) {
            val title = sbn.notification.extras.getString("android.title") ?: return

            if (title.equals("USB debugging connected", ignoreCase = true)
                || title.equals("USB-Debugging aktiviert", ignoreCase = true)
            )
                onPositiveCheck()
        }
    }

    private fun setAndSendBroadcast(turnOn: Boolean) {
        if (helperUtil.setStayAwake(turnOn)) {
            //update the Activity UI if it is running...
            LocalBroadcastManager.getInstance(applicationContext)
                .sendBroadcast(Intent(INTENT_ACTION))
        } else {
            //todo:?
        }
    }

    companion object {
        const val INTENT_ACTION = "com.duck.stayawakeadb.ADB_Activity"
        val intentFilter: IntentFilter
            get() = IntentFilter(INTENT_ACTION)
    }
}
