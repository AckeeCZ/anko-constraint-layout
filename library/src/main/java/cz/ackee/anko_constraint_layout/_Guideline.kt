@file:Suppress("unused", "NOTHING_TO_INLINE")

package cz.ackee.anko_constraint_layout

import android.support.constraint.Guideline
import android.widget.LinearLayout
import org.jetbrains.anko.custom.ankoView

/**
 * A set of helper functions to generate a Guideline view.
 *
 * If you don't specify id for the guideline, new unique id will be generated automatically.
 *
 * There are no more settings for Guideline except its orientation and guide size (margin /
 * percentage). For that reason init lambda is not provided as serves no purpose.
 *
 * These extension functions generate a [Guideline] as a [View]. Use these functions inside of
 * constraintLayout {} init block, not inside of constraints {} block. For definition of guidelines
 * within constraints {} block, use [_ConstraintSet.guideline] functions instead.
 *
 * @see [_ConstraintSet.guideline]
 * @author David Khol [david.khol@ackee.cz]
 * @since 18.8.2017
 */

fun _ConstraintLayout.verticalGuidelineBegin(guide: Int) = verticalGuidelineBegin(ViewIdGenerator.newId(), guide)
fun _ConstraintLayout.verticalGuidelineEnd(guide: Int) = verticalGuidelineEnd(ViewIdGenerator.newId(), guide)
fun _ConstraintLayout.verticalGuidelinePercent(guide: Float) = verticalGuidelinePercent(ViewIdGenerator.newId(), guide)
fun _ConstraintLayout.horizontalGuidelineBegin(guide: Int) = horizontalGuidelineBegin(ViewIdGenerator.newId(), guide)
fun _ConstraintLayout.horizontalGuidelineEnd(guide: Int) = horizontalGuidelineEnd(ViewIdGenerator.newId(), guide)
fun _ConstraintLayout.horizontalGuidelinePercent(guide: Float) = horizontalGuidelinePercent(ViewIdGenerator.newId(), guide)

fun _ConstraintLayout.verticalGuidelineBegin(id: ViewId, guide: Int) = guidelineBegin(id, LinearLayout.VERTICAL, guide)
fun _ConstraintLayout.verticalGuidelineEnd(id: ViewId, guide: Int) = guidelineEnd(id, LinearLayout.VERTICAL, guide)
fun _ConstraintLayout.verticalGuidelinePercent(id: ViewId, guide: Float) = guidelinePercent(id, LinearLayout.VERTICAL, guide)
fun _ConstraintLayout.horizontalGuidelineBegin(id: ViewId, guide: Int) = guidelineBegin(id, LinearLayout.HORIZONTAL, guide)
fun _ConstraintLayout.horizontalGuidelineEnd(id: ViewId, guide: Int) = guidelineEnd(id, LinearLayout.HORIZONTAL, guide)
fun _ConstraintLayout.horizontalGuidelinePercent(id: ViewId, guide: Float) = guidelinePercent(id, LinearLayout.HORIZONTAL, guide)

private inline fun _ConstraintLayout.guidelineBegin(id: ViewId, orientation: Int, guide: Int): Guideline {
    return guideline(id).lparams {
        this.orientation = orientation
        this.guideBegin = guide
    }
}
private inline fun _ConstraintLayout.guidelineEnd(id: ViewId, orientation: Int, guide: Int): Guideline {
    return guideline(id).lparams {
        this.orientation = orientation
        this.guideEnd = guide
    }
}
private inline fun _ConstraintLayout.guidelinePercent(id: ViewId, orientation: Int, guide: Float): Guideline {
    return guideline(id).lparams {
        this.orientation = orientation
        this.guidePercent = guide
    }
}

private inline fun _ConstraintLayout.guideline(id: ViewId): Guideline {
    return ankoView(::Guideline, theme = 0) {
        this.id = id
    }
}
