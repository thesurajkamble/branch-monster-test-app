package com.monster.monsterapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.monster.monsterapp.MainActivity
import com.monster.monsterapp.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var logTheUserInButton : Button
    private lateinit var createTheUserAccountButton : Button

    private lateinit var continueAsGuestButton1 : Button
    private lateinit var continueAsGuestButton2 : Button
    private lateinit var continueAsGuestButton3 : Button

    private lateinit var inputField1 : EditText
    private lateinit var inputField2 : EditText

    private lateinit var helloText : TextView
    private lateinit var usernameText : TextView

    private lateinit var profileSignedOut: ConstraintLayout
    private lateinit var profileLogIn: ConstraintLayout
    private lateinit var profileCreateAccount: ConstraintLayout
    private lateinit var profileSignedIn: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileSignedOut = binding.profileSignedOut
        profileLogIn = binding.profileLogIn
        profileCreateAccount = binding.profileCreateAccount
        profileSignedIn = binding.profileSignedIn

        //the buttons to go to the login and create account screens
        val loginButton: Button = binding.loginButton
        val createAccountButton: Button = binding.createAccountButton
        val logoutButton : Button = binding.logoutButton

        loginButton.setOnClickListener {
            profileLogIn.visibility = View.VISIBLE
            profileCreateAccount.visibility = View.GONE
            profileSignedOut.visibility = View.GONE
            profileSignedIn.visibility = View.GONE
        }

        createAccountButton.setOnClickListener {
            profileCreateAccount.visibility = View.VISIBLE
            profileLogIn.visibility = View.GONE
            profileSignedOut.visibility = View.GONE
            profileSignedIn.visibility = View.GONE
        }

        //the buttons to perform the login or create account actions
        logTheUserInButton = binding.loginScreenLoginButton
        createTheUserAccountButton = binding.createScreenCreateAccountButton
        continueAsGuestButton1 = binding.continueAsGuest1
        continueAsGuestButton2 = binding.continueAsGuest2
        continueAsGuestButton3 = binding.continueAsGuest3
        inputField1 = binding.inputField1
        inputField2 = binding.inputField2
        helloText = binding.helloText
        usernameText = binding.usernameText

        logTheUserInButton.setOnClickListener {
            handleLogin()
        }

        createTheUserAccountButton.setOnClickListener {
            handleLogin()
        }

        continueAsGuestButton1.setOnClickListener {
            handleLogin()
        }

        continueAsGuestButton2.setOnClickListener {
            handleLogin()
        }

        continueAsGuestButton3.setOnClickListener {
            handleLogin()
        }

        logoutButton.setOnClickListener {
            handleLogout()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun handleLogin() {
        profileCreateAccount.visibility = View.GONE
        profileLogIn.visibility = View.GONE
        profileSignedOut.visibility = View.GONE
        profileSignedIn.visibility = View.VISIBLE
        (activity as MainActivity?)!!.showNavView()
        val username = if (!inputField1.text.isEmpty()){ inputField1.text }else {inputField2.text}
        helloText.text = "Hello $username!"
        usernameText.text = username
    }

    fun handleLogout() {
        profileCreateAccount.visibility = View.GONE
        profileLogIn.visibility = View.GONE
        profileSignedOut.visibility = View.VISIBLE
        profileSignedIn.visibility = View.GONE
        (activity as MainActivity?)!!.hideNavView()
    }
}