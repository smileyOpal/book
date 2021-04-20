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
import com.interview.book.service.dto.CalculatedOrderDTO
import com.interview.book.service.dto.CreateOrderDTO
import com.interview.book.service.dto.UserOrderDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Collections.emptyList
import java.util.Collections.emptySet

interface OrderService {
    fun getUserBooks(): UserOrderDTO
    fun createOrder(request: CreateOrderDTO): CalculatedOrderDTO
}

@Service
class OrderServiceImpl(
    val userService: UserService,
    val bookRepository: BookRepository,
    val bookOrderRepository: BookOrderRepository,
    val orderDetailRepository: OrderDetailRepository
) : OrderService {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun getUserBooks(): UserOrderDTO {
        val user = userService.getUserEntity()
        val userId = user.id ?: throw InternalServerException("Unexpected error when get user")
        log.info("Get user $userId ordered books")
        val orderIds = bookOrderRepository.findByUserId(user.id).map { it.id!! }
        val books = if (orderIds.isNotEmpty()) {
            orderDetailRepository.findByOrderDetailIdOrderIdIn(orderIds).map { it.orderDetailId.bookId }.toSet()
        } else {
            log.info("User $userId has no order")
            emptySet<Long>()
        }
        return UserOrderDTO(
            firstName = user.firstName,
            lastName = user.lastName,
            dateOfBirth = user.dateOfBirth,
            books = books
        )
    }

    @Transactional
    override fun createOrder(request: CreateOrderDTO): CalculatedOrderDTO {
        val user = userService.getUserEntity()
        val userId = user.id ?: throw InternalServerException("Unexpected error when get user")
        val books = bookRepository.findByIdIsIn(request.orders)
        if (request.orders.size != books.size) {
            val booksNotFound = request.orders.filter { order -> books.find { it.id == order } == null }
            throw BadRequestException("Invalid book(s) $booksNotFound")
        }
        val saveOrder = bookOrderRepository.save(BookOrder(null, userId))
        val orderId = saveOrder.id ?: throw InternalServerException("Unexpected error when save order")
        request.orders.forEach { orderBook ->
            val book: Book = books.find { it.id == orderBook } ?: throw InternalServerException("Unexpected error when lookup book")
            orderDetailRepository.save(OrderDetail(OrderDetailId(orderId, orderBook), book.price))
        }
        return CalculatedOrderDTO(books.sumByDouble { it.price })
    }

}