package app.tabletracker.features.inventory.util

enum class EditMenuTabOption(val state: Int) {
    Details(0), Prices(1), Advance(2)
}

enum class DatabaseOperation {
    Add, Edit, Delete, Read
}