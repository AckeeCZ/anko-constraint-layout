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

//enum class Side(val value: Int) {
//    LEFT(ConstraintLayout.LayoutParams.LEFT),
//    RIGHT(ConstraintLayout.LayoutParams.RIGHT),
//    TOP(ConstraintLayout.LayoutParams.TOP),
//    BOTTOM(ConstraintLayout.LayoutParams.BOTTOM),
//    BASELINE(ConstraintLayout.LayoutParams.BASELINE),
//    START(ConstraintLayout.LayoutParams.START),
//    END(ConstraintLayout.LayoutParams.END)
//}
//
//enum class SideSide(val start: Side, val end: Side) {
//    LEFTS(Side.LEFT, Side.LEFT),
//    RIGHTS(Side.RIGHT, Side.RIGHT),
//    TOPS(Side.TOP, Side.TOP),
//    BOTTOMS(Side.BOTTOM, Side.BOTTOM),
//    BASELINES(Side.BASELINE, Side.BASELINE),
//    STARTS(Side.START, Side.START),
//    ENDS(Side.END, Side.END),
//    HORIZONTAL(Side(-1), Side(-1)),
//    VERTICAL(Side(-2), Side(-2)),
//    ALL(Side(-3), Side(-3)),
//}

/**
 * @author David Khol [david.khol@ackee.cz]
 * @since 16.8.2017
 **/
@Suppress("MemberVisibilityCanPrivate", "unused", "NOTHING_TO_INLINE")
open class _ConstraintSet : ConstraintSet() {

    private val TAG: String = javaClass.simpleName

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

    //<editor-fold desc="<< Chains definitions >>">
    // TODO: 3. 9. 2017 david.khol: add more sophisticated chain DSL
    // TODO: 3. 9. 2017 david.khol: add dynamic changing of chain types
    // TODO: 4. 9. 2017 david.khol: check if provided ids are invalid

    open inner class Chain {
        open var viewIds: IntArray = intArrayOf()
        open var weights: FloatArray? = null

        open fun views(vararg views: View) {
            this.viewIds = views.map { it.id }.toIntArray()
        }
        open fun viewIds(vararg viewIds: ViewId) {
            this.viewIds = viewIds
        }
        open fun weights(vararg weights: Float) {
            this.weights = weights
        }
    }

    /**
     * Sample usage:
     * ```
     * constraints {
     *     ...
     *     chain(TOP of viewOne, BOTTOM of viewTwo, CHAIN_SPREAD) {
     *         // You should either use views() or viewIds() functions to define elements of the chain
     *         views(viewThree, viewFour, viewFive)
     *
     *         // Optionally you can also define weights to mimic functionality of LinearLayout and it's weights
     *         weights(2f, 1f, 1f)
     *     }
     * }
     * ```
     */
    open fun chain(begin: SideViewId, end: SideViewId, chainType: ChainType = CHAIN_SPREAD, init: Chain.() -> Unit) {
        val horizontal = listOf(LEFT, RIGHT)
        val vertical = listOf(TOP, BOTTOM)
        val horizontalRtl = listOf(START, END)

        val chain = Chain()
        chain.init()

        if (chain.weights != null && chainType == CHAIN_PACKED) {
            throw IllegalArgumentException("You may not use weights together with chainType CHAIN_PACKED")
        }
        // TODO: 9. 9. 2017 david.khol: throw an exception when no view in the chain defines match_constraint

        if (begin.side in horizontal && end.side in horizontal) {
            createHorizontalChain(begin.viewId, begin.side, end.viewId, end.side, chain.viewIds, chain.weights, chainType)
        } else if (begin.side in vertical && end.side in vertical) {
            createVerticalChain(begin.viewId, begin.side, end.viewId, end.side, chain.viewIds, chain.weights, chainType)
        } else if (begin.side in horizontalRtl && end.side in horizontalRtl) {
            createHorizontalChainRtl(begin.viewId, begin.side, end.viewId, end.side, chain.viewIds, chain.weights, chainType)
        } else {
            throw IllegalArgumentException("Cannot create a chain for supplied sides: ${begin.side} together with ${end.side}")
        }
    }
    //</editor-fold>


    fun View.clear() {
        super.clear(this.id)
    }

    fun View.clear(vararg sides: Side) {
        sides.forEach {
            super.clear(this.id, it)
        }
    }

    fun View.connect(vararg connections: SideSideViewId) {
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

    /**
     * Sample usage:
     * ```
     * view.center(LEFT of parentId with 16.dp, RIGHT of parentId with 16.dp, 0.2f)
     * ```
     */
    fun View.center(first: SideViewId, second: SideViewId, bias: Float = 0.5F) {
        val horizontal = listOf(LEFT, RIGHT)
        val vertical = listOf(TOP, BOTTOM)
        val horizontalRtl = listOf(START, END)

        val firstViewId = first.viewId
        val secondViewId = second.viewId

        val firstSide = first.side
        val secondSide = second.side

        val firstMargin = (first as? SideViewIdMargin)?.margin ?: 0
        val secondMargin = (second as? SideViewIdMargin)?.margin ?: 0

        if (firstSide in horizontal && secondSide in horizontal) {
            centerHorizontally(this.id,
                    firstViewId, firstSide, firstMargin,
                    secondViewId, secondSide, secondMargin,
                    bias)
        } else if (firstSide in vertical && secondSide in vertical) {
            centerVertically(this.id,
                    firstViewId, firstSide, firstMargin,
                    secondViewId, secondSide, secondMargin,
                    bias)
        } else if (firstSide in horizontalRtl && secondSide in horizontalRtl) {
            centerHorizontallyRtl(this.id,
                    firstViewId, firstSide, firstMargin,
                    secondViewId, secondSide, secondMargin,
                    bias)
        } else {
            throw IllegalArgumentException("Cannot center a view for supplied sides: $firstSide together with $secondSide")
        }
    }

    inline fun View.width(width: Int) = constrainWidth(this.id, width)
    inline fun View.maxWidth(width: Int) = constrainMaxWidth(this.id, width)
    inline fun View.minWidth(width: Int) = constrainMinWidth(this.id, width)
    inline fun View.defaultWidth(width: Int) = constrainDefaultWidth(this.id, width)

    inline fun View.height(height: Int) = constrainHeight(this.id, height)
    inline fun View.maxHeight(height: Int) = constrainMaxHeight(this.id, height)
    inline fun View.minHeight(height: Int) = constrainMinHeight(this.id, height)
    inline fun View.defaultHeight(height: Int) = constrainDefaultHeight(this.id, height)

    inline fun View.horizontalBias(bias: Float) = setHorizontalBias(this.id, bias)
    inline fun View.verticalBias(bias: Float) = setVerticalBias(this.id, bias)

    inline fun View.margin(anchor: Int, value: Int) = setMargin(this.id, anchor, value)
    inline fun View.goneMargin(anchor: Int, value: Int) = setGoneMargin(this.id, anchor, value)

    inline fun View.aspectRatio(ratio: String) = dimensionRatio(ratio)
    inline fun View.dimensionRatio(ratio: String) = setDimensionRatio(this.id, ratio)

    inline fun View.visibility(visibility: Int) = setVisibility(this.id, visibility)

    inline fun View.alpha(alpha: Float) = setAlpha(this.id, alpha)


//    fun View.rotate(rotation: Float) {
//        setRotation(this.id, rotation)
//        setRotationX(this.id, rotation)
//        setRotationY(this.id, rotation)
//    }
//
//    fun View.scale(scale: Float) {
//        setScaleX(this.id, scale)
//        setScaleY(this.id, scale)
//    }
//
//    fun View.transformPivot(x: Float, y: Float) {
//        setTransformPivot(this.id, x, y)
//        setTransformPivotX(this.id, x)
//        setTransformPivotY(this.id, y)
//    }

    //<editor-fold desc="<< connect() overloads >>">

    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("start.connect(startSide to endSide of end)"))
    fun connect(start: View, startSide: Side, end: View, endSide: Side) = connect(start.id, startSide, end.id, endSide)
    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("start.connect(startSide to endSide of end with margin)"))
    fun connect(start: View, startSide: Side, end: View, endSide: Side, margin: Int) = connect(start.id, startSide, end.id, endSide, margin)

    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("start.connect(startSide to endSide of endId)"))
    fun connect(start: View, startSide: Side, endId: Int, endSide: Side) = connect(start.id, startSide, endId, endSide)
    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("start.connect(startSide to endSide of endId with margin)"))
    fun connect(start: View, startSide: Side, endId: Int, endSide: Side, margin: Int) = connect(start.id, startSide, endId, endSide, margin)

    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("start.connect(startSide to endSide of end)"))
    fun connect(startId: Int, startSide: Side, end: View, endSide: Side) = connect(startId, startSide, end.id, endSide)
    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("start.connect(startSide to endSide of end with margin)"))
    fun connect(startId: Int, startSide: Side, end: View, endSide: Side, margin: Int) = connect(startId, startSide, end.id, endSide, margin)

    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("start.connect(startSide to endSide of end)"))
    fun connect(start: SideViewId, end: SideViewId) = connect(start.viewId, start.side, end.viewId, end.side)
    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("start.connect(startSide to endSide of end with margin)"))
    fun connect(start: SideViewId, end: SideViewId, margin: Int) = connect(start.viewId, start.side, end.viewId, end.side, margin)

    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("start.connect(startSide to endSide of end)"))
    fun connect(con: SideViewSideView) = connect(con.from.view, con.from.side, con.to.view, con.to.side)
    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("start.connect(startSide to endSide of end with margin)"))
    fun connect(con: SideViewSideView, margin: Int) = connect(con.from.view, con.from.side, con.to.view, con.to.side, margin)

    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("viewId.connect(BASELINES of targetId)"))
    fun connectBaseline(viewId: ViewId, targetId: ViewId) = connect(viewId, BASELINE, targetId, BASELINE)
    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("viewId.connect(BASELINES of targetId with margin)"))
    private fun connectBaseline(viewId: ViewId, targetId: ViewId, margin: Int) {
        Log.w(TAG, "Baseline connection cannot define margin. Check your definition.")
        connectBaseline(viewId, targetId)
    }

    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("viewId.connect(HORIZONTAL of targetId)"))
    fun connectHorizontal(viewId: ViewId, targetId: ViewId) {
        connect(viewId, START, targetId, START)
        connect(viewId, LEFT, targetId, LEFT)
        connect(viewId, END, targetId, END)
        connect(viewId, RIGHT, targetId, RIGHT)
    }
    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("viewId.connect(HORIZONTAL of targetId with margin)"))
    fun connectHorizontal(viewId: ViewId, targetId: ViewId, margin: Int) {
        connect(viewId, START, targetId, START, margin)
        connect(viewId, LEFT, targetId, LEFT, margin)
        connect(viewId, END, targetId, END, margin)
        connect(viewId, RIGHT, targetId, RIGHT, margin)
    }

    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("viewId.connect(VERTICAL of targetId)"))
    fun connectVertical(viewId: ViewId, targetId: ViewId) {
        connect(viewId, TOP, targetId, TOP)
        connect(viewId, BOTTOM, targetId, BOTTOM)
    }
    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("viewId.connect(VERICAL of targetId with margin)"))
    fun connectVertical(viewId: ViewId, targetId: ViewId, margin: Int) {
        connect(viewId, TOP, targetId, TOP, margin)
        connect(viewId, BOTTOM, targetId, BOTTOM, margin)
    }

    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("viewId.connect(ALL of targetId)"))
    fun connectAll(viewId: ViewId, targetId: ViewId) {
        connectVertical(viewId, targetId)
        connectHorizontal(viewId, targetId)
    }
    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("viewId.connect(ALL of targetId with margin)"))
    fun connectAll(viewId: ViewId, targetId: ViewId, margin: Int) {
        connectVertical(viewId, targetId, margin)
        connectHorizontal(viewId, targetId, margin)
    }
    //</editor-fold>

    open inner class SideView(side: Side, val view: View) : SideViewId(side, view.id)
    open inner class SideViewId(val side: Side, val viewId: ViewId)
    open inner class SideViewSide(val sideView: SideView, side: Side) : SideViewIdSide(sideView, side)
    open inner class SideViewIdSide(val sideViewId: SideViewId, val side: Side)
    open inner class SideViewSideView(from: SideView, val to: SideView) : SideViewSideViewId(from, to)
    open inner class SideViewSideViewId(val from: SideView, val toId: SideViewId)
    open inner class SideViewIdSideView(val from: SideViewId, val to: SideView) : SideViewIdSideViewId(from, to)
    open inner class SideViewIdSideViewId(val fromId: SideViewId, val toId: SideViewId)
    open inner class SideViewMargin(sideView: SideView, margin: Int) : SideViewIdMargin(sideView, margin)
    open inner class SideViewIdMargin(sideViewId: SideViewId, val margin: Int) : SideViewId(sideViewId.side, sideViewId.viewId)

    infix inline fun Side.of(view: View) = SideView(this, view)
    infix inline fun Side.of(viewId: ViewId) = SideViewId(this, viewId)
    infix inline fun SideView.to(side: Side) = SideViewSide(this, side)
    infix inline fun SideViewId.to(side: Side) = SideViewIdSide(this, side)
    infix inline fun SideViewSide.of(view: View) = SideViewSideView(sideView, SideView(side, view))
    infix inline fun SideViewSide.of(viewId: ViewId) = SideViewSideViewId(sideView, SideViewId(side, viewId))
    infix inline fun SideViewIdSide.of(view: View) = SideViewIdSideView(sideViewId, SideView(side, view))
    infix inline fun SideViewIdSide.of(viewId: ViewId) = SideViewIdSideViewId(sideViewId, SideViewId(side, viewId))
    infix inline fun SideView.with(margin: Int) = SideViewMargin(this, margin)
    infix inline fun SideViewId.with(margin: Int) = SideViewIdMargin(this, margin)

    open inner class SideSide(val start: Side, val end: Side)
    open inner class SideSideView(sides: SideSide, val view: View) : SideSideViewId(sides, view.id)
    open inner class SideSideViewId(val sides: SideSide, val viewId: ViewId)
    open inner class SideSideViewMargin(sides: SideSide, view: View, margin: Int): SideSideViewIdMargin(sides, view.id, margin)
    open inner class SideSideViewIdMargin(sides: SideSide, viewId: ViewId, val margin: Int): SideSideViewId(sides, viewId)

    infix inline fun Side.to(side: Side) = SideSide(this, side)
    infix inline fun SideSide.of(view: View) = SideSideView(this, view)
    infix inline fun SideSide.of(viewId: ViewId) = SideSideViewId(this, viewId)
    infix inline fun SideSideView.with(margin: Int) = SideSideViewMargin(sides, view, margin)
    infix inline fun SideSideViewId.with(margin: Int) = SideSideViewIdMargin(sides, viewId, margin)

}


