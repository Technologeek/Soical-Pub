package com.socialpub.rahul.base


/**
 * Delegates navigation from whole module,
 */
interface BaseNavController {
    fun goto(screen: String)
    fun goBack()
}