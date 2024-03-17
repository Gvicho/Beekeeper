package com.example.beekeeper.presenter.utils

import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

 open class SwipeGestureDetector : GestureDetector.SimpleOnGestureListener() {

    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        e1?.let { start ->
            e2.let { end ->
                val diffY = end.y - start.y
                val diffX = end.x - start.x
                if (abs(diffX) > abs(diffY)) {
                    // Detect left or right swipe
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            // Swipe right
                            onSwipeRight()
                        } else {
                            // Swipe left
                            onSwipeLeft()
                        }
                        return true
                    }
                }
            }
        }
        return false
    }

    open fun onSwipeRight() {
        // Handle swipe right action
    }

    open fun onSwipeLeft() {
        // Handle swipe left action
    }
}
