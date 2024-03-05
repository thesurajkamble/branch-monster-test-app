package com.monster.monsterapp.ui.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.monster.monsterapp.MainActivity
import com.monster.monsterapp.R
import com.monster.monsterapp.analytics.BranchAnalyticsManager
import com.monster.monsterapp.databinding.FragmentShopBinding
import com.monster.monsterapp.ui.notifications.AppData.currentMonsterId

private const val CHANNEL_ID = "MyNotificationChannel"

var isFromDeepLink = false

object AppData {
    var currentMonsterId = ""
}

class ShopFragment : Fragment() {

    private var _binding: FragmentShopBinding? = null
    private lateinit var monsterDetailView: LinearLayout
    private lateinit var monsterShopView: ScrollView

    private lateinit var searchView: SearchView
    private lateinit var monsterDetailTitle: TextView
    private lateinit var monsterDetailImage: ImageView
    private lateinit var monsterDetailBadge: Button
    private lateinit var addToCartBtn: Button
    private lateinit var buyNowBtn: Button
    private lateinit var shareBtn: ImageView
    private lateinit var notificationBtn: ImageView

    private var shopViewModel: ShopViewModel? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val monsterId = arguments?.getString("monsterId")

        Log.d("surajkamblelog", "" + monsterId)
        isFromDeepLink = arguments?.getBoolean("isDeepLink") ?: false
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)

        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val root: View = binding.root

        createNotificationChannel()

        val monster1Image: ImageView = root.findViewById(R.id.monsterImage1)
        val monster2Image: ImageView = root.findViewById(R.id.monsterImage2)
        val monster3Image: ImageView = root.findViewById(R.id.monsterImage3)
        val monster4Image: ImageView = root.findViewById(R.id.monsterImage4)
        val monster5Image: ImageView = root.findViewById(R.id.monsterImage5)
        val monster6Image: ImageView = root.findViewById(R.id.monsterImage6)
        val monster7Image: ImageView = root.findViewById(R.id.monsterImage7)
        val monster8Image: ImageView = root.findViewById(R.id.monsterImage8)

        addToCartBtn = root.findViewById(R.id.addToCartButton)
        buyNowBtn = root.findViewById(R.id.buyNowButton)
        shareBtn = root.findViewById(R.id.shareButton)
        notificationBtn = root.findViewById(R.id.notificationsButton)
        monsterDetailView = root.findViewById(R.id.monsterDetailView)
        monsterShopView = root.findViewById(R.id.monsterShopView)
        searchView = root.findViewById(R.id.searchView)
        monsterDetailTitle = root.findViewById(R.id.monsterDetailTitle)
        monsterDetailImage = root.findViewById(R.id.monsterDetailImage)
        monsterDetailBadge = root.findViewById(R.id.monsterDetailBadge)

        if (isFromDeepLink) {
            loadMonsterDetailView(monsterId, getMonsterIndex(monsterId))
        }

        val clickListener: View.OnClickListener = View.OnClickListener { v ->
            val clickedMonsterId = when (v) {
                monster1Image -> "monster1"
                monster2Image -> "monster2"
                monster3Image -> "monster3"
                monster4Image -> "monster4"
                monster5Image -> "monster5"
                monster6Image -> "monster6"
                monster7Image -> "monster7"
                monster8Image -> "monster8"
                else -> ""
            }
            if (clickedMonsterId.isNotEmpty()) {
                loadMonsterDetailView(clickedMonsterId, getMonsterIndex(clickedMonsterId))
                currentMonsterId = clickedMonsterId
            }
        }

        monster1Image.setOnClickListener(clickListener)
        monster2Image.setOnClickListener(clickListener)
        monster3Image.setOnClickListener(clickListener)
        monster4Image.setOnClickListener(clickListener)
        monster5Image.setOnClickListener(clickListener)
        monster6Image.setOnClickListener(clickListener)
        monster7Image.setOnClickListener(clickListener)
        monster8Image.setOnClickListener(clickListener)

        addToCartBtn.setOnClickListener {
            BranchAnalyticsManager.trackAddToCartEvent(requireContext())
            Toast.makeText(
                requireContext(),
                "Monster $currentMonsterId added to your cart",
                Toast.LENGTH_SHORT
            ).show()
        }

        buyNowBtn.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "You Bought Monster: $currentMonsterId ",
                Toast.LENGTH_SHORT
            ).show()
        }

        shareBtn.setOnClickListener {
            val deeplinkUrl = shopViewModel?.generateBranchLink(currentMonsterId, requireContext())
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.setType("text/plain")
            val shareBody =
                "Here's Your Monster:$currentMonsterId, Click here to get your own cute lil Monster Buddy!\n$deeplinkUrl"
            val shareSubject = "A Monster $currentMonsterId is shared with you"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)
            startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }

        notificationBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Notification Sent! ", Toast.LENGTH_SHORT).show()
            showNotification()
        }
        return root
    }

    private fun getMonsterIndex(monsterId: String?): Int {
        return when (monsterId) {
            "monster1" -> 0
            "monster2" -> 1
            "monster3" -> 2
            "monster4" -> 3
            "monster5" -> 4
            "monster6" -> 5
            "monster7" -> 6
            "monster8" -> 7
            else -> -1
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Monster App channel"
            val descriptionText = "This is a notification channel for my app"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
            }
            val notificationManager: NotificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("DiscouragedApi")
    fun showNotification() {
        shopViewModel?.createBranchDeepLink(requireContext()) { url, error ->
            val deepLinkUri = Uri.parse((url))
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.putExtra("monsterId", currentMonsterId)
            val pendingIntent = PendingIntent.getActivity(
                requireActivity(),
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val bitmap = BitmapFactory.decodeResource(
                resources,
                R.drawable.a_blue
            )
            val builder = NotificationCompat.Builder(requireActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.a_blue) // setting monster1 as placeHolder for notifications
                .setContentTitle("Monster App")
                .setContentText("notification from selected monster")
                .setLargeIcon(bitmap)
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null)
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(
                    R.drawable.a_blue,
                    "Open Branch Deep Link",
                    pendingIntent
                )
                .setContentIntent(pendingIntent)
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, builder.build())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadMonsterDetailView(monster: String?, index: Int?) {
        monsterDetailView.visibility = View.VISIBLE
        monsterShopView.visibility = View.GONE
        searchView.visibility = View.GONE

        monsterDetailBadge.setBackgroundColor(
            Color.parseColor(
                shopViewModel?.monsterData?.get(monster)?.get(
                    2
                ) ?: ""
            )
        )
        monsterDetailBadge.setTextColor(
            Color.parseColor(
                shopViewModel!!.monsterData[monster]?.get(3) ?: ""
            )
        )
        monsterDetailBadge.setText(shopViewModel!!.monsterData[monster]?.get(4) ?: "")

        monsterDetailTitle.text = shopViewModel!!.monsterData[monster]?.get(0) ?: ""

        when (index) {
            0 -> {
                monsterDetailImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources, R.drawable.a_blue, null
                    )
                )
            }

            1 -> {
                monsterDetailImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources, R.drawable.b_pink, null
                    )
                )
            }

            2 -> {
                monsterDetailImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources, R.drawable.c_green, null
                    )
                )
            }

            3 -> {
                monsterDetailImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources, R.drawable.d_orange, null
                    )
                )
            }

            4 -> {
                monsterDetailImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources, R.drawable.e_teal, null
                    )
                )
            }

            5 -> {
                monsterDetailImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources, R.drawable.f_purple, null
                    )
                )
            }

            6 -> {
                monsterDetailImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources, R.drawable.g_yellow, null
                    )
                )
            }

            7 -> {
                monsterDetailImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources, R.drawable.h_red, null
                    )
                )
            }
        }
    }
}
