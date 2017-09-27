@file:Suppress("unused", "NOTHING_TO_INLINE")

package cz.ackee.anko_constraint_layout

import android.support.constraint.ConstraintHelper
import android.view.View

/**
 * A set of helper functions to manipulate referencedIds in [ConstraintHelper] class.
 *
 * @author David Khol [david.khol@ackee.cz]
 * @since 24.09.2017
 **/

fun ConstraintHelper.addViews(vararg views: View) {
    referencedIds = referencedIds.toMutableSet().apply {
        addAll(views.map { it.id })
    }.toIntArray()
}
fun ConstraintHelper.addViews(vararg views: ViewId) {
    referencedIds = referencedIds.toMutableSet().apply {
        addAll(views.toList())
    }.toIntArray()
}

fun ConstraintHelper.removeViews(vararg views: View) {
    referencedIds = referencedIds.toMutableSet().apply {
        removeAll(views.map { it.id })
    }.toIntArray()
}
fun ConstraintHelper.removeViews(vararg views: ViewId) {
    referencedIds = referencedIds.toMutableSet().apply {
        removeAll(views.toList())
    }.toIntArray()
}
