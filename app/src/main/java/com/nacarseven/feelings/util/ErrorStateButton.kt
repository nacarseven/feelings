package com.nacarseven.feelings.util

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import com.nacarseven.feelings.R
import kotlin.properties.Delegates

private val BUTTON_STATE_ERROR = intArrayOf(R.attr.state_error)

/**
 * A button that has a custom error state, enabling background tint
 * and src drawables to respond to state_error
 */
class ErrorStateButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FloatingActionButton(context, attrs, defStyleAttr) {

    var error: Boolean by Delegates.observable(false) { _, o, n ->
        if (o != n) refreshDrawableState()
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {

        // isAttachedToWindow is needed because onCreateDrawableState is called
        // from inside the super constructor, which means that, by then,
        // the error property is uninitialized, causing a NPE
        if (isAttachedToWindow && error) {
            val states = super.onCreateDrawableState(extraSpace + 1)
            return mergeDrawableStates(states, BUTTON_STATE_ERROR)
        }

        return super.onCreateDrawableState(extraSpace)
    }
}