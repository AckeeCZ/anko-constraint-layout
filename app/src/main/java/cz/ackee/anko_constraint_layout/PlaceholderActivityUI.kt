package cz.ackee.anko_constraint_layout

import android.os.Build
import android.support.constraint.ConstraintSet
import android.support.constraint.Placeholder
import android.transition.TransitionManager
import android.view.View
import android.widget.ImageView
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.button
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.imageView

/**
 * @author David Khol [david.khol@ackee.cz]
 * @since 18.8.2017
 **/
class PlaceholderActivityUI : AnkoComponentEx<MainActivity>() {

    private lateinit var collapsedConstraintSet: ConstraintSet
    private lateinit var expandedConstraintSet: ConstraintSet

    override fun create(ui: AnkoContext<MainActivity>): View {
        return ui.constraintLayout {

            val background = imageView(R.drawable.nav_header_bg) {
                scaleType = ImageView.ScaleType.CENTER
            }
            val avatar = imageView(R.drawable.ic_launcher_background)

            val name = defaultTextView("Joe") {
                textSize = 20f
            }
            val surname = defaultTextView("Thompson") {
                textSize = 26f
            }
            val age = defaultTextView("(32)") {
                textSize = 14f
            }

            val placeholder: Placeholder = ankoView(::Placeholder, theme = 0) {

            }

            val button = button("Click me") {
                setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(this@constraintLayout)
                    }
                }
            }
        }
    }
}