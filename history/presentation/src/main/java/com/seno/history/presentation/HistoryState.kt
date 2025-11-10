package com.seno.history.presentation

data class HistoryState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = true,
    val orderItems: List<OrderItem> = listOf(
        OrderItem(
            id = "12347",
            date = "November 10, 12:22",
            items = listOf("1 x Margherita"),
            totalPrice = 8.99,
        ),
        OrderItem(
            id = "12345",
            date = "November 1, 13:45",
            items = listOf(
                "1 x Margherita",
                "2 x Pepsi",
                "2 x Cookies Ice Cream",
            ),
            totalPrice = 25.45,
            status = OrderStatus.COMPLETED,
        ),
        OrderItem(
            id = "12346",
            date = "October 28, 19:53",
            items = listOf(
                "1 x Margherita",
                "2 x Cookies Ice Cream",
            ),
            totalPrice = 11.78,
            status = OrderStatus.COMPLETED,
        ),
    ),
)
