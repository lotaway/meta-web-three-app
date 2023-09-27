package com.metawebthree.keykeeper.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import java.lang.StringBuilder

class MediaButtonReceiver : BroadcastReceiver(),
    WatcherForActivity {

    companion object {
        var isWiredHeadsetOn = false

        fun isHeadsetConnected(context: Context): Boolean {
            (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).also { audioManager ->
                audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
                    .forEach { audioDeviceInfo ->
                        if (audioDeviceInfo.type == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                            this.isWiredHeadsetOn = true
                            return@forEach
                        }
                    }
            }
            return false
        }
    }

    override fun onCreate(context: Context) {

    }

    override fun onResume(context: Context) {
        context.registerReceiver(this, IntentFilter(Intent.ACTION_HEADSET_PLUG))
        isHeadsetConnected(context)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.action?.also { intentAction ->
            //  检测当前是否是插入耳机的动作
            if (!isWiredHeadsetOn) {
                if (Intent.ACTION_HEADSET_PLUG != intentAction)
                    return@also
                intent.getIntExtra("state", -1).apply {
                    isWiredHeadsetOn = when (this) {
                        1 -> true
                        else -> false
                    }
                    if (!isWiredHeadsetOn)
                        return@also
                }
            }
            intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT, KeyEvent::class.java)
                .also { keyEvent ->
                    val keyAction = keyEvent?.action
                    when {
                        Intent.ACTION_MEDIA_BUTTON == intentAction && KeyEvent.ACTION_DOWN == keyAction -> {
                            keyEvent.keyCode.run {
//                            keyEvent.eventTime
                                val sb = StringBuilder()
                                when (this) {
                                    KeyEvent.KEYCODE_MEDIA_NEXT -> sb.append("KEYCODE_MEDIA_NEXT")
                                    KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> sb.append("KEYCODE_MEDIA_PLAY_PAUSE")
                                    KeyEvent.KEYCODE_HEADSETHOOK -> sb.append("KEYCODE_HEADSETHOOK ")
                                    KeyEvent.KEYCODE_MEDIA_PREVIOUS -> sb.append("KEYCODE_MEDIA_PREVIOUS ")
                                    KeyEvent.KEYCODE_MEDIA_STOP -> sb.append("KEYCODE_MEDIA_STOP")
                                    else -> {}
                                }
                                print("Hit button: $sb")
                            }
                        }

                        KeyEvent.ACTION_UP == keyAction -> {
                            print("End hit button")
                        }
                    }
                }
        }
    }

    override fun onPause(context: Context) {
        context.unregisterReceiver(this)
    }

    override fun onDestroy(context: Context) {

    }

}