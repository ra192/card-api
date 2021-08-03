package com.card.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class TransactionItem implements Serializable {
    @Id
    @SequenceGenerator(name = "transaction_item_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "transaction_item_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Long amount;

    @ManyToOne
    private Account srcAccount;

    @ManyToOne
    private Account destAccount;

    @ManyToOne
    private Transaction transaction;

    @Column(nullable = false)
    private LocalDateTime created;

    @ManyToOne
    private Card card;

    public TransactionItem() {
    }

    public TransactionItem(Transaction transaction, Long amount, Account srcAccount, Account destAccount, Card card) {
        this.transaction=transaction;
        this.amount = amount;
        this.srcAccount = srcAccount;
        this.destAccount=destAccount;
        this.created = LocalDateTime.now();
        this.card = card;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getSrcAccount() {
        return srcAccount;
    }

    public void setSrcAccount(Account srcAccount) {
        this.srcAccount = srcAccount;
    }

    public Account getDestAccount() {
        return destAccount;
    }

    public void setDestAccount(Account destAccount) {
        this.destAccount = destAccount;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Card getCard() {
        return card;
    }

    public void setSrcCard(Card card) {
        this.card = card;
    }


}
