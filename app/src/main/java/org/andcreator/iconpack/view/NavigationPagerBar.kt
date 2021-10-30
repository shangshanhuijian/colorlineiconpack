package org.andcreator.iconpack.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import org.andcreator.bubbble.drawable.PagerBarDrawable
import org.andcreator.iconpack.Constans.NAVIGATION_ITEM
import org.andcreator.iconpack.R
import org.andcreator.iconpack.adapter.PagerBarAdapter
import org.andcreator.iconpack.bean.PagerBarBean
import org.andcreator.iconpack.util.DisplayUtil
import org.andcreator.iconpack.util.NavigationItemAnimationUtil


/**
 * @author And
 */
class NavigationPagerBar(context: Context, attrs: AttributeSet?,
                         @AttrRes defStyleAttr:Int, @StyleRes defStyleRes:Int) : FrameLayout(context,attrs,defStyleAttr,defStyleRes) {

    private var pagerItemList: ArrayList<View> = ArrayList()
    private var pagerDrawableList: ArrayList<PagerBarDrawable> = ArrayList()
    private lateinit var pagerBarAdapter: PagerBarAdapter
    private lateinit var itemHolder: ArrayList<PagerBarBean>
    private var iconTranslationX = 0f
    private var viewMaxWidth = 0
    private var firstMaxWidth = 0
    private var currentPager = 0
    private var itemTitleAnimationUtils: ArrayList<NavigationItemAnimationUtil> = ArrayList()

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr:Int):this(context,attrs,defStyleAttr,0)

    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)

    constructor(context: Context):this(context,null)

    private fun init(){

        var icon: ImageView
        var title: TextView
        var background: FrameLayout
        var backgroundDrawable: Drawable

        for (viewCount in 0 until pagerBarAdapter.getCount()){
            val child = pagerBarAdapter.createMenuItem(this, viewCount)
            title = child.findViewById(R.id.title)
            icon = child.findViewById(R.id.icon)
            background = child.findViewById(R.id.background)
            backgroundDrawable = PagerBarDrawable()
            backgroundDrawable.setColorTint(itemHolder[viewCount].iconTint)
            background.background = backgroundDrawable
            title.text = itemHolder[viewCount].title
            title.setTextColor(itemHolder[viewCount].iconTint)
            icon.setImageResource(itemHolder[viewCount].icon)

            icon.setOnClickListener {
                clickListener.onClick(viewCount)
            }

            addView(child)
            pagerItemList.add(child)
            itemTitleAnimationUtils.add(NavigationItemAnimationUtil())
            pagerDrawableList.add(backgroundDrawable)
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var maxMargin = 0
        var maxWidth = 0
        for (viewCount in 0 until childCount){

            val child = getChildAt(viewCount)

            if (maxMargin < child.width){
                maxMargin = child.width
            }
        }

        for (viewCount in 0 until childCount){

            maxWidth = (viewCount * iconTranslationX).toInt()+maxMargin

        }
        setMeasuredDimension(maxWidth, heightMeasureSpec)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        viewMaxWidth = 0
        var maxMargin = 0

        iconTranslationX = DisplayUtil.dip2px(context, NAVIGATION_ITEM).toFloat()

        for (viewCount in 0 until childCount){

            val child = getChildAt(viewCount)

            if (maxMargin < child.width){
                maxMargin = child.width
            }
        }

        for (viewCount in 0 until childCount){

            val child = getChildAt(viewCount)

            viewMaxWidth = (viewCount * iconTranslationX).toInt() + maxMargin

            if (viewCount == 0){
                firstMaxWidth = viewMaxWidth
            }

            child.layout((viewCount * iconTranslationX).toInt(),0, viewMaxWidth, child.measuredHeight)
            child.requestLayout()
        }
    }


    fun setAdapter(pagerBarAdapter: PagerBarAdapter){
        this.pagerBarAdapter = pagerBarAdapter
        itemHolder = pagerBarAdapter.getHolder()
        init()
    }

    fun onAnimationOffset(index: Int){
        for (viewCount in 0 until pagerBarAdapter.getCount()){
            val background: FrameLayout = pagerItemList[viewCount].findViewById(R.id.background)
            val title: TextView = pagerItemList[viewCount].findViewById(R.id.title)
            val icon: ImageView = pagerItemList[viewCount].findViewById(R.id.icon)

            //如果是选中的item
            if (viewCount == index){
                //显示Title
                if (!itemHolder[index].isTitleAnim){
                    itemHolder[index].isTitleAnim = true

                    //判断background显示方向
                    if (currentPager < index){
                        //向左展开Drawable
                        pagerDrawableList[viewCount].magnifyToLeft()
                        showInAlpha(index,(background.width-iconTranslationX).toInt(),title,icon)
                    }else{
                        //向右展开Drawable
                        pagerDrawableList[viewCount].magnifyToRight()
                        showMoveInAlpha(index,(background.width-iconTranslationX).toInt(),title,icon)
                    }

                }

                //判断图标是否做过动画，还原图标位置
                if (itemHolder[index].isIconAnim){
                    moveLeftInIcon(icon,background.width,viewCount)
                    itemHolder[index].isIconAnim = false
                }

            }else{
                //如果是未选中的item

                //隐藏title
                if (itemHolder[viewCount].isTitleAnim){
                    itemHolder[viewCount].isTitleAnim = false

                    //判断background隐藏方向
                    if (currentPager < index){
                        hideLeftDrawable(viewCount)
                        hideMoveInAlpha(viewCount,(background.width-iconTranslationX).toInt(),title,icon)
                    }else{
                        hideRightDrawable(viewCount)
                        hideInAlpha(viewCount,(background.width-iconTranslationX).toInt(),title,icon)
                    }
                }

                background.post {
                    //判断图标移动方向
                    if (viewCount > index && !itemHolder[viewCount].isIconAnim){
                        moveRightInIcon(icon,background.width,viewCount)
                        itemHolder[viewCount].isIconAnim = true
                    }else if (viewCount < index && itemHolder[viewCount].isIconAnim){
                        moveLeftInIcon(icon,background.width,viewCount)
                        itemHolder[viewCount].isIconAnim = false
                    }else if (viewCount < index && itemHolder[viewCount].isIconAnim){
                        moveLeftInIcon(icon,background.width,viewCount)
                        itemHolder[viewCount].isIconAnim = false
                    }else if (viewCount > index && !itemHolder[viewCount].isIconAnim){
                        moveRightInIcon(icon,background.width,viewCount)
                        itemHolder[viewCount].isIconAnim = true
                    }
                }
            }

        }
        currentPager = index
    }

    fun onAnimation(index: Int){
        for (viewCount in 0 until pagerBarAdapter.getCount()){
            val background: FrameLayout = pagerItemList[viewCount].findViewById(R.id.background)
            val title: TextView = pagerItemList[viewCount].findViewById(R.id.title)
            val icon: ImageView = pagerItemList[viewCount].findViewById(R.id.icon)

            //如果是选中的item
            if (viewCount == index){
                //显示Title
                if (!itemHolder[index].isTitleAnim){
                    itemHolder[index].isTitleAnim = true

                    //判断background显示方向
                    if (currentPager < index){
                        //向左展开Drawable
                        pagerDrawableList[viewCount].magnifyToLeft()
                        showInAlpha(index,(background.width-iconTranslationX).toInt(),title,icon)
                    }else{
                        //向右展开Drawable
                        pagerDrawableList[viewCount].magnifyToRight()
                        showMoveInAlpha(index,(background.width-iconTranslationX).toInt(),title,icon)
                    }

                }

                //判断图标是否做过动画，还原图标位置
                if (itemHolder[index].isIconAnim){
                    moveLeftInIcon(icon,background.width,viewCount)
                    itemHolder[index].isIconAnim = false
                }

            }else{
                //如果是未选中的item

                //隐藏title
                if (itemHolder[viewCount].isTitleAnim){
                    itemHolder[viewCount].isTitleAnim = false

                    //判断background隐藏方向
                    if (currentPager < index){
                        hideLeftDrawable(viewCount)
                        hideMoveInAlpha(viewCount,(background.width-iconTranslationX).toInt(),title,icon)
                    }else{
                        hideRightDrawable(viewCount)
                        hideInAlpha(viewCount,(background.width-iconTranslationX).toInt(),title,icon)
                    }

                }

                //判断图标移动方向
                if (currentPager > index && viewCount > index && !itemHolder[viewCount].isIconAnim){
                    moveRightInIcon(icon,background.width,viewCount)
                    itemHolder[viewCount].isIconAnim = true
                }else if (currentPager > index && viewCount < index && itemHolder[viewCount].isIconAnim){
                    moveLeftInIcon(icon,background.width,viewCount)
                    itemHolder[viewCount].isIconAnim = false
                }else if (currentPager < index && viewCount < index && itemHolder[viewCount].isIconAnim){
                    moveLeftInIcon(icon,background.width,viewCount)
                    itemHolder[viewCount].isIconAnim = false
                }else if (currentPager < index && viewCount > index && !itemHolder[viewCount].isIconAnim){
                    moveRightInIcon(icon,background.width,viewCount)
                    itemHolder[viewCount].isIconAnim = true
                }
            }

        }
        currentPager = index
    }

    /**
     * 向左隐藏Drawable
     */
    private fun hideRightDrawable(position: Int){
        if (pagerDrawableList[position].getIsOpen()){
            pagerDrawableList[position].shrinkInRight()
        }
    }

    /**
     * 向右隐藏Drawable
     */
    private fun hideLeftDrawable(position: Int){
        if (pagerDrawableList[position].getIsOpen()){
            pagerDrawableList[position].shrinkInLeft()
        }
    }

    /**
     * 显示Title
     */
    private fun showInAlpha(position: Int, size: Int, view: View, viewIcon: ImageView){
        itemTitleAnimationUtils[position].setPagerBarTitle(view).setPagerBarIcon(viewIcon).setSelectedIconColor(itemHolder[position].iconTint).showInTitle(false)
    }

    /**
     * 隐藏Title
     */
    private fun hideInAlpha(position: Int, size: Int, view: View, viewIcon: ImageView){
        itemTitleAnimationUtils[position].setPagerBarTitle(view).setPagerBarIcon(viewIcon).setSelectedIconColor(itemHolder[position].iconTint).hideInTitle(false)

        Log.e("不移动Title","不移动Title")
    }

    /**
     * 显示移动Title
     */
    private fun showMoveInAlpha(position: Int, size: Int, view: View, viewIcon: ImageView){
        itemTitleAnimationUtils[position].setPagerBarTitle(view).setTitleTranslationSize(size).setSelectedIconColor(itemHolder[position].iconTint).setPagerBarIcon(viewIcon).showInTitle(true)
    }

    /**
     * 隐藏移动Title
     */
    private fun hideMoveInAlpha(position: Int, size: Int, view: View, viewIcon: ImageView){
        itemTitleAnimationUtils[position].setPagerBarTitle(view).setTitleTranslationSize(size).setSelectedIconColor(itemHolder[position].iconTint).setPagerBarIcon(viewIcon).hideInTitle(true)
    }

    /**
     * 向右移动图标
     */
    private fun moveRightInIcon(view: ImageView, size: Int,viewCount: Int){
        itemTitleAnimationUtils[viewCount].setPagerBarIcon(view).setIconTranslationSize(size).moveRightInIcon()
    }

    /**
     * 向左移回图标
     */
    private fun moveLeftInIcon(view: ImageView, size: Int,viewCount: Int){
        itemTitleAnimationUtils[viewCount].setPagerBarIcon(view).setIconTranslationSize(size).moveLeftInIcon()
    }

    interface OnItemClickListener{
        fun onClick(position: Int)
    }

    private lateinit var clickListener: OnItemClickListener

    fun setClickListener(clickListener: OnItemClickListener) {
        this.clickListener = clickListener
    }

}