@file:Suppress("NOTHING_TO_INLINE")

package cz.ackee.anko_constraint_layout

import android.support.constraint.Group
import android.view.View
import org.jetbrains.anko.custom.ankoView

inline fun _ConstraintLayout.group(vararg views: View): Group = group(*views) {}
inline fun _ConstraintLayout.group(vararg views: View, init: (Group).() -> Unit): Group {
    return group(*views.map { it.id }.toIntArray(), init = init)
}

inline fun _ConstraintLayout.group(vararg ids: ViewId): Group = group(*ids) {}
inline fun _ConstraintLayout.group(vararg ids: ViewId, init: (Group).() -> Unit): Group {
    return ankoView(::Group, theme = 0) {
        referencedIds = ids
        init()
    }
}
