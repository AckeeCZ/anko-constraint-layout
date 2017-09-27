# Anko Constraint Layout

This library adds missing support for Constraint Layout in Anko library.

## Usage

Constraint Layout is defined and added to other ViewGroups in the same way as any other view in Anko:
```kotlin
anyViewGroupLayout {
    constraintLayout {
        val name = textView("David")
        val surname = textView("Khol")
    
        constraints {
            name.connect(
                    STARTS of parentId with 16.dp,
                    TOPS of parentId with 16.dp
            )
            surname.connect(
                    TOP to BOTTOM of name,
                    STARTS of name
            )
        }
    }.lparams(matchParent, matchParent)
}
```
or it can be also created through `ViewManager`, `Context`, `Activity` as usual.

## Views positioning and ConstraintSet

To correctly position all views inside the Constraint Layout, we can set layout params to each child
it contains just as if we were using traditional xml definitions.

When defining layout programmatically, another approach is preferred. Instead of specifying layout
params for each child, we can specify relations between children through a `ConstraintSet`.
`ConstraintSet` adds extra helper methods to define constraints more expressively and intuitively.

You can create, define constraints into and then apply a Constraint Set to a Constraint Layout 
using `constraints` block:

```kotlin
constraintLayout {
    // view definitions
    
    constraints {
        // view constraint definitions
    }
}
```

Inside of the `constraints` block we can use various methods to define our layout:
 
### Connect

The most common thing to do with Constraint Layout is defining constraints. You can define a constraint
using `View.connect()` method.

```kotlin
constraints {
    name.connect(
            STARTS of parentId with 16.dp,
            TOPS of parentId with 16.dp
    )
    surname.connect(
            TOP to BOTTOM of name,
            STARTS of name
    )
    avatar.connect(
            HORIZONTAL of name,
            TOP to BOTTOM of name with 8.dp
    )
}
```

`View.connect()` method accepts variable amount of constraints. Each constraint is defined like this:

```SIDE to SIDE of VIEW [with MARGIN]``` 
* First `SIDE` defines side of the view we create constraints for. 
* Second `SIDE` defines side of the view we connect first view to.
* `VIEW` defines the view we connect first view to
* `with MARGIN` clause is optional and defines from first view to the second one

Available `SIDE`s are: `LEFT`, `RIGHT`, `TOP`, `BOTTOM`, `BASELINE`, `START`, `END`
 
To reduce boilerplate, instead of `START to START` you can just specify `STARTS` etc.
Additionally you can use `HORIZONTAL` to define `LEFTS` and `RIGHTS` at the same time and 
analogously `VERTICAL` to define `TOPS` and `BOTTOMS`.
Moreover, you can use `ALL` to define constraints for all four sides at the same time. 

### Chains
You can define [chains](https://developer.android.com/training/constraint-layout/index.html#constrain-chain)
with `chain()`, `chainSpread()`, `chainSpreadInside()` or `chainPacked()` methods like this: 

```kotlin
constraints {
    val views = arrayOf(name, surname) 
    views.chainSpread(TOP of parentId, BOTTOM of parentId)
}
```
You have to define at least 2 elements of the chain. When either `chain spread` or 
`chain spread inside` is used, you can also define pass weights parameter to mimic 
functionality of `LinearLayout` and it's weights. To make weights work, you also have to set the 
view's height or width to `match_constraints` (0dp).

For more information about chains, have a look at [this great article](https://medium.com/@nomanr/constraintlayout-chains-4f3b58ea15bb) by Noman Rafique.

### Dimensions and Ratios
You can define view's width and height with `width()` and `height()` or `size()` methods.

```kotlin
constraints {
    image.size(matchConstraint, matchConstraint)
    image.aspectRatio("H,16:9")
}                
```

### Guidelines
You can create [guidelines](https://developer.android.com/reference/android/support/constraint/Guideline.html) 
in two ways - either as a standalone `View`:
```kotlin
constraintLayout {
    val topGuide: Guideline = horizontalGuidelineBegin(dip(24))
}
```
or as a part of Constraint Set:
```kotlin
constraints {
    val leftGuideId: Int = verticalGuidelineBegin(dip(72))
}
```
Ultimately, it is up to you which one you want to use as you can make references to either of those 
inside of `connect()` method.
 ```kotlin
constraintLayout {
    val name = textView("David")
    val topGuide: Guideline = horizontalGuidelineBegin(dip(24))
    
    constraints {
        val leftGuideId: Int = verticalGuidelineBegin(dip(72))
        name.connect(
                STARTS of leftGuideId,
                TOP of topGuide
        )
    }
}
```
All six combinations of helper methods (`HORIZONTAL | VERTICAL` combined with 
`BEGIN | END | PERCENT`) are available.

### Barriers
Similarly to guidelines, you can also define barriers either as a standalone `View`:
```kotlin
constraintLayout {
    val descriptionBarrier: Barrier = barrierLeft(name, surname)
}
```
or as a part of Constraint Set:
```kotlin
constraints {
    val descriptionBarrier: Int = barrierLeft(name, surname)
}
```
Ultimately, the use inside of `connect()` method does not differ:
```kotlin
constraints {
    // ...
    description.connect(
            ENDS of descriptionBarrier,
            TOPS of parentId
    )
}
```

### Groups
You can define a group of views and control its visibility and elevation (and possibly more with
future updates to the Constraint Layout library) for all referenced views.
```kotlin
constraintLayout {
    val buttonsGroup = group(buttonOne, buttonTwo)
    
    constraints {
        buttonsGroup.visibility(View.GONE)
    }
}
```


### Biases
If you constrain a view from both sides horizontally or vertically, you can also define 
[bias](https://developer.android.com/reference/android/support/constraint/ConstraintLayout.html#Bias).
```kotlin
constraints {
    avatar.connect(
        HORIZONTAL of parentId,
        TOP of parentId
    )
    avatar.horizontalBias(0.2f)
}
```

### Placeholders
TODO

### Percent dimensions
Not yet fully supported.

Currently not possible to define through Constraint Set. To use percent dimensions, define 
attributes of the view in layout params.
```kotlin
constraintLayout {
    button().lparams {
        matchConstraintDefaultWidth = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_PERCENT
        matchConstraintPercentWidth = 0.8f
    }
}
```
<!---
percent dimensions {
    android:layout_width="0dp"
    app:layout_constraintWidth_default="percent"
    app:layout_constraintWidth_percent=".4"
}
--->

## IDs

Constraint Layout heavily depends on ids of its child views. 
For that reason, each child has to have defined a unique id.
We can predefine static ids for each view in `ids.xml` file to be generated by aapt and reference
them in code by `R.id.name_of_view_id`. This can be burdensome to do for every view we add.

This library automatically generates a **dynamic** unique id for each view that is added to the 
Constraint Layout and does not have a specified id.
This ensures positioning of views works correctly, but these ids do NOT get retained across 
configuration changes. That means any view that save its state into a bundle to be persisted across
configuration change will NOT be able to restore its state.
For numerous views such as `TextView`, `Button`, `ImageView`, etc. it is not a big deal because these
views usually don't modify their state based on user input. For other views such as `EditText`, 
`CheckBox`, `RadioButton`, `SeekBar`, etc. it is strongly advised to specify a static id so that
Android framework can restore the view's state automatically.

If you want to disable this functionality, you may set generateIds to false.
```kotlin
val view = context.constraintLayout {

    generateIds = false
    // added views here won't have generated ids
                 
    generateIds = true
    // added views here will once again have generated ids, unless an id has been assigned during their creation
}
```

> Do NOT change ids of views after they have been added to the ConstraintLayout. ConstrainLayout
internally stores references to its views via views' id when they have been added. Changing view's id
and referencing it through its new id will not work and the view will most likely not even get displayed.

## Managing multiple Constraint Sets
You can define and switch between multiple Constraint Sets
by simply using `constraints` block multiple times and 
storing the returned value
```kotlin
constraintLayout {
    val collapsedConstraintSet = constraints {
        // set of constraints
    }
    
    val expandedConstraintSet = constraints {
        // another set of constraints
    }
}
```
Then during runtime you can easily switch between different layouts without recreating the layout.
```kotlin
if (isActivated) {
    expandedConstraintSet
} else {
    collapsedConstraintSet
}.applyTo(this@constraintLayout)
```


## Sample

Sample app can be found in `app` module.

## Dependencies

Add this dependency to your project:
```groovy
implementation 'cz.ackee:anko-constraint-layout:0.4.1'
```

This library is still in development and your implementation might break when minor version gets bumped up. For now, backwards non-compatible changes happens about once every week. 

## References

For more information about Constraint Layout in general, check out these websites:
 * [constraintlayout.com](https://constraintlayout.com/)
 * [developer.android.com/constraint-layout](https://developer.android.com/training/constraint-layout/index.html)
 * [realm.io/advanced-constraintlayout](https://academy.realm.io/posts/360-andev-2017-nicolas-roard-advanced-constraintlayout/)
