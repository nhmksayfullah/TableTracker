package app.tabletracker.feature_receipt.domain

import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.feature_order.data.entity.OrderItemStatus
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.util.toLocalDateTime

class ReceiptGenerator(
    private val restaurant: Restaurant?,
    private val orderWithOrderItems: OrderWithOrderItems
) {
    private var receipt = """
        """.trimIndent()
    private var kitchenCopy = """
        [C]<b>Kitchen Copy</b>${'\n'}
        [L]${'\n'}
    """.trimIndent()

    fun generateReceipt(): String {
        if (restaurant != null) {
            receipt += """
        [C]<font size='big'>${restaurant.name}</font>
        [C]${restaurant.address}
        [C]<b>Contact:</b> ${restaurant.contactNumber}
        ${if (restaurant.website != null) "[C]<b>Website:</b> ${restaurant.website}" else ""}
        [C]<b>VAT number:</b> ${restaurant.vatNumber}
        [L]${'\n'}
        """.trimIndent()
        }
        populateOrderNumber()
        populateOrderDetails()
        populateOrderItems()
        populateTotalPrice()
//        populatePaymentInfo()
        populateDeliveryAddress()
        receipt += """
            [L]${'\n'}
            [L]${'\n'}
        """.trimIndent()
        return receipt
    }

    private fun populateOrderNumber() {
        receipt += """
            [L]${'\n'}
            [C]<font size='big'>${orderWithOrderItems.order.orderNumber}</font>${'\n'}${'\n'}
        """.trimIndent()
        if (orderWithOrderItems.order.tableNumber != null) {
            receipt += """
                [C]<font size='big'>Total Person: ${orderWithOrderItems.order.totalPerson}</font>${'\n'}${'\n'}
            """.trimIndent()
        }
    }

    private fun populateOrderDetails() {
        val orderDetailsString = """
            [L]Order Type: ${orderWithOrderItems.order.orderType} [R]${
            orderWithOrderItems.order.creationTime.toLocalDateTime().toLocalDate()
        }
            [L]Order ID: ${orderWithOrderItems.order.id} [R]${
            orderWithOrderItems.order.creationTime.toLocalDateTime().toLocalTime()
        }${'\n'}${'\n'}
        """.trimIndent()

        receipt += orderDetailsString
    }

    private fun populateOrderItems() {
        var orderItemsString = """
        """.trimIndent()
        if (orderWithOrderItems.orderItems.isNotEmpty()) {
            orderWithOrderItems.orderItems.forEach {
                val price =
                    it.menuItem.prices[orderWithOrderItems.order.orderType]?.times(it.quantity)
                        ?: 0.0f
                orderItemsString += """
                    [L]<b>${it.quantity}x ${it.menuItem.name}</b>[R]<b>£${price}</b>${'\n'}
                """.trimIndent()
            }
        }

        receipt += orderItemsString
    }

    private fun populateTotalPrice() {
        receipt += """
            [L]${'\n'}
            [L]<font size='big'>Sub Total:</font> [R]<font size='big'>£${String.format("%.2f", orderWithOrderItems.order.totalPrice)}</font>${'\n'}
        """.trimIndent()
        if (orderWithOrderItems.order.discount != null) {
            val discount = orderWithOrderItems.order.discount.value.let {
                try {
                    it.toFloat() * orderWithOrderItems.order.totalPrice / 100
                } catch (e: Exception) {
                    0.0f
                }
            }
            receipt += """
                [L]<b>Discount:</b> [R]-£<b>${String.format("%.2f", discount)}</b>${'\n'}
                [L]<font size='big'>Total:</font> [R]<font size='big'>£${String.format("%.2f", orderWithOrderItems.order.totalPrice - discount)}</font>${'\n'}
            """.trimIndent()
        }
        receipt += """
            [L]${'\n'}${'\n'}
        """.trimIndent()
    }

    private fun populatePaymentInfo() {
        receipt += """
            [C]<font size='big'>Paid by ${orderWithOrderItems.order.paymentMethod}</font>${'\n'}${'\n'}
        """.trimIndent()
    }

    private fun populateDeliveryAddress() {
        if (orderWithOrderItems.order.orderType != OrderType.DineIn) {
            receipt += """
                [L]<b>Delivery Address</b>
                [R]<b>Name: ${orderWithOrderItems.order.customer?.name}</b>${'\n'}
                [R]<b>Post Code: ${orderWithOrderItems.order.customer?.postCode}</b>${'\n'}
                [R]<b>House Number: ${orderWithOrderItems.order.customer?.houseNumber}</b>${'\n'}
                [R]<b>Road: ${orderWithOrderItems.order.customer?.street}</b>${'\n'}
                [R]<b>Contact: ${orderWithOrderItems.order.customer?.contact}</b>${'\n'}
            """.trimIndent()
        }
    }


    fun generateKitchenCopy(printFullKitchenCopy: Boolean = false): String {
        val orderItems = orderWithOrderItems.orderItems.filter {
            it.menuItem.isKitchenCategory
        }

        kitchenCopy += """
            [C]${orderWithOrderItems.order.creationTime.toLocalDateTime()}${'\n'}
        """.trimIndent()

        kitchenCopy += """
            [C]<font size='big'>${orderWithOrderItems.order.orderType}</font>${'\n'}
        """.trimIndent()

        kitchenCopy += """
            [C]<font size='big'>${orderWithOrderItems.order.orderNumber}</font>${'\n'}${'\n'}
        """.trimIndent()
        if (orderWithOrderItems.order.orderType == OrderType.DineIn) {
            kitchenCopy += """
                [C]<font size='big'>Table number: ${orderWithOrderItems.order.tableNumber ?: "Unknown Table"}</font>${'\n'}${'\n'}
            """.trimIndent()
            if (orderWithOrderItems.order.totalPerson != null) {
                kitchenCopy += """
                [C]<font size='big'>Total Person: ${orderWithOrderItems.order.totalPerson}</font>${'\n'}${'\n'}
            """.trimIndent()
            }
        }
        if (orderWithOrderItems.order.orderType == OrderType.TakeOut) {
            kitchenCopy += """
                [C]<font size='big'>Customer: ${orderWithOrderItems.order.customer?.name}</font>${'\n'}${'\n'}
            """.trimIndent()
        }
        orderItems.forEach {
            if (!printFullKitchenCopy) {
                if (it.orderItemStatus == OrderItemStatus.Added) {
                    kitchenCopy += """
                [L]<font size='big'>${it.quantity}. ${it.menuItem.abbreviation}</font>${'\n'}
            """.trimIndent()
                    if (it.addedNote.isNotEmpty()) {
                        kitchenCopy += """
                [L]<font size='big'>N: ${it.addedNote}</font>${'\n'}${'\n'}
            """.trimIndent()
                    } else {
                        kitchenCopy += """
                        [L]${'\n'}
                    """.trimIndent()
                    }
                }
            } else {
                kitchenCopy += """
                [L]<font size='big'>${it.quantity}. ${it.menuItem.abbreviation}</font>${'\n'}
            """.trimIndent()
                if (it.addedNote.isNotEmpty()) {
                    kitchenCopy += """
                [L]<font size='big'>N: ${it.addedNote}</font>${'\n'}${'\n'}
            """.trimIndent()
                } else {
                    kitchenCopy += """
                        [L]${'\n'}
                    """.trimIndent()
                }
            }

        }
        kitchenCopy += """
            [L]${'\n'}
            [L]${'\n'}
        """.trimIndent()
        return kitchenCopy
    }

}