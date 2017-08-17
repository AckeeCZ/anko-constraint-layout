package cz.ackee.anko_constraint_layout

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout

/**
 * @author David Khol [david.khol@ackee.cz]
 * @since 16.8.2017
 **/
@Suppress("MemberVisibilityCanPrivate")
open class _ConstraintSet : ConstraintSet() {

    private val TAG: String = javaClass.simpleName


    var generateIds: Boolean = false
        get() = field
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        set(value) {
            field = value
        }

    val UNDEFINED = Int.MAX_VALUE

    @Suppress("unused")
    val parentId: Int = ConstraintLayout.LayoutParams.PARENT_ID

    val matchConstraintWrap: Int = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_WRAP
    val matchConstraintSpread: Int = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_SPREAD
    val matchConstraintPercent: Int = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_PERCENT

    val LEFT =  ConstraintLayout.LayoutParams.LEFT
    val RIGHT = ConstraintLayout.LayoutParams.RIGHT
    val TOP = ConstraintLayout.LayoutParams.TOP
    val BOTTOM = ConstraintLayout.LayoutParams.BOTTOM
    val BASELINE = ConstraintLayout.LayoutParams.BASELINE
    val START = ConstraintLayout.LayoutParams.START
    val END = ConstraintLayout.LayoutParams.END

    val LEFTS: SideSide = LEFT to LEFT
    val RIGHTS: SideSide = RIGHT to RIGHT
    val TOPS: SideSide = TOP to TOP
    val BOTTOMS: SideSide = BOTTOM to BOTTOM
    val BASELINES: SideSide = BASELINE to BASELINE
    val STARTS: SideSide = START to START
    val ENDS: SideSide = END to END
    val HORIZONTAL: SideSide = SideSide(-1, -1)
    val VERTICAL: SideSide = SideSide(-2, -2)

    val CHAIN_SPREAD = ConstraintLayout.LayoutParams.CHAIN_SPREAD
    val CHAIN_SPREAD_INSIDE = ConstraintLayout.LayoutParams.CHAIN_SPREAD_INSIDE
    val CHAIN_PACKED = ConstraintLayout.LayoutParams.CHAIN_PACKED

    //<editor-fold desc="<< Guidelines definitions >>">
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun guideline(orientation: Int, init: (Int) -> Unit): Int {
        val guideId = View.generateViewId()
        create(guideId, orientation)
        init(guideId)
        return guideId
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun guidelineBegin(orientation: Int, guide: Int) = guideline(orientation) { setGuidelineBegin(it, guide)}
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun guidelineEnd(orientation: Int, guide: Int) = guideline(orientation) { setGuidelineEnd(it, guide) }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun guidelinePercent(orientation: Int, guide: Float) = guideline(orientation) { setGuidelinePercent(it, guide) }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    open fun verticalGuidelineBegin(guide: Int) = guidelineBegin(LinearLayout.VERTICAL, guide)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    open fun verticalGuidelineEnd(guide: Int) = guidelineEnd(LinearLayout.VERTICAL, guide)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    open fun verticalGuidelinePercent(guide: Float) = guidelinePercent(LinearLayout.VERTICAL, guide)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    open fun horizontalGuidelineBegin(guide: Int) = guidelineBegin(LinearLayout.HORIZONTAL, guide)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    open fun horizontalGuidelineEnd(guide: Int) = guidelineEnd(LinearLayout.HORIZONTAL, guide)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    open fun horizontalGuidelinePercent(guide: Float) = guidelinePercent(LinearLayout.HORIZONTAL, guide)
    //</editor-fold>


    fun View.reset(vararg sides: Int) {
        sides.forEach {
            clear(this.id, it)
        }
    }

    fun View.connect(vararg connections: SideSideViewId) {
        connections.forEach { (sides, endId, margin) ->
            if (margin == UNDEFINED) {
                when (sides) {
                    HORIZONTAL -> connectHorizontal(this.id, endId)
                    VERTICAL -> connectVertical(this.id, endId)
                    else -> connect(sides.start of this.id, sides.end of endId)
                }
            } else {
                when (sides) {
                    HORIZONTAL -> connectHorizontal(this.id, endId, margin)
                    VERTICAL -> connectVertical(this.id, endId, margin)
                    else -> connect(sides.start of this.id, sides.end of endId, margin)
                }
            }
        }
    }


    //<editor-fold desc="<< connect() overloads >>">
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun View.generateId() {
        if (id == 0) {
            id = newId()
        }
    }

    private fun generateIds(vararg views: View) {
        if (generateIds && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            views.forEach {
                it.generateId()
            }
        }
    }

    fun connect(start: View, startSide: Int, end: View, endSide: Int) {
        generateIds(start, end)
        connect(start.id, startSide, end.id, endSide)
    }
    fun connect(start: View, startSide: Int, end: View, endSide: Int, margin: Int) {
        generateIds(start, end)
        connect(start.id, startSide, end.id, endSide, margin)
    }

    fun connect(start: View, startSide: Int, endId: Int, endSide: Int) {
        generateIds(start)
        connect(start.id, startSide, endId, endSide)
    }
    fun connect(start: View, startSide: Int, endId: Int, endSide: Int, margin: Int) {
        generateIds(start)
        connect(start.id, startSide, endId, endSide, margin)
    }

    fun connect(startId: Int, startSide: Int, end: View, endSide: Int) {
        generateIds(end)
        connect(startId, startSide, end.id, endSide)
    }
    fun connect(startId: Int, startSide: Int, end: View, endSide: Int, margin: Int) {
        generateIds(end)
        connect(startId, startSide, end.id, endSide, margin)
    }

    fun connect(start: SideViewId, end: SideViewId) {
        connect(start.viewId, start.side, end.viewId, end.side)
    }
    fun connect(start: SideViewId, end: SideViewId, margin: Int) {
        connect(start.viewId, start.side, end.viewId, end.side, margin)
    }
    fun connect(con: SideViewSideView) {
        connect(con.from.view, con.from.side, con.to.view, con.to.side)
    }
    fun connect(con: SideViewSideView, margin: Int) {
        connect(con.from.view, con.from.side, con.to.view, con.to.side, margin)
    }
    //</editor-fold>

    //<editor-fold desc="<< connect***() overloads>>">
    private fun connectBaseline(viewId: Int, targetId: Int, margin: Int) {
        Log.w(TAG, "Baseline connection cannot define margin. Check your definition.")
        connectBaseline(viewId, targetId)
    }
    fun connectBaseline(viewId: Int, targetId: Int) {
        connect(viewId, BASELINE, targetId, BASELINE)
    }
    fun connectHorizontal(viewId: Int, targetId: Int) {
        connect(viewId, START, targetId, START)
        connect(viewId, LEFT, targetId, LEFT)
        connect(viewId, END, targetId, END)
        connect(viewId, RIGHT, targetId, RIGHT)
    }
    fun connectHorizontal(viewId: Int, targetId: Int, margin: Int) {
        connect(viewId, START, targetId, START, margin)
        connect(viewId, LEFT, targetId, LEFT, margin)
        connect(viewId, END, targetId, END, margin)
        connect(viewId, RIGHT, targetId, RIGHT, margin)
    }
    fun connectVertical(viewId: Int, targetId: Int) {
        connect(viewId, TOP, targetId, TOP)
        connect(viewId, BOTTOM, targetId, BOTTOM)
    }
    fun connectVertical(viewId: Int, targetId: Int, margin: Int) {
        connect(viewId, TOP, targetId, TOP, margin)
        connect(viewId, BOTTOM, targetId, BOTTOM, margin)
    }
    //</editor-fold>

    data class SideView(val side: Int, val view: View)
    data class SideViewId(val side: Int, val viewId: Int)
    data class SideViewSide(val sideView: SideView, val side: Int)
    data class SideViewIdSide(val sideView: SideViewId, val side: Int)
    data class SideViewSideView(val from: SideView, val to: SideView)
    data class SideViewSideViewId(val from: SideView, val to: SideViewId)
    data class SideViewIdSideView(val from: SideViewId, val to: SideView)
    data class SideViewIdSideViewId(val from: SideViewId, val to: SideViewId)

    infix fun Int.of(view: View) = SideView(this, view)
    infix fun Int.of(viewId: Int) = SideViewId(this, viewId)
    infix fun SideView.to(side: Int) = SideViewSide(this, side)
    infix fun SideViewId.to(side: Int) = SideViewIdSide(this, side)
    infix fun SideViewSide.of(view: View) = SideViewSideView(sideView, SideView(side, view))
    infix fun SideViewSide.of(viewId: Int) = SideViewSideViewId(sideView, SideViewId(side, viewId))
    infix fun SideViewIdSide.of(view: View) = SideViewIdSideView(sideView, SideView(side, view))
    infix fun SideViewIdSide.of(viewId: Int) = SideViewIdSideViewId(sideView, SideViewId(side, viewId))


    // SideSide* classes are used in view.connect(vararg) methods
    data class SideSide(val start: Int, val end: Int)
    data class SideSideView(val sides: SideSide, val view: View, val margin: Int)
    data class SideSideViewId(val sides: SideSide, val viewId: Int, val margin: Int)

    infix fun Int.to(side: Int) = SideSide(this, side)
    infix fun SideSide.of(view: View) = SideSideView(this, view, UNDEFINED)
    infix fun SideSide.of(viewId: Int) = SideSideViewId(this, viewId, UNDEFINED)
    infix fun SideSideView.with(margin: Int) = SideSideView(sides, view, margin)
    infix fun SideSideViewId.with(margin: Int) = SideSideViewId(sides, viewId, margin)


}


