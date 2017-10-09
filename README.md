# Anko Constraint Layout

This library adds missing support for Constraint Layout in Anko library. It is 
based on the `1.1.0-beta1` version of the library and adds support for `Group`, `Barrier` 
and `Placeholder` views as well.

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
* `with MARGIN` clause is optional and defines margin from the first view to the second one

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
`chain spread inside` is used, you can also pass weights parameter to mimic functionality 
of `LinearLayout` and it's weights. To make weights work, you also have to set the 
view's height or width to `matchConstraint` (0dp).

For more information about chains, have a look at [this great article](https://medium.com/@nomanr/constraintlayout-chains-4f3b58ea15bb) by Noman Rafique.

### Dimensions and Ratios
You can define view's width and height with `width()` and `height()` or `size()` methods.
Moreover you can define an [aspect ratio](https://developer.android.com/training/constraint-layout/index.html#ratio) 
for any view with `aspectRatio()` method. To make aspect ratio work you must set at least one of 
height and width to `matchConstraint`.

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

> Additionally for both `Barrier` and `Group` you can easily add to and remove from 
referenced views with `addViews(vararg View)` and `removeViews(vararg View)` respectively.


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
Another option how to define the bias is to use `View.center()` function which also
accepts a bias parameter:
```kotlin
constraints {
    avatar.center(START of background, START of name, 0.2f)
}
```

### Placeholders
You can define a [placeholder](http://androidkt.com/constraintlayout/#80f0) and dynamically 
replace the contents of the placeholder with `placeholder.setContent(View)` method:
```kotlin
constraintLayout {
    val placeholder = placeholder()
    
    button("Click me") {
        setOnClickListener {
            placeholder.setContent(this@button)
        }
    }
    
    constraints {
        button.connect(/* add connections */)
        placeholder.connect(/* add connections */)
    }
}
```

When you set a view as a content of the placeholder, the view will be displayed with 
layout params of the placeholder. When you set another view as a content of the placeholder
(or pass null as the content view), the original view will return to its original 
position and size. 

### Percent dimensions
Currently it is not possible to define percent dimensions through Constraint Set. 
To use percent dimensions, you must define attributes directly in the view's layout params.
```kotlin
constraintLayout {
    button().lparams {
        matchConstraintDefaultWidth = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_PERCENT
        matchConstraintPercentWidth = 0.8f
    }
}
```

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
You can define and switch between multiple Constraint Sets by simply using `constraints` block 
multiple times and storing returned values.

You can also use `prepareConstraints` block to define relations between views, but not apply it 
to the Constraint Layout. This might be useful when you define multiple constraint sets and 
don't want the changes from the first constraint set to be propagated to other ones.
```kotlin
constraintLayout {
    val collapsedConstraintSet = prepareConstraints {
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

## Chaining calls
You can also chain calls like this:
```kotlin
background
        .width(matchConstraint)
        .connect(HORIZONTAL of parentId)
        .connect(TOPS of parentId)
        .dimensionRatio("H,1:1")
```

## Sample
Sample app can be found in `app` module.

## Dependencies
Add this dependency to your project:
```groovy
implementation 'cz.ackee:anko-constraint-layout:0.6.2'
```
This library is based on the beta version of support library so make sure you add dependency for 
`1.1.0-beta1` version instead of `1.0.2` version:
```groovy
implementation 'com.android.support.constraint:constraint-layout:1.1.0-beta1'
```

## References
For more information about Constraint Layout in general, check out these websites:
 * [constraintlayout.com](https://constraintlayout.com/)
 * [developer.android.com/constraint-layout](https://developer.android.com/training/constraint-layout/index.html)
 * [realm.io/advanced-constraintlayout](https://academy.realm.io/posts/360-andev-2017-nicolas-roard-advanced-constraintlayout/)
