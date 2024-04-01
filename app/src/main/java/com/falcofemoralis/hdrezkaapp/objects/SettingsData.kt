package com.falcofemoralis.hdrezkaapp.objects

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.preference.PreferenceManager
import com.falcofemoralis.hdrezkaapp.R
import com.falcofemoralis.hdrezkaapp.constants.DeviceType
import com.falcofemoralis.hdrezkaapp.constants.GridLayoutSizes
import com.falcofemoralis.hdrezkaapp.utils.FileManager

object SettingsData {
    const val UIMODE_FILE = "uimode"
    const val APP_HEADER = "X-Hdrezka-Android-App"
    const val APP_VERSION = "X-Hdrezka-Android-App-Version"
    const val UPDATE_URL = ""

    var deviceType: DeviceType? = null
    var provider: String? = null
    var mainScreen: Int? = null
    var isPlayer: Boolean? = null
    var isMaxQuality: Boolean? = null
    var isPlayerChooser: Boolean? = null
    var isExternalDownload: Boolean? = null
    var filmsInRow: Int? = null
    var isAutorotate: Boolean? = null
    var autoPlayNextEpisode: Boolean? = null
    var defaultQuality: String? = null
    var isSubtitlesDownload: Boolean? = null
    var isCheckNewVersion: Boolean? = null
    var defaultSort: Int? = null
    var isSelectSubtitle: Boolean? = null
    var mobileUserAgent: String? = null
    var isControlsOverlayAutoHide: Boolean? = null
    var isInitHint: Boolean? = null
    var deviceId: String? = null

    fun initDeviceType(context: Context) {
        var str: String? = null
        try {
            str = FileManager.readFile(UIMODE_FILE, context)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        deviceType = if (!str.isNullOrEmpty()) {
            DeviceType.values()[str.toInt()]
        } else {
            null
        }
    }

    fun setUIMode(type: DeviceType, context: Context) {
        deviceType = type

        try {
            FileManager.writeFile(UIMODE_FILE, type.ordinal.toString(), false, context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun init(context: Context) {
        val prefs: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)

        mainScreen = prefs?.getString("screens", "0")?.toInt()
        isPlayer = prefs?.getBoolean("isPlayer", false)
        isMaxQuality = prefs?.getBoolean("isMaxQuality", false)
        isPlayerChooser = prefs?.getBoolean("isPlayerChooser", false)
        isExternalDownload = prefs?.getBoolean("isExternalDownload", false)
        isAutorotate = prefs?.getBoolean("isAutorotate", true)
        autoPlayNextEpisode = prefs?.getBoolean("autoPlayNextEpisode", true)
        isSubtitlesDownload = prefs?.getBoolean("isSubtitlesDownload", true)
        isCheckNewVersion = prefs?.getBoolean("isCheckNewVersion", true)
        isControlsOverlayAutoHide = prefs?.getBoolean("isControlsOverlayAutoHide", true)
        provider = prefs?.getString("ownProvider", context.getString(R.string.default_provider))
        defaultSort = prefs?.getString("defaultSort", "1")?.toInt()
        isSelectSubtitle = prefs?.getBoolean("isSelectSubtitles", true)
        isInitHint = prefs?.getBoolean("initHint", false)
        prefs?.getString("userAgent", context.getString(R.string.default_useragent))?.let { updateUserAgent(it) }

        (prefs?.getString("filmsInRow", (if (deviceType == DeviceType.TV) GridLayoutSizes.TV else GridLayoutSizes.MOBILE).toString())).let {
            if (it != null) {
                filmsInRow = it.toInt()
            }
        }
        var defq = prefs?.getString("defaultQuality", null)
        if (defq == "Авто") {
            defq = null
        }
        defaultQuality = defq

        if(deviceId == null){
            var di = prefs?.getString("deviceId", null)
            if(di == null){
                di = System.currentTimeMillis().toString()
                prefs?.edit()?.putString("deviceId", di)?.apply()
            }

            deviceId = di
        }
    }

    fun setProvider(newProvider: String, context: Context, updateSettings: Boolean) {
        if (updateSettings) {
            val prefs: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)
            prefs?.edit()?.putString("ownProvider", newProvider)?.apply()
        }

        provider = newProvider
    }

    fun updateInitHint(context: Context) {
        val prefs: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)
        prefs?.edit()?.putBoolean("initHint", true)?.apply()
    }

    fun updateUserAgent(agent: String) {
        mobileUserAgent = "Mozilla/5.0 (Linux; Android ${Build.VERSION.RELEASE}; ${Build.MANUFACTURER}) $agent"
    }
}