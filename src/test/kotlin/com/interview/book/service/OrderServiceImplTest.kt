package com.interview.book.service

import com.interview.book.domain.*
import com.interview.book.repository.BookOrderRepository
import com.interview.book.repository.BookRepository
import com.interview.book.repository.OrderDetailRepository
import com.interview.book.rest.error.InternalServerException
import com.interview.book.service.dto.CreateOrderDTO
import com.interview.book.service.dto.CreateOrderDetailDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

internal class OrderServiceImplTest {
    private lateinit var orderService: OrderService

    private val userService = mock(UserService::class.java)
    private val bookRepository = mock(BookRepository::class.java)
    private val bookOrderRepository = mock(BookOrderRepository::class.java)
    private val orderDetailRepository = mock(OrderDetailRepository::class.java)

    @BeforeEach
    fun setUp() {
        orderService = OrderServiceImpl(userService, bookRepository, bookOrderRepository, orderDetailRepository)
        `when`(userService.getUserEntity()).thenReturn(BookUser(1, "firstName", "lastName", "username", "password"))
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `Assert get user book case successfully`() {
        `when`(bookOrderRepository.findByUserId(1)).thenReturn(
            listOf(BookOrder(1, 1), BookOrder(2, 1))
        )
        `when`(orderDetailRepository.findByOrderDetailIdOrderId(1)).thenReturn(
            listOf(OrderDetail(OrderDetailId(1, 1), 1, 10.0, 10.0))
        )
        `when`(orderDetailRepository.findByOrderDetailIdOrderId(2)).thenReturn(
            listOf(OrderDetail(OrderDetailId(2, 2), 2, 20.0, 40.0))
        )
        val actual = orderService.getUserBooks()
        assertThat(actual.firstName).isEqualTo("firstName")
        assertThat(actual.lastName).isEqualTo("lastName")
        assertThat(actual.dateOfBirth).isNull()
        assertThat(actual.orders.size).isEqualTo(2)

        assertThat(actual.orders[0].orderId).isEqualTo(1)
        assertThat(actual.orders[0].detail.size).isEqualTo(1)
        assertThat(actual.orders[0].detail[0].bookId).isEqualTo(1)
        assertThat(actual.orders[0].detail[0].amount).isEqualTo(1)
        assertThat(actual.orders[0].detail[0].unitPrice).isEqualTo(10.0)
        assertThat(actual.orders[0].detail[0].totalPrice).isEqualTo(10.0)

        assertThat(actual.orders[1].orderId).isEqualTo(2)
        assertThat(actual.orders[1].detail.size).isEqualTo(1)
        assertThat(actual.orders[1].detail[0].bookId).isEqualTo(2)
        assertThat(actual.orders[1].detail[0].amount).isEqualTo(2)
        assertThat(actual.orders[1].detail[0].unitPrice).isEqualTo(20.0)
        assertThat(actual.orders[1].detail[0].totalPrice).isEqualTo(40.0)
    }

    @Test
    fun `Assert get user book case fail unexpected error`() {
        `when`(userService.getUserEntity()).thenReturn(BookUser(null, "firstName", "lastName", "username", "password"))
        assertThrows<InternalServerException> { orderService.getUserBooks() }
    }

    @Test
    fun `Assert create order case successfully`() {
        `when`(bookRepository.findByIdIsIn(listOf(1, 2))).thenAnswer { mock ->
            val ids = mock.getArgument<List<Long>>(0)
            ids.map { Book(it, 10.0 * it, "name", "author", false) }
        }

        `when`(bookOrderRepository.save(any())).thenReturn(BookOrder(1, 1))

        val actual = orderService.createOrder(
            CreateOrderDTO(
                listOf(
                    CreateOrderDetailDTO(bookId = 1, amount = 1),
                    CreateOrderDetailDTO(bookId = 2, amount = 2)
                )
            )
        )
        assertThat(actual.price).isEqualTo(50.0)
    }

    @Test
    fun `Assert create order case fail unexpected error`() {
        `when`(userService.getUserEntity()).thenReturn(
            BookUser(
                id = null,
                firstName = "firstName",
                lastName = "lastName",
                username = "username",
                password = "password"
            )
        )
        assertThrows<InternalServerException> {
            orderService.createOrder(
                CreateOrderDTO(listOf(CreateOrderDetailDTO(bookId = 1, amount = 1)))
            )
        }
    }
}