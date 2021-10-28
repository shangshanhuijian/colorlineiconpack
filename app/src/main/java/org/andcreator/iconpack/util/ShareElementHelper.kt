package org.andcreator.iconpack.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Matrix
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import android.widget.ImageView

/**
 * @author  Andrew
 * @date  2020/3/18 18:54
 */

class ShareElementHelper() {

    private lateinit var imageView: ImageView

    private lateinit var originalImageView: ImageView

    /**
     * 源图片矩阵信息
     */
    private var originalValue = FloatArray(9)

    /**
     * 源ImageView基于屏幕的位置
     */
    private var originalLocation = IntArray(2)

    /**
     * 源图片宽度
     */
    private var originalWidth = 0F

    /**
     * 源图片高度
     */
    private var originalHeight = 0F

    /**
     * 源ImageView宽度
     */
    private var originalViewWidth = 0

    /**
     * 源ImageView高度
     */
    private var originalViewHeight = 0

    /**
     * 目标图片矩阵
     */
    private var targetValue = FloatArray(9)

    /**
     * 目标图片相对于屏幕的位置
     */
    private var targetLocation = IntArray(2)

    /**
     * 目标图片宽度
     */
    private var targetWidth = 0F

    /**
     * 目标图片高度
     */
    private var targetHeight = 0F

    /**
     * 目标ImageView宽度
     */
    private var targetViewWidth = 0

    /**
     * 目标ImageView高度
     */
    private var targetViewHeight = 0

    /**
     * 目标ImageView缩放
     */
    private var scaleX = 1F
    private var scaleY = 1F

    /**
     * 目标ImageView偏移
     */
    private var centerOffsetX = 0F
    private var centerOffsetY = 0F

    /**
     * 动画状态
     */
    private var enter = false
    private lateinit var animator: ViewPropertyAnimator
    private var duration = 300L
    private var startListener: ValueAnimator.AnimatorUpdateListener? = null
    private var endListener: ValueAnimator.AnimatorUpdateListener? = null

    interface EndListener {
        fun onEnd()
    }

    private lateinit var endAnimatorListener: EndListener

    /*
    dp 越小，图标越靠right，dp越大，图标越靠bottom
     */
    fun with(imageId: Int, imageView: ImageView, originalImageView: ImageView): ShareElementHelper {
        this.imageView = imageView
        this.originalImageView = originalImageView
        setImage(imageId)
        initInfo()
        return this
    }

    private fun initInfo() {
        getOriginalInfo()
        imageView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                imageView.viewTreeObserver.removeOnPreDrawListener(this)
                getTargetInfo()
                imageView.translationX = centerOffsetX
                imageView.translationY = centerOffsetY
                imageView.scaleX = scaleX
                imageView.scaleY = scaleY
                animator = imageView.animate()
                start()
                return true
            }
        })
    }

    private fun setImage(imageId: Int) {
        imageView.setImageResource(imageId)
    }

    private fun start() {
        enter = true
        animator.setDuration(duration)
            .scaleX(1f)
            .scaleY(1f)
            .translationX(0f)
            .translationY(0f)
        if (startListener != null) animator.setUpdateListener(startListener)
        animator.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                imageView.visibility = View.VISIBLE
            }
        })
        animator.start()
    }

    fun exitAnimator() {
        enter = false
        animator.setDuration(duration)
            .scaleX(scaleX)
            .scaleY(scaleY)
            .translationX(centerOffsetX)
            .translationY(centerOffsetY)
        if (endListener != null) animator.setUpdateListener(endListener)
        animator.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                reset()
                endAnimatorListener.onEnd()
            }
        })
        animator.start()
    }

    fun setDuration(duration: Long): ShareElementHelper {
        this.duration = duration
        return this
    }

    fun setEndAnimatorListener(endListener: EndListener): ShareElementHelper {
        this.endAnimatorListener = endListener
        return this
    }

    fun setStartListener(listener: ValueAnimator.AnimatorUpdateListener): ShareElementHelper {
        this.startListener = listener
        return this
    }

    fun setEndListener(listener: ValueAnimator.AnimatorUpdateListener): ShareElementHelper {
        this.endListener = listener
        return this
    }

    /**
     * 重置
     */
    private fun reset() {
        imageView.visibility = View.INVISIBLE
        imageView.scaleX = 1F
        imageView.scaleY = 1F
        imageView.translationX = 0F
        imageView.translationY = 0F
    }

    /**
     * 获取源ImageView的信息
     */
    private fun getOriginalInfo() {
        originalImageView.imageMatrix.getValues(originalValue)
        val originalRect = originalImageView.drawable.bounds
        originalWidth = originalRect.width() * originalValue[Matrix.MSCALE_X]
        originalHeight = originalRect.height() * originalValue[Matrix.MSCALE_Y]

        originalViewWidth = originalImageView.width - imageView.paddingLeft - imageView.paddingEnd
        originalViewHeight = originalImageView.height - imageView.paddingTop - imageView.paddingBottom
        originalImageView.getLocationOnScreen(originalLocation)
    }

    private fun getTargetInfo() {
        imageView.imageMatrix.getValues(targetValue)
        val targetRect = imageView.drawable.bounds
        targetWidth = targetRect.width() * targetValue[Matrix.MSCALE_X]
        targetHeight = targetRect.height() * targetValue[Matrix.MSCALE_Y]
        targetViewWidth = imageView.width - imageView.paddingLeft - imageView.paddingEnd
        targetViewHeight = imageView.height - imageView.paddingTop - imageView.paddingBottom
        imageView.getLocationOnScreen(targetLocation)
        init()
    }

    private fun init() {
        scaleX = originalWidth / targetWidth
        scaleY = originalHeight  / targetHeight

        centerOffsetX = originalLocation[0] + originalValue[Matrix.MTRANS_X] + originalViewWidth / 2 - targetLocation[0] - targetValue[Matrix.MSCALE_X] - targetViewWidth / 2
        centerOffsetY = originalLocation[1] + originalValue[Matrix.MTRANS_Y] + originalViewHeight / 2 - targetLocation[1] - targetValue[Matrix.MSCALE_Y] - targetViewHeight / 2

    }
}