package inc.fabudi.littleracer.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import inc.fabudi.littleracer.PathParser
import inc.fabudi.littleracer.R
import java.lang.Float.min


class RaceRing @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var progressPaint = Paint()
    private var backgroundPaint = Paint()
    private var whitePaint = Paint()
    private var progresses = HashMap<String, Float>()
    private var progressMax: Int = 100
    private var strokeWidth: Float = 0f
    private var halfWidth = width / 2.0f
    private var halfHeight = height / 2.0f
    private var radius = halfHeight / 2
    private var progressRadius = 10f
    private var progressDrawables = HashMap<String, Drawable>()
    private var path = Path()

    init {
        backgroundPaint.style = Paint.Style.STROKE
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.RaceRing, defStyleAttr, 0)
        try {
            strokeWidth = typedArray.getDimension(R.styleable.RaceRing_RR_stroke_width, 30f)
            progressRadius = typedArray.getDimension(R.styleable.RaceRing_RR_progress_radius, 10f)
            setBackgroundStrokeWidth(strokeWidth)
            setProgressColor(typedArray.getColor(R.styleable.RaceRing_RR_progress_color, 0))
            setProgressStrokeWidth(
                typedArray.getDimension(
                    R.styleable.RaceRing_RR_progress_stroke_width, 0f
                )
            )
            setProgressFilled(typedArray.getBoolean(R.styleable.RaceRing_RR_progress_filled, true))
            setBackgroundProgressColor(
                typedArray.getColor(
                    R.styleable.RaceRing_RR_progress_background_color, 0
                )
            )
        } finally {
            typedArray.recycle()
        }
        path = PathParser.parseStringIntoPathObject(
            "M 152.92 130.702 C 421.89 19.124 492.514 550.325 722.623 557.516 C 1029.614 552.538 1082.324 118.628 1305.317 125.135 C 1411.815 130.305 1463.761 644.255 1305.317 648.445 L 147.353 641.023 C -11.753 638.94 71.901 166.634 152.92 130.702 Z"
        )

        whitePaint.color = Color.WHITE
    }


    private fun setProgressFilled(filled: Boolean) {
        progressPaint.style = if (filled) Paint.Style.FILL else Paint.Style.STROKE
    }

    private fun setProgressStrokeWidth(dimension: Float) {
        progressPaint.strokeWidth = dimension
    }

    fun addProgress(key: String, value: Float, drawable: Drawable?) {
        progresses[key] = value
        if (drawable != null) progressDrawables[key] = drawable
        invalidate()
    }

    fun removeProgress(key: String) {
        progresses.remove(key)
        progressDrawables.remove(key)
        invalidate()
    }

    fun updateProgress(key: String, value: Float) {
        progresses.replace(key, value)
        invalidate()
    }

    fun updateProgress(key: String, value: Float, drawable: Drawable?) {
        progresses.replace(key, value)
        if (drawable != null) progressDrawables.replace(key, drawable)
        invalidate()
    }

    private fun calculateProgressPoint(progress: Float): PointF {
        val pathMeasure = PathMeasure(path, false)
        val cords = floatArrayOf(0f, 0f)
        pathMeasure.getPosTan(
            pathMeasure.length * (progress % progressMax) / progressMax, cords, null
        )
        return PointF(cords[0], cords[1])
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        halfWidth = width / 2.0f
        halfHeight = height / 2.0f
        radius = height / 2.0f
        val scaleMatrix = Matrix()
        val rect = RectF()
        path.computeBounds(rect, true)
        val ratioY = height / rect.height()
        val ratioX = width / rect.width()
        val maxRatio = min(min(ratioX, ratioY), 1.0f)
        scaleMatrix.postScale(maxRatio, maxRatio)
        path.transform(scaleMatrix)
        path.computeBounds(rect, true)
        canvas.translate((width-rect.width())/2, -((height - rect.height()) / 2 + strokeWidth))
        canvas.drawPath(path, backgroundPaint)
        for (progress in progresses) drawPoint(
            canvas, calculateProgressPoint(progress.value), progressDrawables[progress.key]
        )
    }

    private fun drawPoint(canvas: Canvas, point: PointF, drawable: Drawable?) {
        if (drawable == null) canvas.drawCircle(point.x, point.y, progressRadius, progressPaint)
        else {
            drawable.bounds = Rect(
                (point.x - progressRadius).toInt(),
                (point.y - progressRadius).toInt(),
                (point.x + progressRadius).toInt(),
                (point.y + progressRadius).toInt()
            )
            drawable.draw(canvas)
        }
    }

    private fun setProgressColor(color: Int) {
        progressPaint.color = color
    }

    private fun setBackgroundProgressColor(color: Int) {
        backgroundPaint.color = color
    }

    private fun setBackgroundStrokeWidth(strokeWidth: Float) {
        backgroundPaint.strokeWidth = strokeWidth
    }

    fun getProgress(key: String): Float? {
        return progresses[key]
    }

    fun setDrawableStateFor(key: String, state: IntArray) {
        val drawable = progressDrawables[key] ?: return
        drawable.state = state
        progressDrawables.replace(key, drawable)
    }

    fun removeAll() {
        progresses = HashMap()
        progressDrawables = HashMap()
    }

    companion object {
        const val TAG = "RaceRing"
    }

}