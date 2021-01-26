package doa.ai.extentions

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader

class RoundedTransformation (private val _radius: Int, private val _margin: Int) : com.squareup.picasso.Transformation {
    private val _key: String

    init {
        _key = "rounded(radius=$_radius, margin=$_margin)"
    }

    override fun transform(source: Bitmap): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val output = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        canvas.drawRoundRect(RectF(_margin.toFloat(), _margin.toFloat(), (source.width - _margin).toFloat(), (source.height - _margin).toFloat()),
                _radius.toFloat(), _radius.toFloat(), paint)

        if (source != output) {
            source.recycle()
        }

        return output
    }

    override fun key(): String {
        return _key
    }
}