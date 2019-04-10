package com.socialpub.rahul.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/***
 * Skeleton class for Activities , used to reduce boilerplate
 */
abstract class BaseActivity : AppCompatActivity() {

    protected abstract val contentLayout: Int

    protected abstract fun setup()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentLayout)
        setup()
    }

    protected fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}