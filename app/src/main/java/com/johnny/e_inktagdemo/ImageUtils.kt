package com.johnny.e_inktagdemo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.floor
import kotlin.math.roundToInt

object ImageUtils {
    fun generateBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) //創建Bitmap畫布

        val backgroundPaint = Paint()
        backgroundPaint.color = Color.WHITE

        val paint = Paint()
        paint.color = Color.BLACK

        val canvas = Canvas(bitmap)
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), backgroundPaint)

        val space = 10
        for (i in 0..floor((width / space).toDouble()).roundToInt()) {
            for (j in 0..floor((height / space).toDouble()).roundToInt()) {
                if ((i % 2 == 0 && j % 2 == 0)
                    || (i % 2 == 1 && j % 2 == 1)
                ) {
                    val x = (i * space).toFloat()
                    val y = (j * space).toFloat()
                    canvas.drawRect(
                        x,
                        y,
                        x + space.toFloat(),
                        y + space.toFloat(),
                        paint
                    )
                }
            }
        }
        return bitmap
    }
}