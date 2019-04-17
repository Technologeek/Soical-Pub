package com.socialpub.rahul.ui.home.navigation

import com.socialpub.rahul.base.BaseNavController


/**
 * Delegates navigation from whole module,
 */
interface NavController : BaseNavController {

    fun signoutUser()
    fun openSideNavigator()

}