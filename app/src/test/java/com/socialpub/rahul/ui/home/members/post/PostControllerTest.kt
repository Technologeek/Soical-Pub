package com.socialpub.rahul.ui.home.members.post

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import com.socialpub.rahul.R
import com.socialpub.rahul.ui.home.HomeActivity
import io.mockk.verify
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.support.v4.SupportFragmentController

@RunWith(RobolectricTestRunner::class)
internal class PostControllerTest {

    private var postFragment: PostFragment? = null
    lateinit var view: PostContract.View
    lateinit var controller: PostController

    @Before
    fun `prepare activity`() {
        postFragment = SupportFragmentController.of(PostFragment.newInstance(), HomeActivity::class.java, Intent())
            .create(R.id.screen_home, Bundle.EMPTY)
            .start()
            .resume()
            .visible()
            .get()
    }

    @BeforeEach
    fun `check setup`() {
        view = postFragment as PostContract.View
        controller = postFragment!!.controller
    }

    @Test
    fun `prepare view`() {
        assertNotNull(postFragment)
        assertNotNull(view)
        assertNotNull(controller)
        controller.onStart()
        verify(exactly = 1) { view.attachActions() }
        
    }
}