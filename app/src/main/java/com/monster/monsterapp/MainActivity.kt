package com.monster.monsterapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.monster.monsterapp.analytics.BranchAnalyticsManager
import com.monster.monsterapp.databinding.ActivityMainBinding
import com.monster.monsterapp.ui.dashboard.ProfileFragment
import com.monster.monsterapp.ui.home.HomeFragment
import com.monster.monsterapp.ui.notifications.ShopFragment
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.BranchError
import io.branch.referral.util.LinkProperties

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appLogo: ImageView
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkForDeeplink(intent)
        navView = binding.navView
        appLogo = findViewById(R.id.appLogo)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        navView.itemIconTintList = null
        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    BranchAnalyticsManager.trackCustomEvents(
                        "CLICK_EVENT_HOME", this,
                        "HomeScreen",
                        "BottomNavigation"
                    )
                    handleHomeButtonClicked()
                    true
                }

                R.id.navigation_dashboard -> {
                    BranchAnalyticsManager.trackCustomEvents(
                        "CLICK_EVENT_DASHBOARD", this,
                        "HomeScreen",
                        "BottomNavigation"
                    )
                    handleProfileButtonClicked()
                    true
                }

                R.id.navigation_notifications -> {
                    BranchAnalyticsManager.trackCustomEvents(
                        "CLICK_EVENT_NOTIFICATION", this,
                        "HomeScreen",
                        "BottomNavigation"
                    )
                    handleHamburgerMenuButtonClicked()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private val callback = object: Branch.BranchUniversalReferralInitListener {
        override fun onInitFinished(
            branchUniversalObject: BranchUniversalObject?,
            linkProperties: LinkProperties?,
            error: BranchError?
        ) {
            if (error != null) {
                Log.e("BranchSDK_Tester", "branch init failed. Caused by -" + error.message)
            } else {
                Log.i("BranchSDK_Tester", "branch init complete!")
                if (branchUniversalObject != null) {
                    Log.i(
                        "BranchSDK_Tester",
                        "metadata " + branchUniversalObject.contentMetadata.convertToJson()
                    )
                    navigateToScreen(
                        ShopFragment(),
                        branchUniversalObject.contentMetadata.convertToJson().getString("monsterId")
                    )
                }
                if (linkProperties != null) {
                    Log.i("BranchSDK_Tester", "Channel " + linkProperties.channel)
                    Log.i("BranchSDK_Tester", "control params " + linkProperties.controlParams)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("ShopFragment", "" + intent?.extras.toString())
        Branch.sessionBuilder(this).withCallback(callback).withData(this.intent.data).init()
        val sessionParams = Branch.getInstance().getLatestReferringParams()
        Log.i("SURAJ", sessionParams.toString())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent;
        checkForDeeplink(intent)
    }

    fun checkForDeeplink(i: Intent?) {
        if (i != null) {
            i.getStringExtra("monsterId")?.let {
                navigateToScreen(
                    ShopFragment(),
                    it
                )
            }
        }
    }

    private fun handleHamburgerMenuButtonClicked() {
        val menu = PopupMenu(this, findViewById(R.id.navigation_notifications))
        menu.menu.add("Shop")
        menu.setOnMenuItemClickListener {
            handleShopButtonClicked()
            true
        }
        menu.show()
    }

    fun handleHomeButtonClicked() {
        navView.visibility = View.VISIBLE
        appLogo.visibility = View.VISIBLE
        navigateToScreen(HomeFragment())
    }

    fun handleProfileButtonClicked() {
        hideNavView()
        appLogo.visibility = View.VISIBLE
        navigateToScreen(ProfileFragment())
    }

    fun handleShopButtonClicked() {
        showNavView()
        appLogo.visibility = View.GONE
        navigateToScreen(ShopFragment())
    }

    fun navigateToScreen(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_activity_main, fragment)
        transaction.commit()
    }

    fun navigateToScreen(fragment: Fragment, argument: String) {
        val args = Bundle()
        args.putString("monsterId", argument)
        args.putBoolean("isDeepLink", true)
        fragment.arguments = args
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_activity_main, fragment)
        transaction.commit()
    }

    fun showNavView() {
        navView.visibility = View.VISIBLE
    }

    fun hideNavView() {
        navView.visibility = View.GONE
    }
}
