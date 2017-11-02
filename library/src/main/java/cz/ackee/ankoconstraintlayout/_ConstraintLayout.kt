@file:Suppress("NOTHING_TO_INLINE")

package cz.ackee.ankoconstraintlayout

import android.app.Activity
import android.content.Context
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
 * @author David Khol [david.khol@ackee.cz]
 * @since 9.8.2017
 **/
@Suppress("NOTHING_TO_INLINE", "unused", "MemberVisibilityCanPrivate")
open class _ConstraintLayout(ctx: Context) : ConstraintLayout(ctx) {

    val matchConstraint: Int = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
    @Deprecated("You shouldn't use match_parent inside of ConstraintLayout")
    val matchParent: Int = ConstraintLayout.LayoutParams.MATCH_PARENT
    val wrapContent: Int = ConstraintLayout.LayoutParams.WRAP_CONTENT

    /**
     * If true, automatically generate unique ids for views added to the constraint layout.
     * WARNING: Do not manually change child view id after it has been added.
     */
    var generateIds = true

    /**
     * Generates unique ID for a view. This is useful when we need to define ids for views
     * used in constraint layout to create constraints.
     * Warning: Don't rely on this function for views which should persist their state. Views will
     * get assigned a new generated ID after a configuration change and thus automatic instance
     * state restore won't work properly.
     */
    override fun onViewAdded(view: View) {
        // The constraint layout stores references through view ids and when the view's id gets
        // updated, constraint layout won't be able to find it anymore through the new id.
        // To fight this, update the newly added view's id before the constraint layout stores
        // the reference of the view.
        // (Another possible way to resolve this issue is to remove the view, change view's id
        // and add the view again. Doing so might cause rendering problems though. Order of views
        // being rendered might change.)
        if (generateIds) {
            if (view.id == View.NO_ID) {
                view.id = ViewIdGenerator.newId()
            }
        }
        super.onViewAdded(view)
    }

    /**
     * Create a constraint set from the current constraint layout, initialize it and
     * apply the changes back to the constraint layout
     */
    inline fun constraints(init: _ConstraintSet.() -> Unit): _ConstraintSet {
        val constraintSet = _ConstraintSet()
        constraintSet.clone(this)
        constraintSet.init()
        constraintSet.applyTo(this)
        return constraintSet
    }

    /**
     * Create a constraint set from the current constraint layout and initialize it.
     * Changes are NOT applied back to the constraint layout.
     * This might be useful when you define multiple constraint sets and don't want
     * the changes from the first constraint set to be propagated to other ones.
     */
    inline fun prepareConstraints(init: _ConstraintSet.() -> Unit): _ConstraintSet {
        val constraintSet = _ConstraintSet()
        constraintSet.clone(this)
        constraintSet.init()
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

inline fun Activity.themedConstraintLayout(theme: Int = 0): _ConstraintLayout = themedConstraintLayout(theme) {}
inline fun Activity.themedConstraintLayout(theme: Int = 0, init: (@AnkoViewDslMarker _ConstraintLayout).() -> Unit): _ConstraintLayout {
    return ankoView(::_ConstraintLayout, theme) { init() }
}
//</editor-fold>
