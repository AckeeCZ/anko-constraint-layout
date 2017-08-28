package cz.ackee.anko_constraint_layout

import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import cz.ackee.anko_constraint_layout.ViewIdGenerator.newId


typealias Side = Int
typealias ViewId = Int
typealias ChainType = Int


/**
 * @author David Khol [david.khol@ackee.cz]
 * @since 16.8.2017
 **/
@Suppress("MemberVisibilityCanPrivate", "unused")
open class _ConstraintSet : ConstraintSet() {

    private val TAG: String = javaClass.simpleName
    private val UNDEFINED = Int.MAX_VALUE

    var generateIds: Boolean = false

    val parentId: ViewId = ConstraintLayout.LayoutParams.PARENT_ID

    val MATCH_CONSTRAINT_WRAP = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_WRAP
    val MATCH_CONSTRAINT_SPREAD = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_SPREAD
    val MATCH_CONSTRAINT_PERCENT = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_PERCENT

    val LEFT: Side =  ConstraintLayout.LayoutParams.LEFT
    val RIGHT: Side = ConstraintLayout.LayoutParams.RIGHT
    val TOP: Side = ConstraintLayout.LayoutParams.TOP
    val BOTTOM: Side = ConstraintLayout.LayoutParams.BOTTOM
    val BASELINE: Side = ConstraintLayout.LayoutParams.BASELINE
    val START: Side = ConstraintLayout.LayoutParams.START
    val END: Side = ConstraintLayout.LayoutParams.END

    val LEFTS: SideSide = LEFT to LEFT
    val RIGHTS: SideSide = RIGHT to RIGHT
    val TOPS: SideSide = TOP to TOP
    val BOTTOMS: SideSide = BOTTOM to BOTTOM
    val BASELINES: SideSide = BASELINE to BASELINE
    val STARTS: SideSide = START to START
    val ENDS: SideSide = END to END
    val HORIZONTAL: SideSide = SideSide(-1, -1)
    val VERTICAL: SideSide = SideSide(-2, -2)
    val ALL: SideSide = SideSide(-3, -3)

    val CHAIN_SPREAD: ChainType = ConstraintLayout.LayoutParams.CHAIN_SPREAD
    val CHAIN_SPREAD_INSIDE: ChainType = ConstraintLayout.LayoutParams.CHAIN_SPREAD_INSIDE
    val CHAIN_PACKED: ChainType = ConstraintLayout.LayoutParams.CHAIN_PACKED

    //<editor-fold desc="<< Guidelines definitions >>">
    private fun guideline(orientation: Int, init: (Int) -> Unit): Int {
        val guideId = newId()
        create(guideId, orientation)
        init(guideId)
        return guideId
    }

    private fun guidelineBegin(orientation: Int, guide: Int) = guideline(orientation) { setGuidelineBegin(it, guide)}
    private fun guidelineEnd(orientation: Int, guide: Int) = guideline(orientation) { setGuidelineEnd(it, guide) }
    private fun guidelinePercent(orientation: Int, guide: Float) = guideline(orientation) { setGuidelinePercent(it, guide) }

    open fun verticalGuidelineBegin(guide: Int) = guidelineBegin(LinearLayout.VERTICAL, guide)
    open fun verticalGuidelineEnd(guide: Int) = guidelineEnd(LinearLayout.VERTICAL, guide)
    open fun verticalGuidelinePercent(guide: Float) = guidelinePercent(LinearLayout.VERTICAL, guide)
    open fun horizontalGuidelineBegin(guide: Int) = guidelineBegin(LinearLayout.HORIZONTAL, guide)
    open fun horizontalGuidelineEnd(guide: Int) = guidelineEnd(LinearLayout.HORIZONTAL, guide)
    open fun horizontalGuidelinePercent(guide: Float) = guidelinePercent(LinearLayout.HORIZONTAL, guide)
    //</editor-fold>


    fun View.reset(vararg sides: Side) {
        sides.forEach {
            clear(this.id, it)
        }
    }

    fun View.connect(vararg connections: SideSideViewId) {
        generateId()

        connections.forEach {
            val sides = it.sides
            val endId = it.viewId

            if (it is SideSideViewIdMargin) {
                val margin = it.margin
                when (sides) {
                    HORIZONTAL -> connectHorizontal(this.id, endId, margin)
                    VERTICAL -> connectVertical(this.id, endId, margin)
                    ALL -> connectAll(this.id, endId, margin)
                    else -> connect(sides.start of this.id, sides.end of endId, margin)
                }
            } else {
                when (sides) {
                    HORIZONTAL -> connectHorizontal(this.id, endId)
                    VERTICAL -> connectVertical(this.id, endId)
                    ALL -> connectAll(this.id, endId)
                    else -> connect(sides.start of this.id, sides.end of endId)
                }
            }
        }
    }


    //<editor-fold desc="<< connect() overloads >>">
    private fun View.generateId() {
        if (generateIds && id == 0) {
            id = newId()
        }
    }

    /**
     * Generates a unique ID for a view.
     * Warning: Don't rely on this function for views which should persist their state. There is
     * no guarantee that the same view will get the same generated ID after a configuration change.
     */
    private fun generateIds(vararg views: View) {
        if (generateIds) {
            views.forEach {
                it.generateId()
            }
        }
    }

    fun connect(start: View, startSide: Side, end: View, endSide: Side) {
        generateIds(start, end)
        connect(start.id, startSide, end.id, endSide)
    }
    fun connect(start: View, startSide: Side, end: View, endSide: Side, margin: Int) {
        generateIds(start, end)
        connect(start.id, startSide, end.id, endSide, margin)
    }

    fun connect(start: View, startSide: Side, endId: Int, endSide: Side) {
        generateIds(start)
        connect(start.id, startSide, endId, endSide)
    }
    fun connect(start: View, startSide: Side, endId: Int, endSide: Side, margin: Int) {
        generateIds(start)
        connect(start.id, startSide, endId, endSide, margin)
    }

    fun connect(startId: Int, startSide: Side, end: View, endSide: Side) {
        generateIds(end)
        connect(startId, startSide, end.id, endSide)
    }
    fun connect(startId: Int, startSide: Side, end: View, endSide: Side, margin: Int) {
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
        // ids are generated in the delegated function
        connect(con.from.view, con.from.side, con.to.view, con.to.side)
    }
    fun connect(con: SideViewSideView, margin: Int) {
        // ids are generated in the delegated function
        connect(con.from.view, con.from.side, con.to.view, con.to.side, margin)
    }
    //</editor-fold>

    //<editor-fold desc="<< connect***() overloads>>">
    fun connectBaseline(viewId: ViewId, targetId: ViewId) {
        connect(viewId, BASELINE, targetId, BASELINE)
    }
    private fun connectBaseline(viewId: ViewId, targetId: ViewId, margin: Int) {
        Log.w(TAG, "Baseline connection cannot define margin. Check your definition.")
        connectBaseline(viewId, targetId)
    }
    fun connectHorizontal(viewId: ViewId, targetId: ViewId) {
        connect(viewId, START, targetId, START)
        connect(viewId, LEFT, targetId, LEFT)
        connect(viewId, END, targetId, END)
        connect(viewId, RIGHT, targetId, RIGHT)
    }
    fun connectHorizontal(viewId: ViewId, targetId: ViewId, margin: Int) {
        connect(viewId, START, targetId, START, margin)
        connect(viewId, LEFT, targetId, LEFT, margin)
        connect(viewId, END, targetId, END, margin)
        connect(viewId, RIGHT, targetId, RIGHT, margin)
    }
    fun connectVertical(viewId: ViewId, targetId: ViewId) {
        connect(viewId, TOP, targetId, TOP)
        connect(viewId, BOTTOM, targetId, BOTTOM)
    }
    fun connectVertical(viewId: ViewId, targetId: ViewId, margin: Int) {
        connect(viewId, TOP, targetId, TOP, margin)
        connect(viewId, BOTTOM, targetId, BOTTOM, margin)
    }
    fun connectAll(viewId: ViewId, targetId: ViewId) {
        connectVertical(viewId, targetId)
        connectHorizontal(viewId, targetId)
    }
    fun connectAll(viewId: ViewId, targetId: ViewId, margin: Int) {
        connectVertical(viewId, targetId, margin)
        connectHorizontal(viewId, targetId, margin)
    }
    //</editor-fold>

    open inner class SideView(side: Side, val view: View) : SideViewId(side, view.id) {
        init { view.generateId() }
    }
    open inner class SideViewId(val side: Side, val viewId: ViewId)
    open inner class SideViewSide(val sideView: SideView, side: Side) : SideViewIdSide(sideView, side)
    open inner class SideViewIdSide(val sideViewId: SideViewId, val side: Side)
    open inner class SideViewSideView(from: SideView, val to: SideView) : SideViewSideViewId(from, to)
    open inner class SideViewSideViewId(val from: SideView, val toId: SideViewId)
    open inner class SideViewIdSideView(val from: SideViewId, val to: SideView) : SideViewIdSideViewId(from, to)
    open inner class SideViewIdSideViewId(val fromId: SideViewId, val toId: SideViewId)

    infix fun Side.of(view: View) = SideView(this, view)
    infix fun Side.of(viewId: ViewId) = SideViewId(this, viewId)
    infix fun SideView.to(side: Side) = SideViewSide(this, side)
    infix fun SideViewId.to(side: Side) = SideViewIdSide(this, side)
    infix fun SideViewSide.of(view: View) = SideViewSideView(sideView, SideView(side, view))
    infix fun SideViewSide.of(viewId: ViewId) = SideViewSideViewId(sideView, SideViewId(side, viewId))
    infix fun SideViewIdSide.of(view: View) = SideViewIdSideView(sideViewId, SideView(side, view))
    infix fun SideViewIdSide.of(viewId: ViewId) = SideViewIdSideViewId(sideViewId, SideViewId(side, viewId))


    // SideSide* classes are used in view.connect(vararg) methods
    open inner class SideSide(val start: Side, val end: Side)
    open inner class SideSideView(sides: SideSide, val view: View) : SideSideViewId(sides, view.id) {
        init { view.generateId() }
    }
    open inner class SideSideViewId(val sides: SideSide, val viewId: ViewId)
    open inner class SideSideViewIdMargin(sides: SideSide, viewId: ViewId, val margin: Int): SideSideViewId(sides, viewId)

    infix fun Side.to(side: Side) = SideSide(this, side)
    infix fun SideSide.of(view: View) = SideSideView(this, view)
    infix fun SideSide.of(viewId: ViewId) = SideSideViewId(this, viewId)
    infix fun SideSideView.with(margin: Int) = SideSideViewIdMargin(sides, viewId, margin)
    infix fun SideSideViewId.with(margin: Int) = SideSideViewIdMargin(sides, viewId, margin)

}


