package com.socialpub.rahul.ui.home.navigation

import com.socialpub.rahul.base.BaseNavController
import com.socialpub.rahul.data.model.Post


/**
 * Delegates navigation from whole module,
 */
interface NavController : BaseNavController {

    fun signoutUser()
    fun openSideNavigator()
    fun openProfilePreview(showFollow: Boolean, PreviewUserId: String,fromSearchResults:Boolean = false)
    fun openPostLocationOnMap(post: Post)
    fun openPostPreview(enableDelete: Boolean, PreviewPostId: String, PreviewUserId: String)

}