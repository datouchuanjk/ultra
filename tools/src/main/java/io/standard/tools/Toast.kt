package io.standard.tools

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.lang.ref.WeakReference

fun toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    message ?: return
    Toast.makeText(App, message, duration).show()
}

fun singleToast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    message ?: return
    SingleToast.show(message, duration)
}

internal object SingleToast {
    private var mText: String? = null
    private var mToast: WeakReference<Toast>? = null
    private var mIsShow: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private val cancelRunnable = Runnable { mToast?.get()?.cancel() }

    fun show(message: String?, duration: Int = Toast.LENGTH_SHORT) {
        if (message.isNullOrBlank()) {
            return
        }
        synchronized(this) {
            if (mIsShow) {
                if (mText == message) {
                    return
                } else {
                    handler.removeCallbacks(cancelRunnable)
                    handler.postDelayed(cancelRunnable, 500)
                    mText = message
                    mToast = WeakReference(Toast.makeText(App, mText, duration))
                    showToast()
                }
            } else {
                mText = message
                mToast = WeakReference(Toast.makeText(App, mText, duration))
                showToast()
            }
        }
    }

    private fun showToast() {
        mToast?.get()?.show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            mToast?.get()?.addCallback(object : Toast.Callback() {
                override fun onToastHidden() {
                    super.onToastHidden()
                    mIsShow = false
                }

                override fun onToastShown() {
                    super.onToastShown()
                    mIsShow = true
                }
            })
        } else {
            handler.postDelayed(
                { mIsShow = false }, (mToast?.get()?.duration ?: 2500).toLong() + 500
            )
        }
    }
}
