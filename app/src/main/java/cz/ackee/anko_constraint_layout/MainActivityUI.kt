package cz.ackee.anko_constraint_layout

import android.support.constraint.ConstraintSet
import android.transition.TransitionManager
import android.view.View
import android.widget.ImageView
import org.jetbrains.anko.*



/**
 * @author David Khol [david.khol@ackee.cz]
 * @since 18. 8. 2017
 **/
class MainActivityUI : AnkoComponentEx<MainActivity>() {

    lateinit var constraints1: ConstraintSet
    lateinit var constraints2: ConstraintSet

    override fun create(ui: AnkoContext<MainActivity>): View {
        return ui.constraintLayout {

            val constraintLayout = this

            val image = imageView {
                id = newId()
                scaleType = ImageView.ScaleType.CENTER
                imageResource = R.drawable.nav_header_bg
            }.lparams(matchConstraint, matchConstraint) {
                dimensionRatio = "H,16:9"
            }


            val name = defaultTextView("Joe") {
                textSize = 20f
            }
            val surname = defaultTextView("Thompson") {
                textSize = 26f
            }
            val age = defaultTextView("(32)") {
                textSize = 14f
            }

            val button = button {
                id = newId()
                text = "Click me"

                setOnClickListener {
                    TransitionManager.beginDelayedTransition(constraintLayout)
                    if (isActivated) {
                        constraints2.applyTo(constraintLayout)
                    } else {
                        constraints1.applyTo(constraintLayout)
                    }
                    isActivated = !isActivated
                }
            }


            constraints1 = constraints(generateIds = true) {

                val centerGuideId: Int = verticalGuidelinePercent(0.5f)

                name.connect(   STARTS of parentId with 16.dp,
                                TOPS of parentId with 16.dp)

                surname.connect(TOP to BOTTOM of name.id,
                                STARTS of name.id)

                age.connect(    START to END of surname.id with 8.dp,
                                BASELINES of surname.id)

                button.connect( RIGHTS of image.id,
                                TOP to BOTTOM of image.id)

                image.connect(  RIGHTS of centerGuideId,
                                TOPS of parentId,
                                LEFTS of parentId)
            }

            constraints2 = constraints {
                val leftGuideId: Int = verticalGuidelineBegin(72.dp)
                val rightGuideId: Int = verticalGuidelinePercent(0.8f)

                name.connect(   START to START of leftGuideId,
                                TOPS of parentId with 32.dp)

                surname.connect(HORIZONTAL of name.id)

                button.reset(BOTTOM)
                button.connect( HORIZONTAL of rightGuideId,
                                TOPS of parentId with 128.dp)

                image.reset(TOP)
                image.connect(  HORIZONTAL of parentId,
                                BOTTOMS of parentId)
            }
        }
    }
}
