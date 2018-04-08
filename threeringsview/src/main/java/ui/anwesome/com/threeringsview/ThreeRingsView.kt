package ui.anwesome.com.threeringsview

/**
 * Created by anweshmishra on 09/04/18.
 */

import android.content.*
import android.view.*
import android.graphics.*

class ThreeRingsView (ctx : Context) : View(ctx) {

    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}