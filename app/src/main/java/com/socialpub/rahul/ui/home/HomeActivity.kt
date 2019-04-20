package com.socialpub.rahul.ui.home

import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.base.NavFlow
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.ui.home.navigation.NavController
import com.socialpub.rahul.ui.home.navigation.Navigator
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_home_side_nav_header.view.*

class HomeActivity : BaseActivity(), NavController {


    override val contentLayout: Int
        get() = R.layout.activity_home

    lateinit var navigator: Navigator

    override fun setup() {

        navigator = Navigator(R.id.screen_home, this)

        navigator.addFragment(
            navigator.getFragment(NavFlow.Home.FEEDS),
            NavFlow.Home.FEEDS
        )

        bottom_nave_home.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> navigator.goto(NavFlow.Home.FEEDS)
                R.id.action_search -> navigator.goto(NavFlow.Home.SEARCH)
                R.id.action_profile -> navigator.goto(NavFlow.Home.PROFILE)
                else -> navigator.goto(NavFlow.Home.FEEDS)
            }
            return@setOnNavigationItemSelectedListener true
        }

        side_drawer_home.let {

            it.setNavigationItemSelectedListener {
                drawer_container_home.closeDrawers()
                return@setNavigationItemSelectedListener when (it.itemId) {
                    R.id.action_favourite -> {
                        navigator.openManageFavourite()
                        true
                    }
                    R.id.action_followers -> {
                        navigator.openManageFollowers()
                        true
                    }
                    R.id.action_notification -> {
                        navigator.openNotifications()
                        true
                    }
                    R.id.action_edit_profile -> {
                        navigator.openEditProfile()
                        true
                    }
                    R.id.action_settings -> {
                        navigator.openSettings()
                        true
                    }
                    R.id.action_logout -> {
                        signoutUser()
                        true
                    }
                    else -> false
                }
            }

            drawer_container_home.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {

                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                }

                override fun onDrawerClosed(drawerView: View) {
                    updateDrawerHeader(it)
                }

                override fun onDrawerOpened(drawerView: View) {

                }

            })

            updateDrawerHeader(it)

        }

    }

    private fun updateDrawerHeader(it: NavigationView) {
        val navHeader = it.getHeaderView(0)

        with(requireNotNull(navHeader)) {

            Picasso.get()
                .load(Injector.userPrefs().avatarUrl)
                .placeholder(R.mipmap.ic_launcher_round)
                .transform(CropCircleTransformation())
                .into(navHeader.image_side_nav_home_useravatar)

            navHeader.text_side_nav_home_username.text = Injector.userPrefs().displayName

        }
    }

    override fun openPostLocationOnMap(post: Post) {
        navigator.openMapLocation(post.postId)
    }

    override fun openProfilePreview(showFollow: Boolean, PreviewUserId: String, fromSearchResults: Boolean) {
        navigator.openProfilePreview(showFollow, PreviewUserId, fromSearchResults)
    }

    override fun openPostPreview(enableDelete: Boolean, PreviewPostId: String, PreviewUserId: String) {
        navigator.openPostPreview(enableDelete, PreviewPostId, PreviewUserId)
    }


    override fun openSideNavigator() {
        drawer_container_home.openDrawer(side_drawer_home)
    }

    override fun signoutUser() {
        actionSignoutConfirm()
    }

    override fun goto(screen: String) {
        navigator.goto(screen)
    }

    override fun goBack() = navigator.goBack()

    private fun actionSignoutConfirm() {
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("SignOut")
            setMessage("Are you sure you want to Signout from Social-pup?")
            setPositiveButton("Yes", DialogInterface.OnClickListener(function = { dialog, which ->
                with(Injector) {
                    firebaseManager().auth.signOut()
                    userPrefs().clearPrefs()
                }
                navigator.restartApp()
            }))
            setNegativeButton("No", DialogInterface.OnClickListener(function = { dialog, which ->
                dialog.dismiss()
            }))
            if (!isFinishing) show()
        }
    }

}


