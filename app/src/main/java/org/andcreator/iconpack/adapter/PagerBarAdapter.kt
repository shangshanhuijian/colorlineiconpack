package org.andcreator.iconpack.adapter

import android.view.View
import android.view.ViewGroup
import org.andcreator.iconpack.bean.PagerBarBean


/**
 * @author And
 */

abstract class PagerBarAdapter {

    abstract fun createMenuItem(parent: ViewGroup, index: Int): View

    abstract fun getCount(): Int

    abstract fun getHolder(): ArrayList<PagerBarBean>
}