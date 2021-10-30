package org.andcreator.bubbble.drawable

import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.RectF
import androidx.annotation.ColorInt
import org.andcreator.iconpack.IconPack
import org.andcreator.iconpack.util.DisplayUtil
import org.andcreator.iconpack.util.NavigationItemAnimationUtil


/**
 * @author And
 */
class PagerBarDrawable : Drawable() {

    private var mPaint: Paint = Paint()
    private var mColor: Int = adjustAlpha(0xFFB39DDB.toInt(),0f)
    private var mRect: RectF
    private var itemAnimationUtil: NavigationItemAnimationUtil = NavigationItemAnimationUtil()
    var viewLeft = 0f
    var viewTop = 0f
    var viewRight = 0f
    var viewBottom = 0f

    var viewL = 0f
    var viewT = 0f
    var viewR = 0f
    var viewB = 0f

    private val margin = DisplayUtil.dip2px(IconPack.context,4f).toFloat()

    private var isOpen = true

    init {
        itemAnimationUtil.setPagerBarDrawable(this)
        mPaint.color = mColor
        mRect = RectF()
    }

    override fun onBoundsChange(bounds: Rect) {
        viewLeft = bounds.left+0f
        viewTop = bounds.top+0f
        viewRight = bounds.right+0f
        viewBottom = bounds.bottom+0f
        viewL = bounds.left+0f
        viewT = bounds.top+0f
        viewR = bounds.right+0f
        viewB = bounds.bottom+0f
    }

    override fun draw(canvas: Canvas) {

        val rectF = RectF(viewL+margin, viewT, viewR-margin, viewB)
        canvas.drawRoundRect(rectF, viewB/2,viewB/2, mPaint)
        canvas.save()
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun getOpacity(): Int {

        return PixelFormat.UNKNOWN
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    fun setColorTint(mColor: Int) {
        this.mColor = mColor
    }

    fun setColor(mColor: Int) {
        mPaint.color = mColor
        invalidateSelf()
    }

    fun getColor(): Int {
        return mColor
    }

    fun shrinkInLeft(){
        itemAnimationUtil.shrinkInLeft()
        isOpen = false
    }

    fun shrinkInRight(){
        itemAnimationUtil.shrinkInRight()
        isOpen = false
    }

    fun magnifyToRight(){
        itemAnimationUtil.magnifyToRight()
        isOpen = true
    }

    fun magnifyToLeft(){
        itemAnimationUtil.magnifyToLeft()
        isOpen = true
    }

    fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
        val alpha = Math.round(Color.alpha(color) * factor)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    fun getIsOpen(): Boolean{
        return isOpen
    }
}