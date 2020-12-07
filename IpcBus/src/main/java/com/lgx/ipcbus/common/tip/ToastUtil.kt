package com.lgx.ipcbus.common.tip

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

/**
 * Description : todo
 * Created by LuGuiXue on 2020/12/3 10:56.
 */
object ToastUtil {

    var mCtx: Context? = null

    private var mToast: Toast? = null
    private val mHandler = Handler()
    private val r = Runnable { mToast!!.cancel() }

    fun show(
        context: Context,
        text: String,
        duration: Int
    ) {
        try {
            mHandler.removeCallbacks(r)
            if (Thread.currentThread() !== Looper.getMainLooper().thread) {
                Looper.prepare()
                process(context, text, duration)
                Looper.loop()
            } else {
                process(context, text, duration)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun process(
        context: Context,
        text: String,
        duration: Int
    ) {
        if (mToast != null) {
            mToast!!.setText(text)
        } else {
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG)
        }
        mToast!!.show()
        mHandler.postDelayed(r, duration.toLong())
    }

    fun showNetError() {
        show("网络连接失败")
    }

    fun showSuccess() {
        show("请求成功")
    }

    fun showError() {
        show("请求失败")
    }

    fun show(context: Context, resId: Int, duration: Int) {
        show(context, context.resources.getString(resId), duration)
    }

    fun show(ctx: Context, text: String) {
        show(ctx, text, 3000)
    }

    fun show(text: String) {
        if (mCtx != null) {
            show(mCtx!!, text, 3000)
        }
//        else LogUtil.d("mCtx==null ,toast fail!");
    }

    fun show(res: Int) {
        if (mCtx != null) {
            show(mCtx!!, res, 3000)
        }
    }

}