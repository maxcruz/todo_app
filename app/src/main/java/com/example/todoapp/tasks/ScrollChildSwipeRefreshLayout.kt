package com.example.todoapp.tasks

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.View

class ScrollChildSwipeRefreshLayout : SwipeRefreshLayout {

    private var mScrollUpChild: View? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun canChildScrollUp(): Boolean {
        val scrollUpChild = mScrollUpChild ?: return false
        return if (mScrollUpChild != null) {
            scrollUpChild.canScrollVertically(-1)
        } else super.canChildScrollUp()
    }

    fun setScrollUpChild(view: View) {
        mScrollUpChild = view
    }

}
