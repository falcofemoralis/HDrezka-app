package com.falcofemoralis.hdrezkaapp.views.tv

import android.animation.*
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.falcofemoralis.hdrezkaapp.R
import com.falcofemoralis.hdrezkaapp.constants.NavigationMenuTabs
import com.falcofemoralis.hdrezkaapp.objects.SettingsData
import com.falcofemoralis.hdrezkaapp.views.tv.interfaces.FragmentChangeListener
import com.falcofemoralis.hdrezkaapp.views.tv.interfaces.NavigationStateListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nikartm.support.ImageBadgeView


class NavigationMenu : Fragment() {
    private lateinit var fragmentChangeListener: FragmentChangeListener
    private lateinit var navigationStateListener: NavigationStateListener
    private lateinit var currentView: View
    private var lastSelectedMenu: String? = NavigationMenuTabs.nav_menu_newest
    private var _context: Context? = null
    private val buttons = ArrayList<DrawerButton>()

    private class DrawerButton(buttonView: ImageView, textView: TextView, category: String, selectedImage: Int, unselectedImage: Int, id: Int) {
        val buttonView: ImageView = buttonView // ImageButton
        val textView: TextView = textView
        val category: String = category
        val selectedImage: Int = selectedImage
        val unselectedImage: Int = unselectedImage
        val id: Int = id
    }

    companion object {
        val animDuration = 200L
        var notifyBtn: ImageBadgeView? = null
        var isFree = true
        var closed = true
        var isLocked = false
        var isViewOnHover = false
    }

    /**
     * Init drawer button views
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentView = inflater.inflate(R.layout.fragment_nav_menu, container, false) as View

        isFree = true
        closed = true
        isLocked = false
        isViewOnHover = false

        val notifyIB = currentView.findViewById<ImageBadgeView>(R.id.notify_IB)
        notifyBtn = notifyIB
        buttons.add(DrawerButton(notifyIB, currentView.findViewById(R.id.notify_TV), NavigationMenuTabs.nav_menu_series_updates, R.drawable.ic_baseline_notifications_24_sel, R.drawable.ic_baseline_notifications_24, -1))
        buttons.add(DrawerButton(currentView.findViewById(R.id.newest_IB), currentView.findViewById(R.id.newest_TV), NavigationMenuTabs.nav_menu_newest, R.drawable.ic_baseline_movie_24_sel, R.drawable.ic_baseline_movie_24, 0))
        buttons.add(DrawerButton(currentView.findViewById(R.id.categories_IB), currentView.findViewById(R.id.categories_TV), NavigationMenuTabs.nav_menu_categories, R.drawable.ic_baseline_categories_24_sel, R.drawable.ic_baseline_categories_24, 1))
        buttons.add(DrawerButton(currentView.findViewById(R.id.search_IB), currentView.findViewById(R.id.search_TV), NavigationMenuTabs.nav_menu_search, R.drawable.ic_baseline_search_24_sel, R.drawable.ic_baseline_search_24, 2))
        buttons.add(DrawerButton(currentView.findViewById(R.id.bookmarks_IB), currentView.findViewById(R.id.bookmarks_TV), NavigationMenuTabs.nav_menu_bookmarks, R.drawable.ic_baseline_bookmarks_24_sel, R.drawable.ic_baseline_bookmarks_24, 3))
        buttons.add(DrawerButton(currentView.findViewById(R.id.later_IB), currentView.findViewById(R.id.later_TV), NavigationMenuTabs.nav_menu_later, R.drawable.ic_baseline_watch_later_24_sel, R.drawable.ic_baseline_watch_later_24, 4))
        buttons.add(DrawerButton(currentView.findViewById(R.id.settings_IB), currentView.findViewById(R.id.settings_TV), NavigationMenuTabs.nav_menu_settings, R.drawable.ic_baseline_settings_24_sel, R.drawable.ic_baseline_settings_24, -1))

        return currentView
    }

    /**
     * Init drawer buttons listeners
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _context = context

        GlobalScope.launch {
            Thread.sleep(100)
            withContext(Dispatchers.Main) {
                for (drawerBtn in buttons) {
                    setDrawerButtonListener(drawerBtn)
                }
                setOnHoverListener()
            }
        }
    }

    private fun setDrawerButtonListener(drawerButton: DrawerButton) {
        // Set default focus
        if (SettingsData.mainScreen == drawerButton.id) {
            lastSelectedMenu = drawerButton.category
            setMenuIconFocusView(drawerButton.selectedImage, drawerButton.buttonView)
            setMenuNameFocusView(drawerButton.textView, true)
        }

        drawerButton.buttonView.setOnFocusChangeListener { v, hasFocus ->
            if (isFree && !isLocked) {
                if (hasFocus) {
                    if (isNavigationOpen()) {
                        // user selects the button when the drawer is open
                        // focus this button
                        Log.d("TEST_DRAWER", "user selects the button when the drawer is open -> focus this button")
                        setFocusedView(drawerButton.buttonView, drawerButton.selectedImage)
                        setMenuNameFocusView(drawerButton.textView, true)
                        focusIn(drawerButton.buttonView)
                    } else {
                        // user selects the button when the drawer is closed
                        // open the drawer
                        Log.d("TEST_DRAWER", "user selects the button when the drawer is closed -> focus this button")
                        System.out.println(v)
                        openNav()
                    }
                } else {
                    if (isNavigationOpen()) {
                        // user goes out of the button when the drawer is open
                        // unfocus this button
                        Log.d("TEST_DRAWER", "user goes out of the button when the drawer is open -> unfocus this button")
                        setOutOfFocusedView(drawerButton.buttonView, drawerButton.unselectedImage)
                        setMenuNameFocusView(drawerButton.textView, false)
                        focusOut(drawerButton.buttonView)
                    }
                }
            }
        }

        drawerButton.buttonView.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        if (!closed) {
                            // user presses DPAD_RIGHT when drawer is open
                            // close drawer
                            Log.d("TEST_DRAWER", "user presses DPAD_RIGHT when drawer is open -> close drawer")
                            closeNav()
                        }
                    }
//                    KeyEvent.KEYCODE_DPAD_LEFT -> {
//                        if (!isNavigationOpen()) {
//                            // user presses DPAD_LEFT when drawer is closed
//                            // open drawer
//                            Log.d("TEST_DRAWER", "user presses DPAD_LEFT when drawer is closed -> open drawer")
//                            openNav()
//                        }
//                    }
//                    KeyEvent.KEYCODE_ENTER -> {
//                        Log.d("KET_TEST", "KEYCODE_ENTER")
//                        closed = true
//                        lastSelectedMenu = lastMenu
//                        fragmentChangeListener.switchFragment(lastMenu)
//                        focusOut(ib)
//                        // closeNav()
//                    }
//                    KeyEvent.KEYCODE_DPAD_UP -> {
//                        if (!drawerButton.buttonView.isFocusable)
//                            drawerButton.buttonView.isFocusable = true
//                    }
//                    KeyEvent.KEYCODE_DPAD_CENTER -> {
//                        if (lastSelectedMenu != lastMenu) {
//                            fragmentChangeListener.switchFragment(lastMenu)
//                        }
//                        lastSelectedMenu = lastMenu
//                        closeNav()
//                    }
                    KeyEvent.KEYCODE_BACK -> {
                        if (isNavigationOpen()) {
                            // user presses BACK when drawer is open
                            // close drawer
                            Log.d("TEST_DRAWER", "user presses BACK when drawer is open -> close drawer")
                            closeNav()
                        }
                    }
                }
            }
            false
        }

        drawerButton.buttonView.setOnClickListener {
            if (isNavigationOpen()) {
                // user presses on image button when drawer is open
                // close drawer and go to selected fragment
                Log.d("TEST_DRAWER", "user presses on image button when drawer is open -> close drawer and go to selected fragment")
                if (lastSelectedMenu != drawerButton.category) {
                    fragmentChangeListener.switchFragment(drawerButton.category)
                }
                lastSelectedMenu = drawerButton.category
                closeNav()
            } else {
                // user presses on image button when drawer is close
                // close drawer
                Log.d("TEST_DRAWER", "user presses on image button when drawer is close -> close drawer")
                openNav()
            }
        }

        drawerButton.textView.setOnClickListener {
            if (isNavigationOpen()) {
                // user presses on text button when drawer is open
                // close drawer
                Log.d("TEST_DRAWER", " user presses on text button when drawer is open -> close drawer and go to selected fragment")
                highlightMenuSelection(drawerButton.category)
                if (lastSelectedMenu != drawerButton.category) {
                    fragmentChangeListener.switchFragment(drawerButton.category)
                }
                lastSelectedMenu = drawerButton.category
                closeNav()
            }
        }

        // hover

        drawerButton.textView.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    //   highlightMenuSelection(lastMenu)
                    drawerButton.textView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.primary_red
                        )
                    )
                }
                MotionEvent.ACTION_UP -> {
                    //  unHighlightMenuSelections(lastMenu)
                    drawerButton.textView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
            }

            v?.onTouchEvent(event) ?: true
        }

        fun onHoverListener(event: MotionEvent) {
            if (isFree && !isLocked) {
                when (event.action) {
                    MotionEvent.ACTION_HOVER_ENTER -> {
                        isViewOnHover = true

                        if (lastSelectedMenu != drawerButton.category) {
                            setFocusedView(drawerButton.buttonView, drawerButton.selectedImage)
                            setMenuNameFocusView(drawerButton.textView, true)
                            focusIn(drawerButton.buttonView)
                        }
                    }
                    MotionEvent.ACTION_HOVER_EXIT -> {
                        if (lastSelectedMenu != drawerButton.category) {
                            setOutOfFocusedView(drawerButton.buttonView, drawerButton.unselectedImage)
                            setMenuNameFocusView(drawerButton.textView, false)
                            focusOut(drawerButton.buttonView)
                        }
                    }
                }
            }
        }

        drawerButton.buttonView.setOnHoverListener { v, event ->
            onHoverListener(event)
            true
        }

        drawerButton.textView.setOnHoverListener { v, event ->
            onHoverListener(event)
            true
        }
    }

    private fun setOnHoverListener() {
        currentView.findViewById<ConstraintLayout>(R.id.open_nav_CL).setOnHoverListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_HOVER_ENTER -> {
                    if (isViewOnHover) {
                        isViewOnHover = false
                    } else {
                        openNav()
                    }
                }
                MotionEvent.ACTION_HOVER_EXIT -> {
                    if (!isViewOnHover) {
                        closeNav()
                    }
                }
            }
            true

        }
    }

    private fun setOutOfFocusedView(view: ImageView, resource: Int) {
        setMenuIconFocusView(resource, view)
    }

    private fun setFocusedView(view: ImageView, resource: Int) {
        setMenuIconFocusView(resource, view)
    }

    private fun focusOut(v: View) {
        val scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.2f, 1f)
        val set = AnimatorSet()
        set.play(scaleX).with(scaleY)
        set.start()
    }

    private fun focusIn(v: View) {
        val scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.2f)
        val scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.2f)
        val set = AnimatorSet()
        set.play(scaleX).with(scaleY)
        set.start()
    }

    private fun setMenuIconFocusView(resource: Int, view: ImageView) {
        view.setImageResource(resource)
    }

    private fun setMenuNameFocusView(view: TextView, inFocus: Boolean) {
        if (inFocus) {
            view.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.primary_red
                )
            )
        } else
            view.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
    }

    private fun openNav() {
        Log.d("TEST_DRAWER", "openNav")
        Log.d("TEST_DRAWER", closed.toString())
        if (closed) {
            Log.d("TEST_DRAWER", "OPEN")
            closed = false
            navigationStateListener.onStateChanged(closed)

            val fromWidth = currentView.width
            toggleNavMenuViews(View.VISIBLE)
            currentView.measure(WRAP_CONTENT, MATCH_PARENT)
            val toWidth = currentView.measuredWidth
            toggleNavMenuViews(View.GONE)
            animateView(currentView, fromWidth, toWidth) {
                toggleNavMenuViews(View.VISIBLE)
            }

            for (btn in buttons) {
                if (btn.category == lastSelectedMenu) {
                    btn.buttonView.requestFocus()
                    setMenuNameFocusView(btn.textView, true)
                    break
                }
            }
        }
    }

    private fun closeNav() {
        Log.d("TEST_DRAWER", "closeNav")
        Log.d("TEST_DRAWER", closed.toString())
        if (!closed) {
            Log.d("TEST_DRAWER", "CLOSE")
            closed = true
            navigationStateListener.onStateChanged(closed)

            val fromWidth = currentView.width
            toggleNavMenuViews(View.GONE)
            currentView.measure(WRAP_CONTENT, MATCH_PARENT)
            val toWidth = currentView.measuredWidth
            animateView(currentView, fromWidth, toWidth) {

            }

            //highlighting last selected menu icon
            highlightMenuSelection(lastSelectedMenu)

            //Setting out of focus views for menu icons, names
            unHighlightMenuSelections(lastSelectedMenu)
        }
    }

    private fun animateView(view: View, from: Int, to: Int, animationEnd: () -> Unit) {
        val anim = ValueAnimator.ofInt(from, to)
        anim.interpolator = AccelerateInterpolator()
        anim.duration = animDuration
        anim.addUpdateListener { animation ->
            view.layoutParams.width = animation.animatedValue as Int
            view.requestLayout()
        }
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                // At the end of animation, set the height to wrap content
                // This fix is for long views that are not shown on screen
                animationEnd()
                val layoutParams = view.layoutParams
                layoutParams.width = WRAP_CONTENT
            }
        })
        anim.start()
    }

    private fun unHighlightMenuSelections(lastSelectedMenu: String?) {
        for (btn in buttons) {
            if (!lastSelectedMenu.equals(btn.category, true)) {
                setOutOfFocusedView(btn.buttonView, btn.unselectedImage)
                setMenuNameFocusView(btn.textView, false)
            }
        }
    }

    private fun highlightMenuSelection(lastSelectedMenu: String?) {
        for (btn in buttons) {
            if (lastSelectedMenu == btn.category) {
                setFocusedView(btn.buttonView, btn.selectedImage)
                break
            }
        }
    }

    private fun toggleNavMenuViews(visibility: Int) {
        for (btn in buttons) {
            btn.textView.visibility = visibility
        }
        /*     if (visibility == View.GONE) {
                 val animate = AnimationUtils.loadAnimation(_context, R.anim.slide_in_right_menu_name)
                 val duration = context?.resources?.getInteger(R.integer.animation_duration)?.toLong()

                 duration?.let {
                     animate.duration = it
                 }
                 animate.setAnimationListener(object : Animation.AnimationListener {
                     override fun onAnimationStart(animation: Animation?) {
                     }

                     override fun onAnimationEnd(animation: Animation?) {
                         for (btn in buttons) {
                             btn.textView.visibility = visibility
                         }
                     }

                     override fun onAnimationRepeat(animation: Animation?) {
                     }
                 })

                 for (btn in buttons) {
                     btn.textView.startAnimation(animate)
                 }
             } else {
                 for (btn in buttons) {
                     btn.textView.visibility = visibility
                 }
             }*/
    }

    private fun isNavigationOpen() = buttons[1].textView.visibility == View.VISIBLE

    fun setFragmentChangeListener(callback: FragmentChangeListener) {
        this.fragmentChangeListener = callback
    }

    fun setNavigationStateListener(callback: NavigationStateListener) {
        this.navigationStateListener = callback
    }
}
