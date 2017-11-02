package cz.ackee.ankoconstraintlayout

import android.os.Build
import android.view.View
import java.util.concurrent.atomic.AtomicInteger


/**
 * Starting with android version Jelly Bean (17), we can programmatically generate unique
 * ids for our views using generateViewId() method. [ViewIdGenerator] object backports this
 * functionality to pre-JB version as well. Implementation for pre-JB is copied from AOSP
 * source codes and mirrors post-JB implementation.
 *
 * View ids defined in xml layouts and ids.xml are always generated with ids higher than 0x00FFFFFF.
 * That gives us a lot of space for generating our own ids under this limit.
 *
 * Note: If another library uses the same approach for dynamic generation of ids by mirroring the
 * post-JB implementation, it is possible to have id conflicts because both this library and the
 * other library counts nextGeneratedId separately. In such case don't rely on automatic generation
 * of ids provided by this library and manually specify ids for all views that for any reason
 * need to have an unique id
 *
 * @author David Khol [david.khol@ackee.cz]
 * @since 25. 8. 2017
 **/
object ViewIdGenerator {

    /**
     * Users may set their own implementation of idGenerator to eliminate problem with multiple
     * libraries using separate counters for generating new IDs
     */
    var idGeneratorImplementation: () -> Int = this::ourImplementation


    // generates a unique id every time this function is called
    fun newId(): Int {
        return idGeneratorImplementation()
    }


    private val nextIdGenerator = AtomicInteger(1)

    fun ourImplementation(): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            while (true) {
                val result = nextIdGenerator.get()
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                var newValue = result + 1
                if (newValue > 0x00FFFFFF)
                    newValue = 1 // Roll over to 1, not 0.
                if (nextIdGenerator.compareAndSet(result, newValue)) {
                    return result
                }
            }
        } else {
            return View.generateViewId()
        }
    }
}