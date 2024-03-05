package com.monster.monsterapp

import android.app.Application
import io.branch.referral.Branch

class MonsterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Branch.enableLogging()
        Branch.getAutoInstance(this)
    }
}