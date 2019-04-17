package com.socialpub.rahul.ui.home

import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.base.NavFlow
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
                    R.id.action_post -> {
                        navigator.openManagePost()
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

    }

    override fun signoutUser() {
        with(Injector) {
            firebaseManager().auth.signOut()
            userPrefs().clearPrefs()
        }
        navigator.restartApp()
    }

    override fun goto(screen: String) {
        navigator.goto(screen)
    }

    override fun goBack() = navigator.goBack()
}


