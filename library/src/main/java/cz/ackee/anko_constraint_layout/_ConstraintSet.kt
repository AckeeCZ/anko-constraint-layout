package cz.ackee.anko_constraint_layout

import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import cz.ackee.anko_constraint_layout.ViewIdGenerator.newId


typealias Side = Int
typealias ViewId = Int
typealias ChainStyle = Int
typealias DefaultSize = Int

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
@Suppress("MemberVisibilityCanPrivate", "unused", "NOTHING_TO_INLINE", "PropertyName")
open class _ConstraintSet : ConstraintSet() {

    private val TAG: String = javaClass.simpleName

    val parentId: ViewId = ConstraintLayout.LayoutParams.PARENT_ID

    val MATCH_CONSTRAINT_WRAP: DefaultSize = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_WRAP
    val MATCH_CONSTRAINT_SPREAD: DefaultSize = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_SPREAD
    val MATCH_CONSTRAINT_PERCENT: DefaultSize = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_PERCENT

    val LEFT: Side = ConstraintLayout.LayoutParams.LEFT
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

    val CHAIN_SPREAD: ChainStyle = ConstraintLayout.LayoutParams.CHAIN_SPREAD
    val CHAIN_SPREAD_INSIDE: ChainStyle = ConstraintLayout.LayoutParams.CHAIN_SPREAD_INSIDE
    val CHAIN_PACKED: ChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED

    //<editor-fold desc="<< Guidelines definitions >>">
    open fun guideline(orientation: Int, init: (Int) -> Unit): Int {
        val guideId = newId()
        create(guideId, orientation)
        init(guideId)
        return guideId
    }

    open fun guidelineBegin(orientation: Int, guide: Int) = guideline(orientation) { setGuidelineBegin(it, guide) }
    open fun guidelineEnd(orientation: Int, guide: Int) = guideline(orientation) { setGuidelineEnd(it, guide) }
    open fun guidelinePercent(orientation: Int, guide: Float) = guideline(orientation) { setGuidelinePercent(it, guide) }

    open fun verticalGuidelineBegin(guide: Int) = guidelineBegin(LinearLayout.VERTICAL, guide)
    open fun verticalGuidelineEnd(guide: Int) = guidelineEnd(LinearLayout.VERTICAL, guide)
    open fun verticalGuidelinePercent(guide: Float) = guidelinePercent(LinearLayout.VERTICAL, guide)
    open fun horizontalGuidelineBegin(guide: Int) = guidelineBegin(LinearLayout.HORIZONTAL, guide)
    open fun horizontalGuidelineEnd(guide: Int) = guidelineEnd(LinearLayout.HORIZONTAL, guide)
    open fun horizontalGuidelinePercent(guide: Float) = guidelinePercent(LinearLayout.HORIZONTAL, guide)
    //</editor-fold>

    //<editor-fold desc="<< Barrier definitions >>">
    open fun barrier(direction: Int, vararg views: View): Int {
        val barrierId = newId()
        createBarrier(barrierId, direction, *views.map { it.id }.toIntArray())
        return barrierId
    }

    open fun barrierLeft(vararg views: View) = barrier(LEFT, *views)
    open fun barrierRight(vararg views: View) = barrier(RIGHT, *views)
    open fun barrierTop(vararg views: View) = barrier(TOP, *views)
    open fun barrierBottom(vararg views: View) = barrier(BOTTOM, *views)
    open fun barrierStart(vararg views: View) = barrier(START, *views)
    open fun barrierEnd(vararg views: View) = barrier(END, *views)
    //</editor-fold>

    //<editor-fold desc="<< Chains definitions >>">

    /**
     * Creates a chain of views.
     * ```
     * constraints {
     *     ...
     *     val views = arrayOf(viewThree, viewFour, viewFive)
     *     views.chainSpread(TOP of viewOne, BOTTOM of viewTwo, arrayOf(2f, 1f, 1f))
     * }
     * ```
     */
    fun chain(viewIds: IntArray, begin: SideViewId, end: SideViewId, chainStyle: ChainStyle, weights: FloatArray? = null) {
        val horizontal = listOf(LEFT, RIGHT)
        val vertical = listOf(TOP, BOTTOM)
        val horizontalRtl = listOf(START, END)

        if (weights != null && weights!!.size != viewIds.size) {
            throw IllegalArgumentException("If you define weights, it must contain the same amount of weights as views")
        }
        if (weights != null && chainStyle == CHAIN_PACKED) {
            throw IllegalArgumentException("You may not use weights together with chainStyle CHAIN_PACKED")
        }
        // TODO: 9. 9. 2017 david.khol: throw an exception when no view in the chain defines match_constraint

        if (begin.side in horizontal && end.side in horizontal) {
            createHorizontalChain(begin.viewId, begin.side, end.viewId, end.side, viewIds, weights, chainStyle)
        } else if (begin.side in vertical && end.side in vertical) {
            createVerticalChain(begin.viewId, begin.side, end.viewId, end.side, viewIds, weights, chainStyle)
        } else if (begin.side in horizontalRtl && end.side in horizontalRtl) {
            createHorizontalChainRtl(begin.viewId, begin.side, end.viewId, end.side, viewIds, weights, chainStyle)
        } else {
            throw IllegalArgumentException("Cannot create a chain for supplied sides: ${begin.side} together with ${end.side}")
        }
    }

    fun chainSpread(viewIds: IntArray, begin: SideViewId, end: SideViewId, weights: FloatArray? = null) {
        chain(viewIds, begin, end, CHAIN_SPREAD, weights)
    }

    fun chainSpreadInside(viewIds: IntArray, begin: SideViewId, end: SideViewId, weights: FloatArray? = null) {
        chain(viewIds, begin, end, CHAIN_SPREAD_INSIDE, weights)
    }

    fun chainPacked(viewIds: IntArray, begin: SideViewId, end: SideViewId, weights: FloatArray? = null) {
        chain(viewIds, begin, end, CHAIN_PACKED, weights)
    }


    inline fun IntArray.chain(begin: SideViewId, end: SideViewId, chainStyle: ChainStyle, weights: FloatArray? = null): IntArray {
        chain(this, begin, end, chainStyle, weights)
        return this
    }

    inline fun IntArray.chainSpread(begin: SideViewId, end: SideViewId, weights: FloatArray? = null): IntArray {
        chain(begin, end, CHAIN_SPREAD, weights)
        return this
    }

    inline fun IntArray.chainSpreadInside(begin: SideViewId, end: SideViewId, weights: FloatArray? = null): IntArray {
        chain(begin, end, CHAIN_SPREAD_INSIDE, weights)
        return this
    }

    inline fun IntArray.chainPacked(begin: SideViewId, end: SideViewId, weights: FloatArray? = null): IntArray {
        chain(begin, end, CHAIN_PACKED, weights)
        return this
    }

    inline fun <T : View> Array<T>.chain(begin: SideViewId, end: SideViewId, chainStyle: ChainStyle, weights: FloatArray? = null): Array<T> {
        chain(this.map { it.id }.toIntArray(), begin, end, chainStyle, weights)
        return this
    }

    inline fun <T : View> Array<T>.chainSpread(begin: SideViewId, end: SideViewId, weights: FloatArray? = null): Array<T> {
        chain(begin, end, CHAIN_SPREAD, weights)
        return this
    }

    inline fun <T : View> Array<T>.chainSpreadInside(begin: SideViewId, end: SideViewId, weights: FloatArray? = null): Array<T> {
        chain(begin, end, CHAIN_SPREAD_INSIDE, weights)
        return this
    }

    inline fun <T : View> Array<T>.chainPacked(begin: SideViewId, end: SideViewId, weights: FloatArray? = null): Array<T> {
        chain(begin, end, CHAIN_PACKED, weights)
        return this
    }


    inline fun <T : View> T.setHorizontalChainStyle(chainStyle: ChainStyle): T {
        setHorizontalChainStyle(this.id, chainStyle)
        return this
    }

    inline fun <T : View> T.setVerticalChainStyle(chainStyle: ChainStyle): T {
        setVerticalChainStyle(this.id, chainStyle)
        return this
    }

    inline fun <T : View> T.addToHorizontalChain(leftId: Int, rightId: Int): T {
        addToHorizontalChain(this.id, leftId, rightId)
        return this
    }

    inline fun <T : View> T.addToHorizontalChainRTL(leftId: Int, rightId: Int): T {
        addToHorizontalChainRTL(this.id, leftId, rightId)
        return this
    }

    inline fun <T : View> T.addToVerticalChain(topId: Int, bottomId: Int): T {
        addToVerticalChain(this.id, topId, bottomId)
        return this
    }

    inline fun <T : View> T.addToHorizontalChain(leftView: View, rightView: View): T {
        addToHorizontalChain(this.id, leftView.id, rightView.id)
        return this
    }

    inline fun <T : View> T.addToHorizontalChainRTL(leftView: View, rightView: View): T {
        addToHorizontalChainRTL(this.id, leftView.id, rightView.id)
        return this
    }

    inline fun <T : View> T.addToVerticalChain(topView: View, bottomView: View): T {
        addToVerticalChain(this.id, topView.id, bottomView.id)
        return this
    }

    inline fun <T : View> T.removeFromVerticalChain(): T {
        removeFromVerticalChain(this.id)
        return this
    }

    inline fun <T : View> T.removeFromHorizontalChain(): T {
        removeFromHorizontalChain(this.id)
        return this
    }

    inline fun <T : View> T.setHorizontalWeight(weight: Float): T {
        setHorizontalWeight(this.id, weight)
        return this
    }

    inline fun <T : View> T.setVerticalWeight(weight: Float): T {
        setVerticalWeight(this.id, weight)
        return this
    }


    //</editor-fold>

    //<editor-fold desc="<< Various extensions >>">
    inline fun <T : View> T.clear(): T {
        super.clear(this.id)
        return this
    }

    inline fun <T : View> T.clear(vararg sides: Side): T {
        sides.forEach {
            super.clear(this.id, it)
        }
        return this
    }

    fun <T : View> T.center(first: SideViewId, second: SideViewId, bias: Float = 0.5F): T {
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
        return this
    }

    inline fun <T : View> T.width(width: Int): T {
        constrainWidth(this.id, width)
        return this
    }

    inline fun <T : View> T.maxWidth(width: Int): T {
        constrainMaxWidth(this.id, width)
        return this
    }

    inline fun <T : View> T.minWidth(width: Int): T {
        constrainMinWidth(this.id, width)
        return this
    }

    inline fun <T : View> T.defaultWidth(width: DefaultSize): T {
        constrainDefaultWidth(this.id, width)
        return this
    }

    inline fun <T : View> T.height(height: Int): T {
        constrainHeight(this.id, height)
        return this
    }

    inline fun <T : View> T.maxHeight(height: Int): T {
        constrainMaxHeight(this.id, height)
        return this
    }

    inline fun <T : View> T.minHeight(height: Int): T {
        constrainMinHeight(this.id, height)
        return this
    }

    inline fun <T : View> T.defaultHeight(height: DefaultSize): T {
        constrainDefaultHeight(this.id, height)
        return this
    }

    inline fun <T : View> T.size(width: Int, height: Int): T {
        width(width)
        height(height)
        return this
    }

    inline fun <T : View> T.maxSize(width: Int, height: Int): T {
        maxWidth(width)
        maxHeight(height)
        return this
    }

    inline fun <T : View> T.minSize(width: Int, height: Int): T {
        minWidth(width)
        minHeight(height)
        return this
    }

    inline fun <T : View> T.defaultSize(width: DefaultSize, height: DefaultSize): T {
        defaultWidth(width)
        defaultHeight(height)
        return this
    }

    inline fun <T : View> T.margin(anchor: Int, value: Int): T {
        setMargin(this.id, anchor, value)
        return this
    }

    inline fun <T : View> T.goneMargin(anchor: Int, value: Int): T {
        setGoneMargin(this.id, anchor, value)
        return this
    }

    inline fun <T : View> T.horizontalBias(bias: Float): T {
        setHorizontalBias(this.id, bias)
        return this
    }

    inline fun <T : View> T.verticalBias(bias: Float): T {
        setVerticalBias(this.id, bias)
        return this
    }

    inline fun <T : View> T.dimensionRatio(ratio: String): T {
        setDimensionRatio(this.id, ratio)
        return this
    }

    inline fun <T : View> T.visibility(visibility: Int): T {
        setVisibility(this.id, visibility)
        return this
    }

    inline fun <T : View> T.alpha(alpha: Float): T {
        setAlpha(this.id, alpha)
        return this
    }

    inline var <T : View> T.applyElevation: Boolean
        get() = getApplyElevation(this.id)
        set(value) = setApplyElevation(this.id, value)

    inline fun <T : View> T.applyElevation(): Boolean {
        return getApplyElevation(this.id)
    }

    inline fun <T : View> T.applyElevation(apply: Boolean): T {
        setApplyElevation(this.id, apply)
        return this
    }

    inline fun <T : View> T.elevation(elevation: Float): T {
        setElevation(this.id, elevation)
        return this
    }

    inline fun <T : View> T.rotationX(rotation: Float): T {
        setRotationX(this.id, rotation)
        return this
    }

    inline fun <T : View> T.rotationY(rotation: Float): T {
        setRotationY(this.id, rotation)
        return this
    }

    inline fun <T : View> T.rotation(rotation: Float): T {
        setRotation(this.id, rotation)
        return this
    }

    inline fun <T : View> T.scaleX(scale: Float): T {
        setScaleX(this.id, scale)
        return this
    }

    inline fun <T : View> T.scaleY(scale: Float): T {
        setScaleY(this.id, scale)
        return this
    }

    inline fun <T : View> T.scale(scale: Float): T {
        setScaleX(this.id, scale)
        setScaleY(this.id, scale)
        return this
    }

    inline fun <T : View> T.transformPivotX(x: Float): T {
        setTransformPivotX(this.id, x)
        return this
    }

    inline fun <T : View> T.transformPivotY(y: Float): T {
        setTransformPivotY(this.id, y)
        return this
    }

    inline fun <T : View> T.transformPivot(x: Float, y: Float): T {
        setTransformPivot(this.id, x, y)
        return this
    }

    inline fun <T : View> T.translationX(translationX: Float): T {
        setTranslationX(this.id, translationX)
        return this
    }

    inline fun <T : View> T.translationY(translationY: Float): T {
        setTranslationY(this.id, translationY)
        return this
    }

    inline fun <T : View> T.translationZ(translationZ: Float): T {
        setTranslationZ(this.id, translationZ)
        return this
    }

    inline fun <T : View> T.translation(translationX: Float, translationY: Float): T {
        setTranslation(this.id, translationX, translationY)
        return this
    }
    //</editor-fold>

    //<editor-fold desc="<< connect() overloads >>">
    fun <T : View> T.connect(vararg connections: SideSideViewId): T {
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
        return this
    }

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

    @Deprecated("Use View.connect() instead", replaceWith = ReplaceWith("viewId.connect(VERTICAL of targetId with margin)"))
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

    //<editor-fold desc="<< Helper classes and infix operators >>">
    open inner class SideSide(val start: Side, val end: Side)

    open inner class SideSideView(sides: SideSide, val view: View) : SideSideViewId(sides, view.id)
    open inner class SideSideViewId(val sides: SideSide, val viewId: ViewId)
    open inner class SideSideViewMargin(sides: SideSide, view: View, margin: Int) : SideSideViewIdMargin(sides, view.id, margin)
    open inner class SideSideViewIdMargin(sides: SideSide, viewId: ViewId, val margin: Int) : SideSideViewId(sides, viewId)
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

    infix inline fun Side.to(side: Side) = SideSide(this, side)
    infix inline fun SideSide.of(view: View) = SideSideView(this, view)
    infix inline fun SideSide.of(viewId: ViewId) = SideSideViewId(this, viewId)
    infix inline fun SideSideView.with(margin: Int) = SideSideViewMargin(sides, view, margin)
    infix inline fun SideSideViewId.with(margin: Int) = SideSideViewIdMargin(sides, viewId, margin)
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
    //</editor-fold>

}


