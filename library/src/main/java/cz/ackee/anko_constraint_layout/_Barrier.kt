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

// TODO: 4. 9. 2017 david.khol: check if barriers need to have generated ids
// TODO: 4. 9. 2017 david.khol: generate ids for referenced views

typealias BarrierType = Int

val LEFT: BarrierType = Barrier.LEFT
val RIGHT: BarrierType = Barrier.RIGHT
val TOP: BarrierType = Barrier.TOP
val BOTTOM: BarrierType = Barrier.BOTTOM
val START: BarrierType = Barrier.START
val END: BarrierType = Barrier.END

inline fun _ConstraintLayout.barrier(side: BarrierType, vararg views: View): Barrier = barrier(side, *views) {}
inline fun _ConstraintLayout.barrier(side: BarrierType, vararg views: View, init: (Barrier).() -> Unit): Barrier {
    return barrier(side, *views.map { it.id }.toIntArray(), init = init)
}

inline fun _ConstraintLayout.barrier(side: BarrierType, vararg ids: ViewId): Barrier = barrier(side, *ids) {}
inline fun _ConstraintLayout.barrier(side: BarrierType, vararg ids: ViewId, init: (Barrier).() -> Unit): Barrier {
    return ankoView(::Barrier, theme = 0) {
        type = side
        referencedIds = ids
        init()
    }
}