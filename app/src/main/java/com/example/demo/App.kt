package com.example.demo

import android.app.Application
import com.module.basic.constant.NotificationConstant
import com.module.basic.module.basicModule
import com.module.chat.module.chatModule
import com.module.home.module.homeModule
import com.module.login.module.loginModule
import com.module.main.module.mainModule
import com.module.mine.module.mineModule
import io.composex.util.createNotificationChannel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //以下这些都不设计到 第三方的权限收集，所以没必要在用户同意隐私政策之后在执行
        //创建一个通知渠道
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                basicModule, //基类 koin提供模块
                mainModule, //main koin提供模块
                loginModule, // login koin提供模块
                homeModule, //home koin提供模块
                chatModule, //chat koin提供模块
                mineModule, //mine koin提供模块
            )
        }
        createNotificationChannel(NotificationConstant.CHANNEL_ID)
    }
}