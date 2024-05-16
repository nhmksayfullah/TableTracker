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
        populatePaymentInfo()
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
            [C]<font size='big-3'>${orderWithOrderItems.order.orderNumber}</font>${'\n'}${'\n'}
        """.trimIndent()
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
                    [L]${it.quantity}x ${it.menuItem.name}[R]£${price}${'\n'}
                """.trimIndent()
            }
        }

        receipt += orderItemsString
    }

    private fun populateTotalPrice() {
        receipt += """
            [L]${'\n'}
            [L]Sub Total: [R]£${orderWithOrderItems.order.totalPrice}${'\n'}
            [L]<b>Total:</b> [R]<b>£${orderWithOrderItems.order.totalPrice}</b>${'\n'}
            [L]${'\n'}${'\n'}
        """.trimIndent()
    }

    private fun populatePaymentInfo() {
        receipt += """
            [C]<font size='big'>Paid by ${orderWithOrderItems.order.paymentMethod}</font>${'\n'}${'\n'}
        """.trimIndent()
    }

    private fun populateDeliveryAddress() {
        if (orderWithOrderItems.order.orderType == OrderType.Delivery) {
            receipt += """
                [L]<b>Delivery Address</b>
                [R]<b>Name: ${orderWithOrderItems.order.customer?.name}</b>${'\n'}
                [R]<b>${orderWithOrderItems.order.customer?.postCode}</b>${'\n'}
                [R]<b>${orderWithOrderItems.order.customer?.houseNumber}</b>${'\n'}
                [R]<b>${orderWithOrderItems.order.customer?.street}</b>${'\n'}
                [R]<b>Contact: ${orderWithOrderItems.order.customer?.contact}</b>${'\n'}
            """.trimIndent()
        }
    }


    fun generateKitchenCopy(): String {
        val orderItems = orderWithOrderItems.orderItems.filter {
            it.menuItem.isKitchenCategory
        }

        kitchenCopy += """
            [C]<font size='big-3'>${orderWithOrderItems.order.orderNumber}</font>${'\n'}${'\n'}
        """.trimIndent()
        if (orderWithOrderItems.order.orderType == OrderType.DineIn) {
            kitchenCopy += """
                [C]<font size='bog'>Table number: ${orderWithOrderItems.order.tableNumber ?: "Unknown Table"}</font>${'\n'}
            """.trimIndent()
        }
        orderItems.forEach {
            if (it.orderItemStatus == OrderItemStatus.Added) {
                kitchenCopy += """
                [L]<font size='big-2'>${it.quantity}. ${it.menuItem.abbreviation}</font>${'\n'}
            """.trimIndent()
                if (it.addedNote.isNotEmpty()) {
                    kitchenCopy += """
                [L]    <b>${it.addedNote}</b>${'\n'}
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