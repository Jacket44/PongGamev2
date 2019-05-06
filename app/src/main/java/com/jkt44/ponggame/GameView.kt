package com.jkt44.ponggame

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
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
    private val sharedPreferences : SharedPreferences = context.getSharedPreferences("points", Context.MODE_PRIVATE)

    private var ball = Ball(0f, 0f)
    private var leftPaddle = Paddle(0f, 0f, 0f)
    private var rightPaddle = Paddle(0f, 0f, 0f)
    private var bestScore = sharedPreferences.getInt("bestScore", 0)
    private var score = 0


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

        Log.i("test","${bestScore}")

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
            score++
        }
        if (ball.x <= leftPaddle.x + leftPaddle.getWidth() && ball.y <= leftPaddle.y + leftPaddle.height && ball.y >= leftPaddle.y){
            ball.bounceSide()
            ball.speedUp()
            score++
        }
        if(ball.x <= 0f || ball.x >= width) {
            reset()
            if(bestScore < score){
                bestScore = score
                sharedPreferences.edit().putInt("bestScore", bestScore)
            }
            score = 0

        }


    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas == null) return

        canvas.drawColor(Color.argb(255, 0,0,0))

        leftPaddle.draw(canvas)
        rightPaddle.draw(canvas)
        ball.draw(canvas)

        updateScore(canvas)

    }

    private fun updateScore(canvas: Canvas?){
        canvas?.also { it ->
            val textPaint = TextPaint()
            textPaint.color = Color.WHITE
            textPaint.textSize = 500f
            textPaint.textAlign = Paint.Align.CENTER
            val xPos = canvas.width / 2f
            val yPos = (canvas.height / 2f - (textPaint.descent() + textPaint.ascent()) / 2f)
            it.drawText("${score}", xPos, yPos, textPaint)
            textPaint.textSize = 100f
            it.drawText("BEST: ${bestScore}", xPos, 100f, textPaint)

        }

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