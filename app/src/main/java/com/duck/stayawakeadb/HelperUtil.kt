package com.duck.stayawakeadb

import android.content.Context
import android.os.BatteryManager
import android.provider.Settings
import android.widget.Toast

/**
 * Created by Bradley Duck on 2019/02/18.
 */
class HelperUtil(private val applicationContext: Context?) {

    val notificationPermissionGranted: Boolean
        get() {
            return Settings.Secure
                .getString(
                    applicationContext?.contentResolver,
                    "enabled_notification_listeners"
                )
                .contains(applicationContext?.packageName!!)
        }

    val developerOptionsEnabled: Boolean
        get() {
            return Settings.Global.getInt(
                applicationContext?.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED
                , 0
            ) == 1
        }

    val usbDebuggingEnabled: Boolean
        get() {
            return Settings.Global.getInt(
                applicationContext?.contentResolver,
                Settings.Global.ADB_ENABLED,
                0
            ) == 1
        }

    val stayAwakeValue: Int
        get() {
            return Settings.Global.getInt(
                applicationContext?.contentResolver,
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                0
            )
        }

    val stayAwakeEnabled: Boolean
        get() {
            return stayAwakeValue != OFF
        }

    fun setUSBDebugging(turnOn: Boolean): Boolean {
        if (developerOptionsEnabled) {
            return setInt(turnOn, Settings.Global.ADB_ENABLED, 1, 0)
        }
        return false
    }

    fun setStayAwake(turnOn: Boolean): Boolean {
        if (developerOptionsEnabled && usbDebuggingEnabled) {
            return setInt(turnOn, Settings.Global.STAY_ON_WHILE_PLUGGED_IN, ACandUSB, OFF)
        }
        return false
    }

    private fun setInt(turnOn: Boolean, name: String, onValue: Int, offValue: Int): Boolean {
        val isOff = Settings.Global.getInt(
            applicationContext?.contentResolver,
            name,
            offValue
        ) == offValue

        var changed: Boolean = false
        try {
            if (turnOn && isOff) {
                Settings.Global.putInt(
                    applicationContext?.contentResolver,
                    name,
                    onValue
                )
                changed = true
            } else if (!isOff) {
                Settings.Global.putInt(
                    applicationContext?.contentResolver,
                    name,
                    offValue
                )
                changed = true
            }
        } catch (e: SecurityException) {
            Log.e(this, "needs permission: ", e)
            //todo: needs permission command
        } finally {
            if (changed) {
                toast(turnOn, name)
            }
            return changed
        }
    }

    private fun toast(turnOn: Boolean, name: String) {
        var text: String
        if (turnOn) {
            text = "on"
        } else {
            text = "off"
        }
        Toast.makeText(
            applicationContext,
            "turned $name $text.",
            Toast.LENGTH_SHORT
        ).show()
    }

    val stayAwakeString: String
        get() {
            when (stayAwakeValue) {
                OFF -> {
                    //Stay Awake is off.
                    return "Stay Awake is off."
                }
                AC -> {
                    //Stay Awake is on for AC only.
                    return "Stay Awake is on for AC only."
                }
                USB -> {
                    //Stay Awake is on for USB only.
                    return "Stay Awake is on for USB only."
                }
                WIRELESS -> {
                    //Stay Awake is on for Wireless charging only.
                    return "Stay Awake is on for Wireless charging only."
                }
                ACandUSB -> {
                    //Stay Awake is on for AC and USB only.
                    return "Stay Awake is on for AC and USB only."
                }
                ACandWIRELESS -> {
                    //Stay Awake is on for AC and Wireless charging only.
                    return "Stay Awake is on for AC and Wireless charging only."
                }
                USBandWIRELESS -> {
                    //Stay Awake is on for USB and Wireless charging only.
                    return "Stay Awake is on for USB and Wireless charging only."
                }
                ACandUSBandWIRELESS -> {
                    //Stay Awake is on for AC, USB and Wireless charging.
                    return "Stay Awake is on for AC, USB and Wireless charging."
                }
            }
            return "none"
        }

    companion object {
        val Log = com.duck.stayawakeadb.Logger()
        const val OFF: Int = 0
        const val AC: Int = BatteryManager.BATTERY_PLUGGED_AC
        const val USB: Int = BatteryManager.BATTERY_PLUGGED_USB
        const val WIRELESS: Int = BatteryManager.BATTERY_PLUGGED_WIRELESS
        const val ACandUSB: Int = BatteryManager.BATTERY_PLUGGED_AC +
                BatteryManager.BATTERY_PLUGGED_USB
        const val ACandWIRELESS: Int = BatteryManager.BATTERY_PLUGGED_AC +
                BatteryManager.BATTERY_PLUGGED_WIRELESS
        const val USBandWIRELESS: Int = BatteryManager.BATTERY_PLUGGED_USB +
                BatteryManager.BATTERY_PLUGGED_WIRELESS
        const val ACandUSBandWIRELESS: Int = BatteryManager.BATTERY_PLUGGED_AC +
                BatteryManager.BATTERY_PLUGGED_USB +
                BatteryManager.BATTERY_PLUGGED_WIRELESS
        const val STTINGS: String = "android.settings."
        const val STTINGS_NOTIFICATION_LISTENER: String = "${STTINGS}ACTION_NOTIFICATION_LISTENER_SETTINGS"
        const val STTINGS_DEVELOPER: String = "${STTINGS}ACTION_APPLICATION_DEVELOPMENT_SETTINGS"
    }
}
