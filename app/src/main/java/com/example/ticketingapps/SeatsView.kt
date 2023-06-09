package com.example.ticketingapps

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import android.view.MotionEvent.ACTION_DOWN

class SeatsView : View {

    private val seats: ArrayList<Seat> = arrayListOf(
        Seat(id = 1, name = "A1", isBooked = false),
        Seat(id = 2, name = "A2", isBooked = false),
        Seat(id = 3, name = "B1", isBooked = false),
        Seat(id = 4, name = "A4", isBooked = false),
        Seat(id = 5, name = "C1", isBooked = false),
        Seat(id = 6, name = "C2", isBooked = false),
        Seat(id = 7, name = "D1", isBooked = false),
        Seat(id = 8, name = "D2", isBooked = false),
    )

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var seat: Seat? = null

    private val backgroundPaint = Paint()
    private val armrestPaint = Paint()
    private val bottomSeatPaint = Paint()
    private val mBounds = Rect()
    private val numberSeatPaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)
    private val titlePaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (seat in seats) {
            drawSeat(canvas, seat)
        }

        val text = "Silakan Memilih Kursi"
        titlePaint.apply {
            textSize = 50f
        }
        canvas?.drawText(text, (width / 2f) - 197f, 100f, titlePaint)
    }

    private fun drawSeat(canvas: Canvas?, seat: Seat) {
        if (seat.isBooked) {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        } else {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.blue_500, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.blue_700, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.blue_200, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
        }

        canvas?.save()

        canvas?.translate(seat.x as Float, seat.y as Float)

        val backgroundPath = Path()
        backgroundPath.addRect(0f, 0f, 200f, 200f, Path.Direction.CCW)
        backgroundPath.addCircle(100f, 50f, 75f, Path.Direction.CCW)
        canvas?.drawPath(backgroundPath, backgroundPaint)

        val armRestPath = Path()
        armRestPath.addRect(0f, 0f, 50f, 200f, Path.Direction.CCW)
        canvas?.drawPath(armRestPath, armrestPaint)
        canvas?.translate(150f, 0f)
        armRestPath.addRect(0f, 0f, 50f, 200f, Path.Direction.CCW)
        canvas?.drawPath(armRestPath, armrestPaint)

        canvas?.translate(-150f, 175f)
        val bottomSeatPath = Path()
        bottomSeatPath.addRect(0f, 0f, 200f, 25f, Path.Direction.CCW)
        canvas?.drawPath(bottomSeatPath, bottomSeatPaint)

        canvas?.translate(0f, -175f)
        numberSeatPaint.apply {
            textSize = 50f
            numberSeatPaint.getTextBounds(seat.name, 0, seat.name.length, mBounds)
        }
        canvas?.drawText(seat.name, 100f - mBounds.centerX(), 100f, numberSeatPaint)

        canvas?.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)

        val halfOfWidth = width / 2
        val halfOfHeight = height / 2
        var value = -600f

        for (i in 0..7) {
            if (i.mod(2) == 0) {
                seats[i].apply {
                    x = halfOfWidth - 300f
                    y = halfOfHeight + value
                }
            } else {
                seats[i].apply {
                    x = halfOfWidth + 100f
                    y = halfOfHeight + value
                }
                value += 300f
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val halfOfHeight = height / 2
        val halfOfWidth = width / 2

        val widthColumnOne = (halfOfWidth - 300f)..(halfOfWidth - 100f)
        val widthColumnTwo = (halfOfWidth + 100f)..(halfOfWidth + 300f)

        val heightRowOne = (halfOfHeight - 600f)..(halfOfHeight - 400f)
        val heightRowTwo = (halfOfHeight - 300f)..(halfOfHeight - 100f)
        val heightRowThree = (halfOfHeight + 0f)..(halfOfHeight + 200f)
        val heightRowFour = (halfOfHeight + 300f)..(halfOfHeight + 500f)

        if (event?.action == ACTION_DOWN) {
            when {
                event.x in widthColumnOne && event.y in heightRowOne -> booking(0)
                event.x in widthColumnTwo && event.y in heightRowOne -> booking(1)
                event.x in widthColumnOne && event.y in heightRowTwo -> booking(2)
                event.x in widthColumnTwo && event.y in heightRowTwo -> booking(3)
                event.x in widthColumnOne && event.y in heightRowThree -> booking(4)
                event.x in widthColumnTwo && event.y in heightRowThree -> booking(5)
                event.x in widthColumnOne && event.y in heightRowFour -> booking(6)
                event.x in widthColumnTwo && event.y in heightRowFour -> booking(7)
            }
        }
        return true
    }

    private fun booking(position: Int) {
        for (seat in seats) {
            seat.isBooked = false
        }

        seats[position].apply {
            seat = this
            isBooked = true
        }
        invalidate()
    }

    data class Seat(
        val id: Int,
        var x: Float? = 0f,
        var y: Float? = 0f,
        var name: String,
        var isBooked: Boolean,
    )
}