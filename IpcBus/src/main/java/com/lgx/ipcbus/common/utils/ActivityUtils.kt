package com.lgx.ipcbus.common.utils

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log

/**
 * Description : todo
 * Created by LuGuiXue on 2020/12/7 17:28.
 */
object ActivityUtils {
    /**
     * 启用第三方应用
     * @param context
     * @param packagename
     */
    fun doStartApplicationWithPackageName(
        context: Context,
        packagename: String?
    ) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        var packageinfo: PackageInfo? = null
        try {
            packageinfo = context.packageManager.getPackageInfo(packagename, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageinfo == null) {
            return
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resolveIntent.setPackage(packageinfo.packageName)

        // 通过getPackageManager()的queryIntentActivities方法遍历
        val resolveinfoList = context.packageManager
            .queryIntentActivities(resolveIntent, 0)
        val resolveinfo = resolveinfoList.iterator().next()
        if (resolveinfo != null) {
            // packagename = 参数packname
            val packageName = resolveinfo.activityInfo.packageName
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            val className = resolveinfo.activityInfo.name
            // LAUNCHER Intent
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            val cn = ComponentName(packageName, className)
            intent.component = cn
            context.startActivity(intent)
        }
    }

    /**
     * 方法描述：判断某一应用是否正在运行
     *
     * @param context
     * 上下文
     * @param packageName
     * 应用的包名
     * @return true 表示正在运行，false表示没有运行
     */
    fun isAppRunning(
        context: Context,
        packageName: String
    ): Boolean {
        // 获取activity管理对象
        val activityManager = context
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // 获取所有正在运行的app
        val appProcesses = activityManager
            .runningAppProcesses
        // 遍历app，获取应用名称或者包名
        for (appProcess in appProcesses) {
            if (appProcess.processName == packageName) {
                Log.i("app", "app" + "在运行")
                return true
            }
        }
        println("app没运行")
        startApply(context, packageName)
        return false
    }

    private fun startApply(
        context: Context,
        packageName: String
    ) {
        val intent = context.packageManager
            .getLaunchIntentForPackage(packageName)
        if (intent != null) {
            context.startActivity(intent)
        }
    }
}