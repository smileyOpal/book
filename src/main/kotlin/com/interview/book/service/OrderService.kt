package com.interview.book.service

import com.interview.book.domain.Book
import com.interview.book.domain.BookOrder
import com.interview.book.domain.OrderDetail
import com.interview.book.domain.OrderDetailId
import com.interview.book.repository.BookOrderRepository
import com.interview.book.repository.BookRepository
import com.interview.book.repository.OrderDetailRepository
import com.interview.book.rest.error.BadRequestException
import com.interview.book.rest.error.InternalServerException
import com.interview.book.service.dto.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface OrderService {
    fun getUserBooks(): UserOrderDTO
    fun createOrder(request: CreateOrderDTO): CalculatedOrderDTO
}

@Service
class OrderServiceImpl(
    private val userService: UserService,
    private val bookRepository: BookRepository,
    private val bookOrderRepository: BookOrderRepository,
    private val orderDetailRepository: OrderDetailRepository
) : OrderService {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun getUserBooks(): UserOrderDTO {
        val user = userService.getUserEntity()
        val userId = user.id ?: throw InternalServerException("Unexpected error when get user")
        log.info("Get user $userId ordered books")
        val orders = bookOrderRepository.findByUserId(user.id)
        val books = mutableListOf<OrderDTO>()
        if (orders.isNotEmpty()) {
            orders.forEach { order ->
                val details = orderDetailRepository.findByOrderDetailIdOrderId(order.id!!).map {
                    OrderDetailDTO(
                        bookId = it.orderDetailId.bookId,
                        amount = it.amount,
                        unitPrice = it.unitPrice,
                        totalPrice = it.totalPrice
                    )
                }
                books.add(OrderDTO(order.id, order.createdDate, details))
            }
        }
        return UserOrderDTO(
            firstName = user.firstName,
            lastName = user.lastName,
            dateOfBirth = user.dateOfBirth,
            orders = books.toList()
        )
    }

    @Transactional
    override fun createOrder(request: CreateOrderDTO): CalculatedOrderDTO {
        val user = userService.getUserEntity()
        val userId = user.id ?: throw InternalServerException("Unexpected error when get user")
        val books = bookRepository.findByIdIsIn(request.orders.map { it.bookId })
        if (request.orders.size != books.size) {
            val booksNotFound = request.orders.filter { order -> books.find { it.id == order.bookId } == null }
            throw BadRequestException("Invalid book(s) $booksNotFound")
        }
        val saveOrder = bookOrderRepository.save(BookOrder(null, userId))
        val orderId = saveOrder.id ?: throw InternalServerException("Unexpected error when save order")
        val orderDetails: List<OrderDetail> = request.orders.map { orderBook ->
            val book: Book = books.find { it.id == orderBook.bookId }
                ?: throw InternalServerException("Unexpected error when lookup book")
            OrderDetail(
                OrderDetailId(orderId, orderBook.bookId),
                orderBook.amount,
                book.price,
                orderBook.amount * book.price
            )
        }
        orderDetailRepository.saveAll(orderDetails)
        return CalculatedOrderDTO(orderDetails.sumByDouble { it.totalPrice })
    }

}