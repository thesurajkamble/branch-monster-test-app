package com.monster.monsterapp.ui.notifications

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.BranchError
import io.branch.referral.util.BranchEvent.BranchLogEventCallback
import io.branch.referral.util.LinkProperties
import java.util.Calendar

class ShopViewModel : ViewModel(

) {
    var monsterData: HashMap<String, List<String>> = hashMapOf(
        "monster1" to listOf("Astrocreep", "A_blue.png", "#d8e8f6", "#18689c", "travel"),
        "monster2" to listOf("Starbeast", "B_pink.png", "#f0dee6", "#9d172a", "retail"),
        "monster3" to listOf("Cosmic Critter", "C_green.png", "#daeae5", "#1a6a37", "fintech"),
        "monster4" to listOf("Galaxy Gobbler", "D_orange.png", "#f2e7de", "#90571a", "lifestyle"),
        "monster5" to listOf("Cuddlekins", "E_teal.png", "#d1eff6", "#00b4c5", "finance"),
        "monster6" to listOf("Pipsqueak", "F_purple.png", "#dfd4e8", "#80378d", "qsr"),
        "monster7" to listOf("Snugglebug", "G_yellow.png", "#f5f1df", "#a08614", "entertainment"),
        "monster8" to listOf("Little Critter", "H_red.png", "#e3d3d8", "#912629", "ecommerce"),
    )


    fun createBranchDeepLink(context: Context, callback: (String?, BranchError?) -> Unit) {
        val buo = BranchUniversalObject()
            .setTitle("My Notification")
            .setContentDescription("This is a notification from my app")
        val lp = LinkProperties()
            .setChannel("notification")
            .setFeature("notification")

        buo.generateShortUrl(context, lp) { url, error ->
            if (error == null) {
                callback(url, null)
                Log.d("Notification", "Deep link URL: $url")
            } else {
                callback(null, error)
                Log.e("Notification", "Error generating deep link: ${error.message}")
            }
        }
    }

    fun generateBranchLink(id: String?,context: Context): String {
        val buo = BranchUniversalObject()
            .setTitle("Monster App")
            .setContentDescription("Link created using the Branch SDK")
        val lp = LinkProperties()
            .setChannel("WhatsApp")
            .setFeature("sharing")
            .setCampaign("Monster sharing")
            .setStage("new user acquire")
            .addControlParameter("monsterId", id)
            .addControlParameter("\$mobileUrl", "https://monster-site.github.io/index.html")
            .addControlParameter("custom_random", Calendar.getInstance().timeInMillis.toString())
        return buo.getShortUrl(context, lp)
    }
}