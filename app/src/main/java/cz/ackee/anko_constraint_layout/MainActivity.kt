package cz.ackee.anko_constraint_layout

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.setContentView

/**
 * @author David Khol [david.khol@ackee.cz]
 * @since 18. 8. 2017
 **/
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlaceholderUI().setContentView(this)
//        MainActivityUI().setContentView(this)
    }

}
