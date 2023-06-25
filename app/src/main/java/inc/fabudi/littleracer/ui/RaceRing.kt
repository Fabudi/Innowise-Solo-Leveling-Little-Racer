package inc.fabudi.littleracer.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import inc.fabudi.littleracer.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


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
        val totalLength = 2 * PI * radius + 2 * (width - 2 * radius)
        val progressLength = totalLength * (progress % progressMax) / progressMax
        val upperLine = width - 2 * radius
        val rightCircle = upperLine + PI * radius
        val lowerLine = rightCircle + width - 2 * radius
        val leftCircle = lowerLine + PI * radius
        val offset = 2f * strokeWidth
        return when (progressLength) {
            in lowerLine..leftCircle -> {
                val angle = (progressLength - lowerLine) / radius
                PointF(
                    (radius + ((radius - offset) * cos(angle + PI / 2)).toFloat()),
                    radius + (((radius - offset) * sin(angle + PI / 2)).toFloat())
                )
            }

            in rightCircle..lowerLine -> PointF(
                (width - radius - (progressLength - rightCircle)).toFloat(), 2 * radius - offset
            )

            in upperLine..rightCircle.toFloat() -> {
                val angle = (progressLength - upperLine) / radius
                PointF(
                    (width.toFloat() - radius + ((radius - offset) * cos(angle - PI / 2)).toFloat()),
                    radius + (((radius - offset) * sin(angle - PI / 2)).toFloat())
                )
            }

            in 0f..upperLine -> PointF((radius + progressLength).toFloat(), offset)
            else -> PointF(0f, 0f)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        halfWidth = width / 2.0f
        halfHeight = height / 2.0f
        radius = height / 2.0f
        drawPath(canvas, backgroundPaint)
        for (progress in progresses) drawPoint(
            canvas, calculateProgressPoint(progress.value), progressDrawables[progress.key]
        )
    }

    private fun drawPath(canvas: Canvas, paint: Paint) {
        canvas.drawArc(
            2 * strokeWidth,
            2 * strokeWidth,
            2 * radius - 2 * strokeWidth,
            height - 2 * strokeWidth,
            270f,
            -180f,
            false,
            paint
        )
        canvas.drawLine(
            radius, 2 * strokeWidth, width - radius, 2 * strokeWidth, paint
        )
        canvas.drawArc(
            width - 2 * radius + 2 * strokeWidth,
            2 * strokeWidth,
            width.toFloat() - 2 * strokeWidth,
            2 * radius - 2 * strokeWidth,
            90f,
            -180f,
            false,
            paint
        )
        canvas.drawLine(
            width - radius,
            2 * radius - 2 * strokeWidth,
            radius,
            2 * radius - 2 * strokeWidth,
            paint
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

    companion object {
        const val TAG = "RaceRing"
    }

}