package com.socialpub.rahul.base

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

abstract class BaseNavigator(
    @IdRes private val activeScreen: Int,
    private val activity: BaseActivity
) {

    fun addFragment(fragment: Fragment, screen: String) =
        activity.supportFragmentManager.beginTransaction()
            .add(activeScreen, fragment, screen)
            .commitAllowingStateLoss()

    fun replaceFragment(fragment: Fragment, screen: String) =
        activity.supportFragmentManager.beginTransaction()
            .replace(activeScreen, fragment, screen)
            .commitAllowingStateLoss()


}