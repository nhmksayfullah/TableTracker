package app.tabletracker.features.inventory.domain.usecase

import app.tabletracker.features.inventory.data.entity.MenuItem
import app.tabletracker.features.inventory.data.entity.meal.MealCourse

fun MenuItem.addNewMealCourse(mealCourse: MealCourse): MenuItem {
    val newMealCourses = mealCourses.toMutableList()
    newMealCourses.add(mealCourse)
    return this.copy(
        mealCourses = newMealCourses
    )
}

fun MenuItem.removeMealCourse(mealCourse: MealCourse): MenuItem {
    val newMealCourses = mealCourses.toMutableList()
    newMealCourses.remove(mealCourse)
    return this.copy(
        mealCourses = newMealCourses
    )
}

fun MenuItem.updateMealCourse(mealCourse: MealCourse): MenuItem {
    val newMealCourses = mealCourses.toMutableList()
    newMealCourses.replaceAll {
        if (it.id == mealCourse.id) {
            mealCourse
        } else {
            it
        }
    }
    return this.copy(
        mealCourses = newMealCourses
    )
}


fun MenuItem.addItemToMealCourse(mealCourse: MealCourse, item: MenuItem): MenuItem {
    val newMealCourses = mealCourses.toMutableList()
    var newMealCourse = mealCourses.find { it.id == mealCourse.id }
    if (newMealCourse != null) {
        val newAvailableItems = newMealCourse.availableItems.toMutableList()
        newAvailableItems.add(item)
        newMealCourse = newMealCourse.copy(availableItems = newAvailableItems)
        newMealCourses[newMealCourses.indexOf(mealCourse)] = newMealCourse
    }
    return this.copy(
        mealCourses = newMealCourses
    )
}

fun MenuItem.addItemsToMealCourse(mealCourse: MealCourse, items: List<MenuItem>): MenuItem {
    val newMealCourses = mealCourses.toMutableList()
    var newMealCourse = mealCourses.find { it.id == mealCourse.id }
    if (newMealCourse != null) {
        val newAvailableItems = newMealCourse.availableItems.toMutableList()
        val itemsToAdd = items.filter {
            id != it.id && !newAvailableItems.contains(it)
        }
        newAvailableItems.addAll(itemsToAdd)
        newMealCourse = newMealCourse.copy(availableItems = newAvailableItems)
        newMealCourses[newMealCourses.indexOf(mealCourse)] = newMealCourse
    }
    return this.copy(
        mealCourses = newMealCourses
    )
}

fun MenuItem.removeItemFromMealCourse(mealCourse: MealCourse, item: MenuItem): MenuItem {
    val newMealCourses = mealCourses.toMutableList()
    var newMealCourse = newMealCourses.find { it.id == mealCourse.id }
    if (newMealCourse != null) {
        val newAvailableItems = newMealCourse.availableItems.toMutableList()
        newAvailableItems.remove(item)
        newMealCourse = newMealCourse.copy(availableItems = newAvailableItems)
        newMealCourses[newMealCourses.indexOf(mealCourse)] = newMealCourse
    }
    return this.copy(
        mealCourses = newMealCourses
    )
}

fun MenuItem.contains(menuItem: MenuItem): Boolean {
    mealCourses.forEach {
        if (it.availableItems.contains(menuItem)) {
            return true
        }
    }
    return false
}

fun MealCourse.contains(menuItem: MenuItem): Boolean {
    return availableItems.contains(menuItem)
}

fun MenuItem.contains(mealCourse: MealCourse): Boolean {
    return mealCourses.contains(mealCourse)
}

fun MenuItem.findMealCourse(mealCourseId: String): MealCourse? {
    return mealCourses.find { it.id == mealCourseId }
}