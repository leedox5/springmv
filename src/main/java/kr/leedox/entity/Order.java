package kr.leedox.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "paypal_order")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private int id;

    private String paypalOrderId;
    private String paypalOrderStatus;
}
