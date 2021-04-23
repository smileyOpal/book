package com.interview.book.repository

import com.interview.book.domain.OrderDetail
import com.interview.book.domain.OrderDetailId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderDetailRepository : JpaRepository<OrderDetail, OrderDetailId> {
    fun findByOrderDetailIdOrderIdIn(orderIds: List<Long>): List<OrderDetail>
    fun findByOrderDetailIdOrderId(orderId: Long): List<OrderDetail>
}