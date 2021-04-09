package io.github.ackeecz.ankoconstraintlayout

import androidx.core.view.ViewCompat

/**
 * Object used for generating new View ids. It provides a way to override the algorithm of id
 * generation if needed.
 */
object ViewIdGenerator {

    /**
     * Users may set their own implementation of idGenerator to eliminate problem with multiple
     * libraries using separate counters for generating new IDs.
     * It defaults to [ViewCompat.generateViewId].
     */
    var idGeneratorImplementation: () -> Int = ViewCompat::generateViewId

    /**
     * Generates a unique id every time this function is called
     */
    fun newId() = idGeneratorImplementation()
}
