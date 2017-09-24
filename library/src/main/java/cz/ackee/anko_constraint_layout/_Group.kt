@file:Suppress("unused", "NOTHING_TO_INLINE")

package cz.ackee.anko_constraint_layout

import android.support.constraint.Group
import android.view.View
import org.jetbrains.anko.custom.ankoView

/**
 * A set of helper functions to generate a [Group] view.
 *
 * Looking at the source code for [Group], it looks like that only visibility and elevation
 * attributes are supported at the time of writing this library, but more should become supported
 * with time.
 *
 * @author David Khol [david.khol@ackee.cz]
 * @since 3.9.2017
 */
fun Group.addViews(vararg views: View) {
    referencedIds = referencedIds.toMutableSet().apply {
        addAll(views.map { it.id })
    }.toIntArray()
}
fun Group.addViews(vararg views: ViewId) {
    referencedIds = referencedIds.toMutableSet().apply {
        addAll(views.toList())
    }.toIntArray()
}

fun Group.removeViews(vararg views: View) {
    referencedIds = referencedIds.toMutableSet().apply {
        removeAll(views.map { it.id })
    }.toIntArray()
}
fun Group.removeViews(vararg views: ViewId) {
    referencedIds = referencedIds.toMutableSet().apply {
        removeAll(views.toList())
    }.toIntArray()
}

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
