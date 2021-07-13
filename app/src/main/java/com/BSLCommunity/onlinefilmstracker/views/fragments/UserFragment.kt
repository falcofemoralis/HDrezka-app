package com.BSLCommunity.onlinefilmstracker.views.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.widget.*
import androidx.fragment.app.Fragment
import com.BSLCommunity.onlinefilmstracker.R
import com.BSLCommunity.onlinefilmstracker.clients.AuthWebViewClient
import com.BSLCommunity.onlinefilmstracker.interfaces.OnFragmentInteractionListener
import com.BSLCommunity.onlinefilmstracker.models.BookmarksModel
import com.BSLCommunity.onlinefilmstracker.models.UserModel
import com.BSLCommunity.onlinefilmstracker.objects.UserData
import com.BSLCommunity.onlinefilmstracker.presenters.UserPresenter
import com.BSLCommunity.onlinefilmstracker.views.MainActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserFragment : Fragment() {
    private lateinit var currentView: View
    private lateinit var userPresenter: UserPresenter
    private lateinit var popupWindowView: RelativeLayout
    private lateinit var popupWindow: PopupWindow
    private lateinit var webView: WebView
    private lateinit var popupWindowLoadingBar: ProgressBar
    private var popupWindowCloseBtn: Button? = null
    private var isLoaded = false
    private lateinit var authPanel: LinearLayout
    private lateinit var exitPanel: TextView
    private lateinit var fragmentListener: OnFragmentInteractionListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentListener = context as OnFragmentInteractionListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentView = inflater.inflate(R.layout.fragment_user, container, false)
        userPresenter = UserPresenter()
        Log.d("FRAGMENT_TEST", "user init")

        authPanel = currentView.findViewById(R.id.fragment_user_ll_auth_panel)
        exitPanel = currentView.findViewById(R.id.fragment_user_tv_exit)

        initExitButton()

        UserData.isLoggedIn?.let {
            setAuthPanel(it)
        }

        currentView.findViewById<TextView>(R.id.fragment_user_tv_settings).setOnClickListener {
            fragmentListener.onFragmentInteraction(this, SettingsFragment(), OnFragmentInteractionListener.Action.NEXT_FRAGMENT_HIDE, true, null, null)
        }

        return currentView
    }

    private fun setAuthPanel(isLogged: Boolean) {
        if (isLogged) {
            popupWindowCloseBtn?.visibility = View.GONE
            authPanel.visibility = View.GONE
            exitPanel.visibility = View.VISIBLE
        } else {
            authPanel.visibility = View.VISIBLE
            exitPanel.visibility = View.GONE
            createAuthDialog()
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    private fun createAuthDialog() {
        activity?.let {
            popupWindowView = requireActivity().layoutInflater.inflate(R.layout.dialog_auth, null) as RelativeLayout

            popupWindow = PopupWindow(popupWindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true)
            popupWindowLoadingBar = popupWindowView.findViewById(R.id.dialog_auth_pb_loading)
            popupWindowCloseBtn = popupWindowView.findViewById(R.id.dialog_auth_bt_close)
            webView = popupWindowView.findViewById(R.id.dialog_auth_wv_auth)
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true

            popupWindowView.setOnTouchListener { _, _ -> //Close the window when clicked outside
                popupWindow.dismiss()
                true
            }
            popupWindowView.findViewById<Button>(R.id.dialog_auth_bt_close).setOnClickListener {
                popupWindow.dismiss()
            }

            currentView.findViewById<TextView>(R.id.fragment_user_tv_login).setOnClickListener {
                // login
                loadAuthPage(true)
            }

            currentView.findViewById<TextView>(R.id.fragment_user_tv_register).setOnClickListener {
                //register
                loadAuthPage(false)
            }
        }
    }

    private fun loadAuthPage(isLogin: Boolean) {
        webView.visibility = View.GONE
        popupWindowCloseBtn?.visibility = View.GONE
        popupWindowLoadingBar.visibility = View.VISIBLE

        webView.webViewClient = AuthWebViewClient(isLogin, ::authCallback)

        popupWindow.showAtLocation(popupWindowView, Gravity.CENTER, 0, 0)
        if (!isLoaded) {
            webView.loadUrl(BookmarksModel.MAIN_PAGE)
            isLoaded = true
        }
    }

    private fun authCallback(isLogged: Boolean) {
        webView.stopLoading()
        isLoaded = false

        if (isLogged) {
            webView.visibility = View.GONE

            UserData.setLoggedIn(isLogged, requireContext())
            setAuthPanel(isLogged)
            setUserAvatar()

            activity?.let {
                (it as MainActivity).updatePager()
            }
            popupWindow.dismiss()
        } else {
            popupWindowLoadingBar.visibility = View.GONE
            popupWindowCloseBtn?.visibility = View.VISIBLE
            webView.visibility = View.VISIBLE
        }
    }

    private fun initExitButton() {
        exitPanel.setOnClickListener {
            activity?.let {
                UserData.setLoggedIn(false, it)
                setAuthPanel(false)
                CookieManager.getInstance().removeAllCookies(null)
                CookieManager.getInstance().flush()

                activity?.let { it1 ->
                    (it1 as MainActivity).updatePager()
                }
            }
        }
    }

    private fun setUserAvatar() {
        GlobalScope.launch {
            val link: String = UserModel.getUserAvatarLink()
            UserData.setAvatar(link, requireContext())

            withContext(Dispatchers.Main) {
                Picasso.get().load(link).into(requireActivity().findViewById<ImageView>(R.id.activity_main_iv_user))
            }
        }
    }
}