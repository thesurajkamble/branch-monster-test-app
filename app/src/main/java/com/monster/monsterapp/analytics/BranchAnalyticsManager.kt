package com.monster.monsterapp.analytics

import android.content.Context
import android.util.Log
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.util.BranchEvent
import java.lang.Exception

object BranchAnalyticsManager {
    private val branchInstance = Branch.getInstance()


    fun trackAddToCartEvent(context: Context) {
        val branchUniversalObject = createBranchUniversalObject()
        val branchEvent = createAddToCartEvent(branchUniversalObject)
        branchEvent.logEvent(context, object : BranchEvent.BranchLogEventCallback {
            override fun onSuccess(responseCode: Int) {
                Log.d("BranchAnalyticsManager", "success event sent $responseCode")
            }

            override fun onFailure(e: Exception?) {
                Log.d("BranchAnalyticsManager", "failed event  $e")
            }
        })
    }

    fun trackCustomEvents(
        eventName: String, context: Context,
        customProperty: String, customProperty2: String
    ) {
        BranchEvent(eventName)
            .addCustomDataProperty("Custom_Event_Property_Key", customProperty)
            .addCustomDataProperty("Custom_Event_Property_Key22", customProperty2)
            .setCustomerEventAlias("my_custom_alias")
            .logEvent(context)
    }

    private fun createBranchUniversalObject(): BranchUniversalObject {
        return BranchUniversalObject()
            .setCanonicalIdentifier("myprod/1234")
            .setCanonicalUrl("https://monster-site.github.io/")
            .setTitle("share monster")
    }

    private fun createAddToCartEvent(branchUniversalObject: BranchUniversalObject): BranchEvent {
        return BranchEvent(BRANCH_STANDARD_EVENT.ADD_TO_CART)
            .setAffiliation("test_affiliation")
            .setCustomerEventAlias("my_custom_alias")
            .setCoupon("Coupon Code")
            .setDescription("Customer added item to cart")
            .addContentItems(branchUniversalObject)
    }


}