package cz.ackee.anko_constraint_layout

import android.os.Build
import android.support.constraint.ConstraintSet
import android.transition.TransitionManager
import android.view.View
import android.widget.ImageView
import org.jetbrains.anko.*

/**
 * @author David Khol [david.khol@ackee.cz]
 * @since 18.8.2017
 **/
class MainActivityUI : AnkoComponentEx<MainActivity>() {

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

            val button = button("Click me") {
                setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(this@constraintLayout)
                    }
                    if (isActivated) {
                        expandedConstraintSet
                    } else {
                        collapsedConstraintSet
                    }.applyTo(this@constraintLayout)
                    isActivated = !isActivated
                }
            }

            collapsedConstraintSet = constraints {
                val topGuideId: Int = horizontalGuidelineBegin(16.dp)

                name.connect(HORIZONTAL of background,
                        TOPS of topGuideId)

                surname.connect(TOP to BOTTOM of name,
                        HORIZONTAL of name)

                age.connect(START to END of surname with 8.dp,
                        BASELINES of surname)

                avatar.visibility(View.GONE)

                button.apply {
                    width(matchConstraint)
                    connect(HORIZONTAL of background,
                            BOTTOMS of background)
                }

                background.apply {
                    size(240.dp, matchConstraint)
                    connect(HORIZONTAL of parentId,
                            TOPS of parentId)
                    dimensionRatio("H,3:2")
                }
            }

            expandedConstraintSet = constraints {
                val topGuideId: Int = horizontalGuidelineBegin(16.dp)
                val leftGuideId: Int = verticalGuidelineBegin(72.dp)
                val fullNameBarrier: Int = barrier(LEFT, name, surname)

                name.apply {
                    clear(END, RIGHT)
                    connect(STARTS of leftGuideId,
                            TOPS of topGuideId)
                }

                surname.apply {
                    clear(END, RIGHT)
                    connect(STARTS of name,
                            TOP to BOTTOM of name)
                }

                age.apply {
                    clear(START, LEFT)
                    connect(ENDS of surname,
                            TOP to BOTTOM of surname)
                }

                avatar.apply {
                    center(START of parentId, START of name)
                    connect(TOPS of name,
                            BOTTOMS of surname)
                    visibility(View.VISIBLE)
                    size(48.dp, 48.dp)
                }

                button.apply {
                    clear(START, LEFT)
                    connect(ENDS of background with 16.dp,
                            BOTTOMS of background with 16.dp)
                    width(wrapContent)
                }

                background.apply {
                    width(matchParent)
                    clear(TOP)
                    connect(HORIZONTAL of parentId,
                            TOPS of parentId)
                    dimensionRatio("H,1:1")
                }
            }
        }
    }
}
