package ui.anwesome.com.threeringsview

/**
 * Created by anweshmishra on 09/04/18.
 */

import android.app.Activity
import android.content.*
import android.view.*
import android.graphics.*

class ThreeRingsView (ctx : Context) : View(ctx) {

    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State (var dir : Float = 0f, var prevScale : Float = 0f, var j : Int = 0) {

        val scales : Array<Float> = arrayOf(0f, 0f, 0f, 0f, 0f)

        fun update(stopcb : (Float) -> Unit) {
            scales[j] += 0.1f * dir
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }

        fun startUpdating (startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator (var view : View, var animated : Boolean = false) {

        fun animate (updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class ThreeRings (var i : Int, val state : State = State()) {
        fun draw (canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val r : Float = Math.min(w, h)/9
            paint.color = Color.parseColor("#ec7b11")
            paint.strokeWidth = Math.min(w,h)/55
            paint.strokeCap = Paint.Cap.ROUND
            for (i in 0..2) {
                canvas.save()
                canvas.translate(w/2 + (w/2 -r) * (1 - i) * this.state.scales[1], -2 * r + (h/2 + 2 * r) * (1 - this.state.scales[2 + i]))
                val path : Path = Path()
                for (j in 0..(360 * this.state.scales[0]).toInt()) {
                    val x : Float = (r * Math.cos( j * Math.PI/180)).toFloat()
                    val y : Float = (r * Math.sin( j * Math.PI/180)).toFloat()

                    if (j == 0) {
                        path.moveTo(x, y)
                    }
                    else {
                        path.lineTo(x, y)
                    }
                }
                canvas.drawPath(path, paint)
                canvas.restore()
            }
        }
        fun update (stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating (startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }

    data class Renderer (var view : ThreeRingsView) {

        private val animator : Animator = Animator(view)

        private val threeRings : ThreeRings = ThreeRings(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            threeRings.draw(canvas, paint)
            animator.animate {
                threeRings.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            threeRings.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity) : ThreeRingsView {
            val view : ThreeRingsView = ThreeRingsView(activity)
            activity.setContentView(view)
            return view
        }
    }
}