@file:Suppress("NOTHING_TO_INLINE")

package cz.ackee.anko_constraint_layout

import android.support.constraint.Barrier
import android.view.View
import org.jetbrains.anko.custom.ankoView

/**
 * A set of helper functions to generate a [Barrier] view.
 *
 * Barriers are helpful when aligning a view to multiple other views in a way that the view should
 * be aligned to a view which takes up more space.
 *
 * @author David Khol [david.khol@ackee.cz]
 * @since 3.9.2017
 */

typealias BarrierType = Int

val LEFT: BarrierType = Barrier.LEFT
val RIGHT: BarrierType = Barrier.RIGHT
val TOP: BarrierType = Barrier.TOP
val BOTTOM: BarrierType = Barrier.BOTTOM
val START: BarrierType = Barrier.START
val END: BarrierType = Barrier.END

fun _ConstraintLayout.barrierLeft(vararg views: View, init: (Barrier).() -> Unit = {}) = barrier(LEFT, *views, init = init)
fun _ConstraintLayout.barrierRight(vararg views: View, init: (Barrier).() -> Unit = {}) = barrier(RIGHT, *views, init = init)
fun _ConstraintLayout.barrierTop(vararg views: View, init: (Barrier).() -> Unit = {}) = barrier(TOP, *views, init = init)
fun _ConstraintLayout.barrierBottom(vararg views: View, init: (Barrier).() -> Unit = {}) = barrier(BOTTOM, *views, init = init)
fun _ConstraintLayout.barrierStart(vararg views: View, init: (Barrier).() -> Unit = {}) = barrier(START, *views, init = init)
fun _ConstraintLayout.barrierEnd(vararg views: View, init: (Barrier).() -> Unit = {}) = barrier(END, *views, init = init)

fun _ConstraintLayout.barrierLeft(vararg views: ViewId, init: (Barrier).() -> Unit = {}) = barrier(LEFT, *views, init = init)
fun _ConstraintLayout.barrierRight(vararg views: ViewId, init: (Barrier).() -> Unit = {}) = barrier(RIGHT, *views, init = init)
fun _ConstraintLayout.barrierTop(vararg views: ViewId, init: (Barrier).() -> Unit = {}) = barrier(TOP, *views, init = init)
fun _ConstraintLayout.barrierBottom(vararg views: ViewId, init: (Barrier).() -> Unit = {}) = barrier(BOTTOM, *views, init = init)
fun _ConstraintLayout.barrierStart(vararg views: ViewId, init: (Barrier).() -> Unit = {}) = barrier(START, *views, init = init)
fun _ConstraintLayout.barrierEnd(vararg views: ViewId, init: (Barrier).() -> Unit = {}) = barrier(END, *views, init = init)

fun _ConstraintLayout.barrier(side: BarrierType, vararg views: View, init: (Barrier).() -> Unit = {}): Barrier {
    return barrier(side, *views.map { it.id }.toIntArray(), init = init)
}

fun _ConstraintLayout.barrier(side: BarrierType, vararg ids: ViewId, init: (Barrier).() -> Unit = {}): Barrier {
    return ankoView(::Barrier, theme = 0) {
        type = side
        referencedIds = ids
        init()
    }
}