package app.tabletracker.feature_menu.domain.usecase

import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_menu.data.entity.meal.MealCourse

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
    newMealCourses.remove(this.mealCourses.find { it.id == mealCourse.id })
    newMealCourses.add(mealCourse)
    return this.copy(
        mealCourses = newMealCourses
    )
}

fun MenuItem.addItemToMealCourse(mealCourse: MealCourse, item: MenuItem): MenuItem {
    val newMealCourses = mealCourses.toMutableList()
    val newMealCourse = newMealCourses.find { it.id == mealCourse.id }
    if (newMealCourse != null) {
        newMealCourse.availableItems.toMutableList().add(item)
        newMealCourses[newMealCourses.indexOf(mealCourse)] = newMealCourse
    }
    return this.copy(
        mealCourses = newMealCourses
    )
}