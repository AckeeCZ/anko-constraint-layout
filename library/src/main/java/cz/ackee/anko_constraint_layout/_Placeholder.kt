package cz.ackee.anko_constraint_layout

import android.support.constraint.Placeholder
import android.view.View
import org.jetbrains.anko.custom.ankoView

/**
 * A set of helper functions to generate a [Placeholder] view.
 *
 * @author David Khol [david.khol@ackee.cz]
 * @since 22.09.2017
 */

inline fun _ConstraintLayout.placeholder(view: View): Placeholder = placeholder(view) {}
inline fun _ConstraintLayout.placeholder(view: View, init: (Placeholder).() -> Unit): Placeholder {
    return placeholder(view.id, init = init)
}

inline fun _ConstraintLayout.placeholder(id: ViewId): Placeholder = placeholder(id) {}
inline fun _ConstraintLayout.placeholder(id: ViewId, init: (Placeholder).() -> Unit): Placeholder {
    return placeholder {
        this.setContentId(id)
        init()
    }
}

inline fun _ConstraintLayout.placeholder(): Placeholder = placeholder {}
inline fun _ConstraintLayout.placeholder(init: (Placeholder).() -> Unit): Placeholder {
    return ankoView(::Placeholder, theme = 0) {
        init()
    }
}
