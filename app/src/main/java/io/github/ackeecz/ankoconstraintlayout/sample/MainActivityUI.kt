package io.github.ackeecz.ankoconstraintlayout.sample

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import io.github.ackeecz.ankoconstraintlayout._ConstraintLayout
import io.github.ackeecz.ankoconstraintlayout.addViews
import io.github.ackeecz.ankoconstraintlayout.constraintLayout
import io.github.ackeecz.ankoconstraintlayout.group
import io.github.ackeecz.ankoconstraintlayout.placeholder
import io.github.ackeecz.ankoconstraintlayout.removeViews
import io.github.ackeecz.ankoconstraintlayout.setContent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.button
import org.jetbrains.anko.imageView

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

                placeholder
                        .width(matchConstraint)
                        .height(32.dp)
                        .connect(HORIZONTAL of background,
                                BOTTOMS of background)

                background
                        .size(240.dp, matchConstraint)
                        .connect(HORIZONTAL of parentId,
                                TOPS of parentId)
                        .dimensionRatio("H,3:2")

                buttonsGroup.visibility(View.GONE)
            }

            expandedLayout = constraints {
                val fullNameBarrier: Int = barrier(LEFT, name, surname)

                name    .connect(STARTS of background with 72.dp,
                                TOPS of background with 16.dp)

                surname .connect(STARTS of name,
                                TOP to BOTTOM of name)

                age     .connect(ENDS of surname,
                                TOP to BOTTOM of surname)

                avatar  .center(START of background, START of name)
                        .connect(TOPS of name,
                                BOTTOMS of surname)
                        .size(48.dp, 48.dp)

                placeholder
                        .connect(STARTS of background with 16.dp,
                                VERTICAL of background)

                background
                        .width(matchConstraint)
                        .connect(HORIZONTAL of parentId,
                            TOPS of parentId)
                        .dimensionRatio("H,1:1")

                buttonsGroup.visibility(View.VISIBLE)

                buttons.chainPacked(TOP of background, BOTTOM of background)
                        .forEach {
                            it.connect(ENDS of background with 16.dp)
                        }
            }
        }.apply {
            constraintLayout = this
        }
    }
}
