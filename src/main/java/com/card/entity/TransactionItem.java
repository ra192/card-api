package com.card.entity;

import com.card.entity.enums.TransactionItemType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class TransactionItem implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long amount;

    @ManyToOne
    private Account account;

    @Column(nullable = false)
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    private TransactionItemType type;

    @ManyToOne
    private Card card;

    public TransactionItem() {
    }
    public TransactionItem(Long amount, Account account, TransactionItemType type, Card card) {
        this.amount = amount;
        this.account = account;
        this.created = LocalDateTime.now();
        this.type = type;
        this.card = card;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public TransactionItemType getType() {
        return type;
    }

    public void setType(TransactionItemType type) {
        this.type = type;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
