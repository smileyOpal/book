package com.interview.book.domain

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "order_detail")
data class OrderDetail(
    @EmbeddedId val orderDetailId: OrderDetailId,
    @Column(name = "total_price", nullable = false) val totalPrice: Double,
) : AbstractAuditingEntity(), Serializable

@Embeddable
data class OrderDetailId(
    @Column(name = "order_id", nullable = false) val orderId: Long,
    @Column(name = "book_id", nullable = false) val bookId: Long
) : Serializable
