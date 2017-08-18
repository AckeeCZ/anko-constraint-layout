@file:Suppress("NOTHING_TO_INLINE")

package cz.ackee.anko_constraint_layout

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import org.jetbrains.anko.AnkoViewDslMarker
import org.jetbrains.anko.custom.ankoView


/**
 * Anko extensions for ConstraintLayout
 *
 * @see [constraintlayout.com](https://constraintlayout.com/)
 * @see [developer.android.com](https://developer.android.com/training/constraint-layout/index.html)
 *
 * @author David Khol [david.khol@ackee.cz]
 * @since 9.8.2017
 **/
@Suppress("NOTHING_TO_INLINE")
open class _ConstraintLayout(ctx: Context) : ConstraintLayout(ctx) {

    @Suppress("unused")
    val matchConstraint: Int = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
    @Suppress("unused")
    val matchParent: Int = ConstraintLayout.LayoutParams.MATCH_PARENT
    @Suppress("unused")
    val wrapContent: Int = ConstraintLayout.LayoutParams.WRAP_CONTENT


    // TODO: groups
    // TODO: barrier
    // TODO: placeholders

    // TODO: placeholders

    // TODO: chains

    // TODO: aspect ratio
    // TODO: percent dimensions

    /**
     * TODO: generateIds is not generating ids in all cases!
     */
    inline fun constraints(generateIds: Boolean = false, init: _ConstraintSet.() -> Unit): _ConstraintSet {
        val constraintSet = _ConstraintSet()
        constraintSet.clone(this)
        constraintSet.generateIds = generateIds
        constraintSet.init()
        constraintSet.applyTo(this)
        return constraintSet
    }


    //<editor-fold desc="<< lparams() overloads >>">
    inline fun <T : View> T.lparams(
            c: Context?,
            attrs: AttributeSet?,
            init: ConstraintLayout.LayoutParams.() -> Unit
    ): T {
        val layoutParams = ConstraintLayout.LayoutParams(c!!, attrs!!)
        layoutParams.init()
        layoutParams.validate()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T : View> T.lparams(
            c: Context?,
            attrs: AttributeSet?
    ): T {
        val layoutParams = ConstraintLayout.LayoutParams(c!!, attrs!!)
        layoutParams.validate()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T : View> T.lparams(
            width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
            height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
            init: ConstraintLayout.LayoutParams.() -> Unit
    ): T {
        val layoutParams = ConstraintLayout.LayoutParams(width, height)
        layoutParams.init()
        layoutParams.validate()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T : View> T.lparams(
            width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
            height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
    ): T {
        val layoutParams = ConstraintLayout.LayoutParams(width, height)
        layoutParams.validate()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T : View> T.lparams(
            source: ViewGroup.LayoutParams?,
            init: ConstraintLayout.LayoutParams.() -> Unit
    ): T {
        val layoutParams = ConstraintLayout.LayoutParams(source!!)
        layoutParams.init()
        layoutParams.validate()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T : View> T.lparams(
            source: ViewGroup.LayoutParams?
    ): T {
        val layoutParams = ConstraintLayout.LayoutParams(source!!)
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T : View> T.lparams(
            source: ViewGroup.MarginLayoutParams?,
            init: ConstraintLayout.LayoutParams.() -> Unit
    ): T {
        val layoutParams = ConstraintLayout.LayoutParams(source!!)
        layoutParams.init()
        layoutParams.validate()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T : View> T.lparams(
            source: ViewGroup.MarginLayoutParams?
    ): T {
        val layoutParams = ConstraintLayout.LayoutParams(source!!)
        layoutParams.validate()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T : View> T.lparams(
            source: ConstraintLayout.LayoutParams?,
            init: ConstraintLayout.LayoutParams.() -> Unit
    ): T {
        val layoutParams = ConstraintLayout.LayoutParams(source!!)
        layoutParams.init()
        layoutParams.validate()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T : View> T.lparams(
            source: ConstraintLayout.LayoutParams?
    ): T {
        val layoutParams = ConstraintLayout.LayoutParams(source!!)
        layoutParams.validate()
        this@lparams.layoutParams = layoutParams
        return this
    }
    //</editor-fold>
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
inline fun newId(): Int {
    return View.generateViewId()
}


//<editor-fold desc="<< constraintLayout() extensions >>">
inline fun ViewManager.constraintLayout(): _ConstraintLayout = constraintLayout {}
inline fun ViewManager.constraintLayout(init: (@AnkoViewDslMarker _ConstraintLayout).() -> Unit): _ConstraintLayout {
    return ankoView(::_ConstraintLayout, theme = 0) { init() }
}

inline fun ViewManager.themedConstraintLayout(theme: Int = 0): _ConstraintLayout = themedConstraintLayout(theme) {}
inline fun ViewManager.themedConstraintLayout(theme: Int = 0, init: (@AnkoViewDslMarker _ConstraintLayout).() -> Unit): _ConstraintLayout {
    return ankoView(::_ConstraintLayout, theme) { init() }
}

inline fun Context.constraintLayout(): _ConstraintLayout = constraintLayout {}
inline fun Context.constraintLayout(init: (@AnkoViewDslMarker _ConstraintLayout).() -> Unit): _ConstraintLayout {
    return ankoView(::_ConstraintLayout, theme = 0) { init() }
}

inline fun Context.themedConstraintLayout(theme: Int = 0): _ConstraintLayout = themedConstraintLayout(theme) {}
inline fun Context.themedConstraintLayout(theme: Int = 0, init: (@AnkoViewDslMarker _ConstraintLayout).() -> Unit): _ConstraintLayout {
    return ankoView(::_ConstraintLayout, theme) { init() }
}

inline fun Activity.constraintLayout(): _ConstraintLayout = constraintLayout {}
inline fun Activity.constraintLayout(init: (@AnkoViewDslMarker _ConstraintLayout).() -> Unit): _ConstraintLayout {
    return ankoView(::_ConstraintLayout, theme = 0) { init() }
}
//</editor-fold>
