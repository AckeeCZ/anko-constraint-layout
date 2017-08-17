@file:Suppress("unused")

package cz.ackee.anko_constraint_layout

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.constraint.Guideline
import android.view.View
import android.widget.LinearLayout
import org.jetbrains.anko.custom.ankoView


/**
 * @author David Khol [david.khol@ackee.cz]
 * @since 9. 8. 2017
 **/
open class _Guideline(ctx: Context) : Guideline(ctx)

private val vertical = LinearLayout.VERTICAL
private val horizontal = LinearLayout.HORIZONTAL

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun _ConstraintLayout.verticalGuidelineBegin(guide: Int) = verticalGuidelineBegin(View.generateViewId(), guide)
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun _ConstraintLayout.verticalGuidelineEnd(guide: Int) = verticalGuidelineEnd(View.generateViewId(), guide)
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun _ConstraintLayout.verticalGuidelinePercent(guide: Float) = verticalGuidelinePercent(View.generateViewId(), guide)
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun _ConstraintLayout.horizontalGuidelineBegin(guide: Int) = horizontalGuidelineBegin(View.generateViewId(), guide)
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun _ConstraintLayout.horizontalGuidelineEnd(guide: Int) = horizontalGuidelineEnd(View.generateViewId(), guide)
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun _ConstraintLayout.horizontalGuidelinePercent(guide: Float) = horizontalGuidelinePercent(View.generateViewId(), guide)

fun _ConstraintLayout.verticalGuidelineBegin(id: Int, guide: Int) = guidelineBegin(id, vertical, guide)
fun _ConstraintLayout.verticalGuidelineEnd(id: Int, guide: Int) = guidelineEnd(id, vertical, guide)
fun _ConstraintLayout.verticalGuidelinePercent(id: Int, guide: Float) = guidelinePercent(id, vertical, guide)
fun _ConstraintLayout.horizontalGuidelineBegin(id: Int, guide: Int) = guidelineBegin(id, horizontal, guide)
fun _ConstraintLayout.horizontalGuidelineEnd(id: Int, guide: Int) = guidelineEnd(id, horizontal, guide)
fun _ConstraintLayout.horizontalGuidelinePercent(id: Int, guide: Float) = guidelinePercent(id, horizontal, guide)

private inline fun _ConstraintLayout.guidelineBegin(id: Int, orientation: Int, guide: Int): _Guideline {
    return guideline(id).lparams {
        this.orientation = orientation
        this.guideBegin = guide
    }
}
private inline fun _ConstraintLayout.guidelineEnd(id: Int, orientation: Int, guide: Int): _Guideline {
    return guideline(id).lparams {
        this.orientation = orientation
        this.guideEnd = guide
    }
}
private inline fun _ConstraintLayout.guidelinePercent(id: Int, orientation: Int, guide: Float): _Guideline {
    return guideline(id).lparams {
        this.orientation = orientation
        this.guidePercent = guide
    }
}

private inline fun _ConstraintLayout.guideline(id: Int): _Guideline {
    return ankoView(::_Guideline, theme = 0) {
        this.id = id
    }
}
