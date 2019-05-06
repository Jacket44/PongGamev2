package com.jkt44.ponggame

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

const val SIZE = 80f

class Ball(var x: Float, var y: Float){

    private var angle = Random().nextFloat() % (2 * Math.PI.toFloat())

    private var xSpeed = 20f * sin(angle)
    private var ySpeed = 20f * cos(angle)

    fun update() {
        x+=xSpeed
        y+=ySpeed
    }

    fun bounceWall(){
        ySpeed=-ySpeed
    }

    fun bounceSide(){
        xSpeed= -xSpeed
    }

    fun speedUp(){
        xSpeed*=1.1f
        ySpeed*=1.1f
    }

    fun resetSpeed(){
        angle = Random().nextFloat() % (2 * Math.PI.toFloat())
        xSpeed = 20f * sin(angle)
        ySpeed = 20f * cos(angle)
    }

    fun draw(canvas: Canvas) {

        val white = Paint()
        white.setARGB(255,255,255,255)
        canvas.drawOval(RectF(x, y,x+SIZE,y+SIZE), white)
    }

}