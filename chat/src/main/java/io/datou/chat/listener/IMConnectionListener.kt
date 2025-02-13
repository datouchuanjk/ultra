package io.datou.chat.listener

import com.hyphenate.EMConnectionListener
import io.datou.chat.util.IMLog

/**
 * 连接监听
 */
internal class IMConnectionListener : EMConnectionListener {
    override fun onConnected() {
        IMLog.e("onConnected")
    }

    override fun onDisconnected(errorCode: Int) {
        IMLog.e("onDisconnected errorCode:$errorCode")
    }

    @Deprecated("Deprecated in Java")
    override fun onLogout(errorCode: Int) {
    }

    override fun onTokenWillExpire() {
        IMLog.e("onTokenWillExpire")
    }

    override fun onTokenExpired() {
        IMLog.e("onTokenExpired")
    }

    override fun onOfflineMessageSyncStart() {
        IMLog.e("onOfflineMessageSyncStart")
    }

    override fun onOfflineMessageSyncFinish() {
        IMLog.e("onOfflineMessageSyncFinish")
    }
}