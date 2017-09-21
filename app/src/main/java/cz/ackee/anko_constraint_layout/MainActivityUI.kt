package cz.ackee.anko_constraint_layout

import android.os.Build
import android.support.constraint.ConstraintSet
import android.support.constraint.Group
import android.transition.TransitionManager
import android.view.View
import android.widget.ImageView
import org.jetbrains.anko.*

/**
 * @author David Khol [david.khol@ackee.cz]
 * @since 18.8.2017
 **/
class MainActivityUI : AnkoComponentEx<MainActivity>() {

    private lateinit var constraints1: ConstraintSet
    private lateinit var constraints2: ConstraintSet

    override fun create(ui: AnkoContext<MainActivity>): View {
        return ui.constraintLayout {

            val image = imageView {
                scaleType = ImageView.ScaleType.CENTER
                imageResource = R.drawable.nav_header_bg
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

            val button = button("Click me") {
                setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(this@constraintLayout)
                    }
                    if (isActivated) {
                        constraints2.applyTo(this@constraintLayout)
                    } else {
                        constraints1.applyTo(this@constraintLayout)
                    }
                    isActivated = !isActivated
                }
            }

            val group: Group = this.group(name, surname)
            group.visibility = View.VISIBLE

            constraints1 = constraints {
                val centerGuideId: Int = verticalGuidelinePercent(0.5f)

                name.connect(
                        STARTS of parentId with 16.dp,
                        TOPS of parentId with 16.dp
                )
                surname.connect(
                        TOP to BOTTOM of name,
                        STARTS of name
                )
                age.connect(
                        START to END of surname with 8.dp,
                        BASELINES of surname
                )
                button.connect(
                        ENDS of image,
                        TOP to BOTTOM of image
                )
                image.size(matchConstraint, matchConstraint)
                image.aspectRatio("H,16:9")
                image.connect(
                        RIGHTS of centerGuideId,
                        TOPS of parentId,
                        LEFTS of parentId
                )
            }

            constraints2 = constraints {
                val leftGuideId: Int = verticalGuidelineBegin(72.dp)
                val rightGuideId: Int = verticalGuidelinePercent(2/3f)

                name.connect(
                        STARTS of leftGuideId,
                        TOPS of parentId with 32.dp
                )
                surname.connect(
                        TOP to BOTTOM of name,
                        HORIZONTAL of name
                )
                button.clear(BOTTOM)
                button.connect(
                        HORIZONTAL of rightGuideId,
                        TOPS of parentId with 128.dp
                )
                image.clear(TOP)
                image.connect(
                        HORIZONTAL of parentId,
                        BOTTOMS of parentId
                )
            }
        }
    }
}
