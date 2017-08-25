@file:Suppress("NOTHING_TO_INLINE")

package cz.ackee.anko_constraint_layout

import android.support.constraint.Barrier
import android.view.View
import org.jetbrains.anko.custom.ankoView


inline fun _ConstraintLayout.barrier(side: Side, vararg views: View): Barrier = barrier(side, *views) {}
inline fun _ConstraintLayout.barrier(side: Side, vararg views: View, init: (Barrier).() -> Unit): Barrier {
    return barrier(side, *views.map { it.id }.toIntArray(), init = init)
}

inline fun _ConstraintLayout.barrier(side: Side, vararg ids: ViewId): Barrier = barrier(side, *ids) {}
inline fun _ConstraintLayout.barrier(side: Side, vararg ids: ViewId, init: (Barrier).() -> Unit): Barrier {
    return ankoView(::Barrier, theme = 0) {
        type = side
        referencedIds = ids
        init()
    }
}