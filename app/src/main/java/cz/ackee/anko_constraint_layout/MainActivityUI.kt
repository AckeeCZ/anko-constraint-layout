package cz.ackee.anko_constraint_layout

import android.support.constraint.ConstraintSet
import android.view.View
import android.widget.ImageView
import org.jetbrains.anko.*

/**
 * @author David Khol [david.khol@ackee.cz]
 * @since 18.8.2017
 **/
class MainActivityUI : AnkoComponentEx<MainActivity>() {

    private lateinit var constraintLayout: _ConstraintLayout

    private lateinit var collapsedLayout: ConstraintSet
    private lateinit var expandedLayout: ConstraintSet

    private fun switchLayouts() {
        if (constraintLayout.isActivated) {
            expandedLayout.applyTo(constraintLayout)
        } else {
            collapsedLayout.applyTo(constraintLayout)
        }
        constraintLayout.isActivated = !constraintLayout.isActivated
    }


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

            val placeholder = placeholder()

            val buttonsGroup = group()

            val listener = { view: View ->
                beginDelayedTransition(constraintLayout)

                if (placeholder.content == view) {
                    switchLayouts()
                } else {
                    buttonsGroup.removeViews(view)
                    if (placeholder.content != null) {
                        buttonsGroup.addViews(placeholder.content)
                    }
                    placeholder.setContent(view)
                }
            }

            val buttons = (1..6).map {
                button("Button $it") {
                    setOnClickListener(listener)
                }
            }.toTypedArray()

            buttonsGroup.addViews(*buttons)


            collapsedLayout = prepareConstraints {
                val topGuideId: Int = horizontalGuidelineBegin(16.dp)

                name.connect(HORIZONTAL of background,
                        TOPS of topGuideId)

                surname.connect(TOP to BOTTOM of name,
                        HORIZONTAL of name)

                age.connect(START to END of surname with 8.dp,
                        BASELINES of surname)

                avatar.visibility(View.GONE)

                placeholder.apply {
                    width(matchConstraint)
                    height(32.dp)
                    connect(HORIZONTAL of background,
                            BOTTOMS of background)
                }

                background.apply {
                    size(240.dp, matchConstraint)
                    connect(HORIZONTAL of parentId,
                            TOPS of parentId)
                    dimensionRatio("H,3:2")
                }

                buttonsGroup.visibility(View.GONE)
            }

            expandedLayout = constraints {
                val fullNameBarrier: Int = barrier(LEFT, name, surname)

                name.apply {
                    connect(STARTS of background with 72.dp,
                            TOPS of background with 16.dp)
                }

                surname.apply {
                    connect(STARTS of name,
                            TOP to BOTTOM of name)
                }

                age.apply {
                    connect(ENDS of surname,
                            TOP to BOTTOM of surname)
                }

                avatar.apply {
                    center(START of background, START of name)
                    connect(TOPS of name,
                            BOTTOMS of surname)
                    size(48.dp, 48.dp)
                }

                placeholder.apply {
                    connect(STARTS of background with 16.dp,
                            VERTICAL of background)
                }

                background.apply {
                    width(matchConstraint)
                    connect(HORIZONTAL of parentId,
                            TOPS of parentId)
                    dimensionRatio("H,1:1")
                }

                buttonsGroup.visibility(View.VISIBLE)

                chainPacked(TOP of background, BOTTOM of background) {
                    views(*buttons)
                }
                buttons.forEach {
                    it.connect(ENDS of background with 16.dp)
                }
            }
        }.apply {
            constraintLayout = this
        }
    }
}
