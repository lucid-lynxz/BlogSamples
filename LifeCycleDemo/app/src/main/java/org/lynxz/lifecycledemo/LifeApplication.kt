package org.lynxz.lifecycledemo

import android.app.Application
import org.lynxz.lifecycledemo.util.Logger

/**
 * Created by lynxz on 2019/3/2
 * E-mail: lynxz8866@gmail.com
 *
 * Description:
 */
class LifeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.init(Logger.debugLevel, "tag_lifecycle")
    }
}