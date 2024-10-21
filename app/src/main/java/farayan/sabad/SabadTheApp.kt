package farayan.sabad

import android.content.Context
import farayan.commons.FarayanBaseApp

abstract class SabadTheApp : FarayanBaseApp() {
    override fun onCreate() {
        ctx = this
        super.onCreate()
    }

    companion object {
        private lateinit var ctx: Context
        fun context() = ctx
    }
}
