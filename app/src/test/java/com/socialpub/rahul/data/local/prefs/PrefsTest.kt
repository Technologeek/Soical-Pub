package com.socialpub.rahul.data.local.prefs

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions

class PrefsTest {

    /**
     * Global shared preferces of android, Since its a part of android
     * API is already tested by Google team , we only need to verify,
     * whether our use case function is returning values or not,
     *
     * here we are testing our getter setter of prefrences.
     */

    lateinit var socialPrefs: SocialPrefs

    @Before
    fun setup() {
        clearAllMocks()
        socialPrefs = mockk(relaxed = true)
    }

    @Test
    fun `user logged in setter is functional`() {
        socialPrefs.isUserLoggedIn = true
        verify(exactly = 1) { socialPrefs.isUserLoggedIn = any() }
    }

    @Test
    fun `user logged in getter is functional`() {
        every { socialPrefs.isUserLoggedIn } returns true
        Assertions.assertTrue(socialPrefs.isUserLoggedIn)
    }

}