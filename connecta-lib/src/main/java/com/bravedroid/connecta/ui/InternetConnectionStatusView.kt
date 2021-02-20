package com.bravedroid.connecta.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import com.bravedroid.connecta.R

class InternetConnectionStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_NOT_CONNECTED_STATUS_TEXT = "No Connection "
        private const val DEFAULT_CONNECTED_STATUS_TEXT = "Back Online "
        private const val DEFAULT_CONNECTED_STATUS_COLOR = Color.GREEN
        private const val DEFAULT_NOT_CONNECTED_STATUS_COLOR = Color.RED
        private const val DEFAULT_NOT_CONNECTED_STATUS_TEXT_COLOR = Color.BLACK
    }

    private data class Input(
        internal val connectedStatusText: String,
        internal val notConnectedStatusText: String,
        @ColorInt
        internal val colorConnectedStatus: Int,
        @ColorInt
        internal val colorNotConnectedStatus: Int,
        @ColorInt
        internal val textColorConnectedStatus: Int,
        @ColorInt
        internal val textColorNotConnectedStatus: Int,
    )

    private val successAnimatorSet: AnimatorSet
    private val failAnimatorSet: AnimatorSet
    private val animatorsList: List<AnimatorSet>

    private val input: Input

    init {
        successAnimatorSet = createSuccessAnimatorSet()
        failAnimatorSet = createFailAnimatorSet()
        animatorsList = listOf(successAnimatorSet, failAnimatorSet)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Connecta_InternetConnectionStatusView,
            defStyleAttr,
            0,
        ).let { typedArray ->
            input = Input(
                connectedStatusText = typedArray.getString(
                    R.styleable.Connecta_InternetConnectionStatusView_textConnectedStatus,
                ) ?: DEFAULT_CONNECTED_STATUS_TEXT,
                notConnectedStatusText = typedArray.getString(
                    R.styleable.Connecta_InternetConnectionStatusView_textNotConnectedStatus,
                ) ?: DEFAULT_NOT_CONNECTED_STATUS_TEXT,
                colorConnectedStatus = typedArray.getColor(
                    R.styleable.Connecta_InternetConnectionStatusView_colorConnectedStatus,
                    DEFAULT_NOT_CONNECTED_STATUS_COLOR,
                ),
                colorNotConnectedStatus = typedArray.getColor(
                    R.styleable.Connecta_InternetConnectionStatusView_colorNotConnectedStatus,
                    DEFAULT_CONNECTED_STATUS_COLOR,
                ),
                textColorConnectedStatus = typedArray.getColor(
                    R.styleable.Connecta_InternetConnectionStatusView_textColorConnectedStatus,
                    DEFAULT_NOT_CONNECTED_STATUS_TEXT_COLOR,
                ),
                textColorNotConnectedStatus = typedArray.getColor(
                    R.styleable.Connecta_InternetConnectionStatusView_textColorNotConnectedStatus,
                    DEFAULT_NOT_CONNECTED_STATUS_TEXT_COLOR,
                ),
            )
            typedArray.recycle()
        }
    }

    private fun createFailAnimatorSet(): AnimatorSet = AnimatorSet().apply {
        play(
            ObjectAnimator.ofFloat(
                this@InternetConnectionStatusView,
                View.ALPHA,
                0f, 1f
            ).apply {
                duration = 500
            })

        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@InternetConnectionStatusView.isVisible = true
                removeAllListeners()
            }
        })
    }

    private fun createSuccessAnimatorSet() = (AnimatorSet()).apply {
        playSequentially(
            ObjectAnimator.ofFloat(
                this@InternetConnectionStatusView,
                View.ALPHA,
                0f, 1f
            ).apply {
                duration = 500
            },
            ObjectAnimator.ofFloat(
                this@InternetConnectionStatusView,
                View.ALPHA,
                1f, 1f
            ).apply {
                duration = 1000
            },
            ObjectAnimator.ofFloat(
                this@InternetConnectionStatusView,
                View.ALPHA,
                1f, 0f
            ).apply {
                duration = 500
            }
        )
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@InternetConnectionStatusView.isVisible = false
                removeAllListeners()
            }
        })
    }

    fun showNotConnectedStatusUi() {
        animatorsList.forEach {
            it.removeAllListeners()
            it.cancel()
        }
        apply {
            setBackgroundColor(input.colorNotConnectedStatus)
            text = input.notConnectedStatusText
            setTextColor(input.textColorNotConnectedStatus)
            isVisible = true
        }
        failAnimatorSet.start()
    }

    fun showConnectedStatusUi() {
        animatorsList.forEach {
            it.removeAllListeners()
            it.cancel()
        }
        apply {
            setBackgroundColor(input.colorConnectedStatus)
            text = input.connectedStatusText
            setTextColor(input.textColorConnectedStatus)
            isVisible = true
        }
        successAnimatorSet.start()
    }
}
