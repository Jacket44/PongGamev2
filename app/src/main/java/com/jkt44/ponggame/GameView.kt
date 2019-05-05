package com.jkt44.ponggame

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.view.*


class GameView(context: Context, attributeSet: AttributeSet)
    : SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    private val thread : GameThread
    private var  sharedPreferences : SharedPreferences = context.getSharedPreferences("points", Context.MODE_PRIVATE)

    private var ball = Ball(0f, 0f)
    private var leftPaddle = Paddle(0f, 0f, 0f)
    private var rightPaddle = Paddle(0f, 0f, 0f)
    private var rScore = 0
    private var lScore = 0

    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        thread.setRunning(false)
        thread.join()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        thread.setRunning(true)
        thread.start()

        ball.y = height / 2f
        ball.x = width / 2f

        leftPaddle.height = height / 3f
        rightPaddle.height = height / 3f

        rightPaddle.x = width - rightPaddle.getWidth()

    }

    fun reset(){
        ball.y = height / 2f
        ball.x = width / 2f
        ball.resetSpeed()
    }

    fun update() {
        ball.update()

        if (ball.y <= 0 || ball.y + SIZE >= height) ball.bounceWall()

        if (ball.x >= rightPaddle.x - rightPaddle.getWidth() && ball.y <= rightPaddle.y + rightPaddle.height && ball.y >= rightPaddle.y){
            ball.bounceSide()
            ball.speedUp()
        }
        if (ball.x <= leftPaddle.x + leftPaddle.getWidth() && ball.y <= leftPaddle.y + leftPaddle.height && ball.y >= leftPaddle.y){
            ball.bounceSide()
            ball.speedUp()
        }
        if(ball.x <= 0f){
            reset()
            rScore++
            sharedPreferences.edit().putInt("RightScore", rScore).apply()
            Log.i("test", "lScore: ${sharedPreferences.getInt("LeftScore", 0)}, rScore: ${sharedPreferences.getInt("RightScore", 0)}")
        }

        if(ball.x >= width) {

            reset()
            lScore++
            sharedPreferences.edit().putInt("LeftScore", lScore).apply()
            Log.i("test", "lScore: ${sharedPreferences.getInt("LeftScore", 0)}, rScore: ${sharedPreferences.getInt("RightScore", 0)}")
        }

    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas == null) return

        canvas.drawColor(Color.argb(255, 0,0,0))

        leftPaddle.draw(canvas)
        rightPaddle.draw(canvas)
        ball.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        for (i in 0 until event!!.pointerCount) {
            if (event.getX(i) < width / 2f) {
                leftPaddle.y = event.getY(i) - leftPaddle.height / 2
            } else {
                rightPaddle.y = event.getY(i) - rightPaddle.height / 2
            }
        }
        return true
    }
}