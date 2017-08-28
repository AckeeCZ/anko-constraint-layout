package cz.ackee.anko_constraint_layout

import android.content.Context
import android.view.View
import android.view.ViewManager
import android.widget.TextView
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView

/**
 * @author David Khol [david.khol@ackee.cz]
 * @since 18. 8. 2017
 **/
abstract class AnkoComponentEx<in T>: AnkoComponent<T> {

	protected lateinit var context: Context
	protected val Int.dp: Int get() = this.dpf.toInt()
	protected val Int.dpf: Float get() = this * context.resources.displayMetrics.density

	protected fun ViewManager.defaultTextView(text: CharSequence, init: TextView.() -> Unit): TextView {
		return textView(text) {
			textColor = 0xFF000000.toInt()
			init()
		}
	}


	final override fun createView(ui: AnkoContext<T>): View {
		this.context = ui.ctx
		return create(ui)
	}

	abstract fun create(ui: AnkoContext<T>): View

}