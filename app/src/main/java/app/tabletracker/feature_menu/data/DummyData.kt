package app.tabletracker.feature_menu.data

import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_order.data.entity.OrderType

object DummyData {

    val categories = listOf(
        Category(name = "Appetizers"),
        Category(name = "Main Course"),
        Category(name = "Desserts"),
        Category(name = "Appetizers"),
        Category(name = "Main Course"),
        Category(name = "Desserts"),
        Category(name = "Appetizers"),
        Category(name = "Main Course"),
        Category(name = "Desserts")
    )

    val menuItems = mutableListOf<MenuItem>()

    init {
// Dummy menu items for Appetizers category
        menuItems.add(
            MenuItem(
                name = "Spring Rolls",
                abbreviation = "SR",
                description = "Crispy spring rolls filled with vegetables.",
                prices = mapOf(OrderType.DineIn to 5.99f, OrderType.TakeOut to 6.99f),
                categoryId = categories[0].id
            )
        )
        menuItems.add(
            MenuItem(
                name = "Chicken Wings",
                abbreviation = "CW",
                description = "Spicy chicken wings served with blue cheese dip.",
                prices = mapOf(OrderType.DineIn to 8.99f, OrderType.TakeOut to 9.99f),
                categoryId = categories[0].id
            )
        )
        menuItems.add(
            MenuItem(
                name = "Caprese Salad",
                abbreviation = "CS",
                description = "Fresh tomatoes, mozzarella, and basil with balsamic glaze.",
                prices = mapOf(OrderType.DineIn to 7.99f, OrderType.TakeOut to 8.99f),
                categoryId = categories[0].id
            )
        )

// Dummy menu items for Main Course category
        menuItems.add(
            MenuItem(
                name = "Spaghettianianian Bolognesenianainisn sdosldkng",
                abbreviation = "SB",
                description = "Classic spaghetti with meat sauce.",
                prices = mapOf(OrderType.DineIn to 12.99f, OrderType.TakeOut to 13.99f),
                categoryId = categories[0].id
            )
        )
        menuItems.add(
            MenuItem(
                name = "Grilled Salmon",
                abbreviation = "GS",
                description = "Grilled salmon fillet with lemon butter sauce.",
                prices = mapOf(OrderType.DineIn to 15.99f, OrderType.TakeOut to 16.99f),
                categoryId = categories[0].id
            )
        )
        menuItems.add(
            MenuItem(
                name = "Vegetable Stir-Fry",
                abbreviation = "VSF",
                description = "Assorted vegetables stir-fried with tofu.",
                prices = mapOf(OrderType.DineIn to 10.99f, OrderType.TakeOut to 11.99f),
                categoryId = categories[0].id
            )
        )

// Dummy menu items for Desserts category
        menuItems.add(
            MenuItem(
                name = "Chocolate Cake",
                abbreviation = "CC",
                description = "Decadent chocolate cake served with vanilla ice cream.",
                prices = mapOf(OrderType.DineIn to 6.99f, OrderType.TakeOut to 7.99f),
                categoryId = categories[0].id
            )
        )
        menuItems.add(
            MenuItem(
                name = "Apple Pie",
                abbreviation = "AP",
                description = "Homemade apple pie served warm with caramel sauce.",
                prices = mapOf(OrderType.DineIn to 5.99f, OrderType.TakeOut to 6.99f),
                categoryId = categories[0].id
            )
        )
        menuItems.add(
            MenuItem(
                name = "Cheesecake",
                abbreviation = "CC",
                description = "New York-style cheesecake with strawberry topping.",
                prices = mapOf(OrderType.DineIn to 7.99f, OrderType.TakeOut to 8.99f),
                categoryId = categories[0].id
            )
        )

        menuItems.add(
            MenuItem(
                name = "Chocolate Cake",
                abbreviation = "CC",
                description = "Decadent chocolate cake served with vanilla ice cream.",
                prices = mapOf(OrderType.DineIn to 6.99f, OrderType.TakeOut to 7.99f),
                categoryId = categories[0].id
            )
        )
        menuItems.add(
            MenuItem(
                name = "Apple Pie",
                abbreviation = "AP",
                description = "Homemade apple pie served warm with caramel sauce.",
                prices = mapOf(OrderType.DineIn to 5.99f, OrderType.TakeOut to 6.99f),
                categoryId = categories[0].id
            )
        )
        menuItems.add(
            MenuItem(
                name = "Cheesecake",
                abbreviation = "CC",
                description = "New York-style cheesecake with strawberry topping.",
                prices = mapOf(OrderType.DineIn to 7.99f, OrderType.TakeOut to 8.99f),
                categoryId = categories[0].id
            )
        )

        menuItems.add(
            MenuItem(
                name = "Chocolate Cake",
                abbreviation = "CC",
                description = "Decadent chocolate cake served with vanilla ice cream.",
                prices = mapOf(OrderType.DineIn to 6.99f, OrderType.TakeOut to 7.99f),
                categoryId = categories[0].id
            )
        )
        menuItems.add(
            MenuItem(
                name = "Apple Pie",
                abbreviation = "AP",
                description = "Homemade apple pie served warm with caramel sauce.",
                prices = mapOf(OrderType.DineIn to 5.99f, OrderType.TakeOut to 6.99f),
                categoryId = categories[0].id
            )
        )
        menuItems.add(
            MenuItem(
                name = "Cheesecake",
                abbreviation = "CC",
                description = "New York-style cheesecake with strawberry topping.",
                prices = mapOf(OrderType.DineIn to 7.99f, OrderType.TakeOut to 8.99f),
                categoryId = categories[0].id
            )
        )

        menuItems.add(
            MenuItem(
                name = "Chocolate Cake",
                abbreviation = "CC",
                description = "Decadent chocolate cake served with vanilla ice cream.",
                prices = mapOf(OrderType.DineIn to 6.99f, OrderType.TakeOut to 7.99f),
                categoryId = categories[0].id
            )
        )
        menuItems.add(
            MenuItem(
                name = "Apple Pie",
                abbreviation = "AP",
                description = "Homemade apple pie served warm with caramel sauce.",
                prices = mapOf(OrderType.DineIn to 5.99f, OrderType.TakeOut to 6.99f),
                categoryId = categories[0].id
            )
        )
        menuItems.add(
            MenuItem(
                name = "Cheesecake",
                abbreviation = "CC",
                description = "New York-style cheesecake with strawberry topping.",
                prices = mapOf(OrderType.DineIn to 7.99f, OrderType.TakeOut to 8.99f),
                categoryId = categories[0].id
            )
        )

    }

    // Creating CategoryWithMenuItems instances for each category
    val categoriesWithMenuItems = categories.map { category ->
        CategoryWithMenuItems(
            category = category,
            menuItems = menuItems.filter { it.categoryId == category.id }
        )
    }
}