package com.jkt44.ponggame

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

const val SIZE = 80f

class Ball(var x: Float, var y: Float){

    private var dx = 10f
    private var dy = 10f

    fun update() {
        x+=dx
        y+=dy
    }

    fun bounceWall(){
        dy=-dy
    }

    fun bounceSide(){
        dx= -dx
    }

    fun speedUp(){
        dx*=1.25f
        dy*=1.25f
    }

    fun resetSpeed(){
        dx = 10f
        dy = 10f
    }

    fun draw(canvas: Canvas) {

        val white = Paint()
        white.setARGB(255,255,255,255)
        canvas.drawOval(RectF(x, y,x+SIZE,y+SIZE), white)
    }

}