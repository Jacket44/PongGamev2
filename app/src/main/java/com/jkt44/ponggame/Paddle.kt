package com.jkt44.ponggame

import android.graphics.Canvas
import android.graphics.Paint

const val WIDTH = 50f

class Paddle(var x:Float, var y:Float, var height :Float){

    fun draw(canvas: Canvas) {
        val white = Paint()
        white.setARGB(255,255,255,255)
        canvas.drawRect(x, y, x+ WIDTH, y+ height, white)
    }

    fun getWidth():Float{return WIDTH}
}

