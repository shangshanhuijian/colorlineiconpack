package org.andcreator.iconpack.util

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import org.andcreator.bubbble.drawable.PagerBarDrawable
import org.andcreator.iconpack.Constans
import org.andcreator.iconpack.IconPack

/**
 * @author And
 */
class NavigationItemAnimationUtil {

    companion object{
        private const val DEF_DURATION = 300L
        private const val OPEN = 0F
        private const val CLOSE = 1F
    }

    private val iconTranslationX = DisplayUtil.dip2px(IconPack.context, Constans.NAVIGATION_ITEM).toFloat()

    private var itemDrawableRightValue = 0f

    private var itemDrawableLeftValue = 0f

    private var itemIconValue = 0f

    private var itemTitleValue = 0f

    private lateinit var pagerBarDrawable: PagerBarDrawable

    private lateinit var viewTitle: View

    private lateinit var viewIcon: ImageView

    private var iconTranslationSize = 0

    private var titleTranslationSize = 0

    private var titleIsMove = true

    private var selectedIconColor = (0xff9575cd).toInt()

    private var unselectedIconColor = (0xff747e8a).toInt()

    /**
     * 右边伸缩动画
     */
    private var animationDrawableRight = ValueAnimator().apply {
        addUpdateListener { animation ->
            itemDrawableRightValue = animation.animatedValue as Float
            pagerBarDrawable.viewR = pagerBarDrawable.viewRight - (itemDrawableRightValue * (pagerBarDrawable.viewRight - DisplayUtil.dip2px(IconPack.context,
                Constans.NAVIGATION_ITEM
            )))

            pagerBarDrawable.setColor(adjustAlpha(pagerBarDrawable.getColor(),(1-itemDrawableRightValue) * 0.2f))
            pagerBarDrawable.invalidateSelf()
        }
        duration = DEF_DURATION
    }

    /**
     * 左边伸缩动画
     */
    private var animationDrawableLeft = ValueAnimator().apply {
        addUpdateListener { animation ->
            itemDrawableLeftValue = animation.animatedValue as Float
            pagerBarDrawable.viewL = pagerBarDrawable.viewLeft + (itemDrawableLeftValue * (pagerBarDrawable.viewRight - DisplayUtil.dip2px(IconPack.context,
                Constans.NAVIGATION_ITEM
            )))

            pagerBarDrawable.setColor(adjustAlpha(pagerBarDrawable.getColor(),(1-itemDrawableLeftValue) * 0.2f))
            pagerBarDrawable.invalidateSelf()
        }
        duration = DEF_DURATION
    }

    /**
     * Title显隐动画
     */
    private var viewTitleAnimation = ValueAnimator().apply{
        addUpdateListener { animation ->
            itemTitleValue = animation.animatedValue as Float
            if (titleIsMove){
                viewTitle.translationX = (0-titleTranslationSize) - (itemTitleValue * (0-titleTranslationSize))
            }
            viewTitle.alpha = itemTitleValue

            val iconColor = TintUtil.colorTransition(unselectedIconColor, selectedIconColor, itemTitleValue)
            TintUtil.tintDrawable(viewIcon.drawable).setColor(iconColor).tint()

        }
        duration = DEF_DURATION
    }

    /**
     * Icon位移动画
     */
    private var viewIconAnimation = ValueAnimator().apply{
        addUpdateListener { animation ->
            itemIconValue = animation.animatedValue as Float
            viewIcon.translationX = itemIconValue * (iconTranslationSize-iconTranslationX)
        }
        duration = DEF_DURATION
    }

    /**
     * 右边从满值缩减到一个图标的大小
     */
    fun shrinkInLeft(){
        pagerBarDrawable.viewL = pagerBarDrawable.viewLeft
        animationDrawableRight.cancel()
        if (itemDrawableRightValue == CLOSE){
            itemDrawableRightValue = OPEN
        }
        animationDrawableRight.setFloatValues(itemDrawableRightValue, CLOSE)
        animationDrawableRight.duration = (DEF_DURATION * (CLOSE - itemDrawableRightValue)).toLong()
        animationDrawableRight.start()
    }

    /**
     * 右边从一个图标的大小展开到原始大小
     */
    fun magnifyToRight(){
        pagerBarDrawable.viewL = pagerBarDrawable.viewLeft
        animationDrawableRight.cancel()
        if (itemDrawableRightValue == OPEN){
            itemDrawableRightValue = CLOSE
        }
        animationDrawableRight.setFloatValues(itemDrawableRightValue, OPEN)
        animationDrawableRight.duration = (DEF_DURATION * (itemDrawableRightValue - OPEN)).toLong()
        animationDrawableRight.start()
    }

    /**
     * 左边从满值缩减到一个图标的大小
     */
    fun shrinkInRight(){

        pagerBarDrawable.viewR = pagerBarDrawable.viewRight
        animationDrawableLeft.cancel()
        if (itemDrawableLeftValue == CLOSE){
            itemDrawableLeftValue = OPEN
        }
        animationDrawableLeft.setFloatValues(itemDrawableLeftValue, CLOSE)
        animationDrawableLeft.duration = (DEF_DURATION * (CLOSE - itemDrawableLeftValue)).toLong()
        animationDrawableLeft.start()
    }

    /**
     * 左边从一个图标的大小展开到原始大小
     */
    fun magnifyToLeft(){

        pagerBarDrawable.viewR = pagerBarDrawable.viewRight
        animationDrawableLeft.cancel()
        if (itemDrawableLeftValue == OPEN){
            itemDrawableLeftValue = CLOSE
        }
        animationDrawableLeft.setFloatValues(itemDrawableLeftValue, OPEN)
        animationDrawableLeft.duration = (DEF_DURATION * (itemDrawableLeftValue - OPEN)).toLong()
        animationDrawableLeft.start()
    }


    fun setPagerBarDrawable(pagerBarDrawable: PagerBarDrawable){
        this.pagerBarDrawable = pagerBarDrawable
    }


    fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
        val alpha = Math.round(Color.alpha(color) * factor)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    fun setPagerBarTitle(viewTitle: View): NavigationItemAnimationUtil{
        this.viewTitle = viewTitle
        return this
    }

    fun showInTitle(isMove: Boolean){
        this.titleIsMove = isMove
        viewTitleAnimation.cancel()
        if (itemTitleValue == CLOSE){
            itemTitleValue = OPEN
        }
        viewTitleAnimation.setFloatValues(itemTitleValue, CLOSE)
        viewTitleAnimation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                itemTitleValue = CLOSE
                titleIsMove = true
            }

            override fun onAnimationStart(animation: Animator?) {
                if (!titleIsMove){
                    viewTitle.translationX = 0f
                }
                viewTitle.visibility = View.VISIBLE
            }
        })

        viewTitleAnimation.duration = (DEF_DURATION * (CLOSE - itemTitleValue)).toLong()
        viewTitleAnimation.start()

    }

    fun hideInTitle(isMove: Boolean){
        this.titleIsMove = isMove
        viewTitleAnimation.cancel()
        if (itemTitleValue == OPEN){
            itemTitleValue = CLOSE
        }
        viewTitleAnimation.setFloatValues(itemTitleValue, OPEN)
        viewTitleAnimation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {

//                viewTitle.visibility = View.INVISIBLE
                itemTitleValue = OPEN
                titleIsMove = true
            }

            override fun onAnimationStart(animation: Animator?) {
                if (!titleIsMove){
                    viewTitle.translationX = 0f
                }
            }
        })

        viewTitleAnimation.duration = (DEF_DURATION * (itemTitleValue - OPEN)).toLong()
        viewTitleAnimation.start()
    }


    fun setPagerBarIcon(viewIcon: ImageView): NavigationItemAnimationUtil{
        this.viewIcon = viewIcon
        return this
    }

    fun setIconTranslationSize(iconTranslationSize: Int): NavigationItemAnimationUtil{
        this.iconTranslationSize = iconTranslationSize
        return this
    }

    fun setTitleTranslationSize(titleTranslationSize: Int): NavigationItemAnimationUtil{
        this.titleTranslationSize = titleTranslationSize
        return this
    }

    fun setSelectedIconColor(color: Int): NavigationItemAnimationUtil{
        selectedIconColor = color
        return this
    }

    fun moveRightInIcon(){
        viewIconAnimation.cancel()
        if (itemIconValue == CLOSE){
            itemIconValue = OPEN
        }
        viewIconAnimation.setFloatValues(itemIconValue, CLOSE)

        viewIconAnimation.duration = (DEF_DURATION * (CLOSE - itemIconValue)).toLong()
        viewIconAnimation.start()
    }


    fun moveLeftInIcon(){

        viewIconAnimation.cancel()
        if (itemIconValue == OPEN){
            itemIconValue = CLOSE
        }
        viewIconAnimation.setFloatValues(itemIconValue, OPEN)

        viewIconAnimation.duration = (DEF_DURATION * (itemIconValue - OPEN)).toLong()
        viewIconAnimation.start()
    }
}