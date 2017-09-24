package cz.ackee.anko_constraint_layout

import android.os.Build
import android.support.constraint.Group
import android.support.constraint.Placeholder
import android.transition.TransitionManager
import android.view.View
import android.widget.Button
import android.widget.ImageView
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.button
import org.jetbrains.anko.imageView

/**
 * @author David Khol [david.khol@ackee.cz]
 * @since 18.8.2017
 **/
class PlaceholderUI : AnkoComponentEx<MainActivity>() {

    lateinit var image: View
    lateinit var buttonsGroup: Group
    lateinit var placeholder: Placeholder
    lateinit var buttons: List<Button>

    override fun create(ui: AnkoContext<MainActivity>): View {
        return ui.constraintLayout {

            image = imageView(R.drawable.nav_header_bg) {
                scaleType = ImageView.ScaleType.CENTER
                setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(this@constraintLayout)
                    }
                    isActivated = !isActivated
                    buttonsGroup.visibility = if (isActivated) View.GONE else View.VISIBLE
                }
            }

            placeholder = placeholder()

            val listener = { view: View ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(this@constraintLayout)
                }
                buttonsGroup.removeViews(view)
                if (placeholder.content != null) {
                    buttonsGroup.addViews(placeholder.content)
                }
                placeholder.setContent(if (placeholder.content == view) null else view)
            }

            buttons = (1..4).map {
                button("Button $it") {
                    setOnClickListener(listener)
                }
            }
            .toList()

            buttonsGroup = group(*buttons.toTypedArray())


            constraints {
//                // TODO: 23. 9. 2017 david.khol: this should be invalid :(
//                connect(TOPS of parentId)

                image.center(START of parentId, END of parentId)
                image.connect(TOPS of parentId)
                image.size(matchConstraint, wrapContent)

                placeholder.connect(ALL of image)

                chainSpread(START of parentId, END of parentId) {
                    views(*buttons.toTypedArray())
                }
                buttons.forEach {
                    it.connect(BOTTOMS of image)
                }
            }
        }
    }
}