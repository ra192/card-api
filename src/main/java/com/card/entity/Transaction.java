package com.card.entity;

import com.card.entity.enums.TransactionStatus;
import com.card.entity.enums.TransactionType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Transaction implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String orderId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @OneToMany
    @JoinColumn
    private List<TransactionItem>items;

    public Transaction() {
    }

    public Transaction(String orderId, TransactionType type, TransactionStatus status, List<TransactionItem> items) {
        this.orderId = orderId;
        this.type = type;
        this.status = status;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public List<TransactionItem> getItems() {
        return items;
    }

    public void setItems(List<TransactionItem> items) {
        this.items = items;
    }
}
