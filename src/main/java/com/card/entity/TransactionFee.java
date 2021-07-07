package com.card.entity;

import com.card.entity.enums.TransactionType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class TransactionFee implements Serializable {
    @Id
    @SequenceGenerator(name = "transaction_fee_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "transaction_fee_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    private Account account;

    @Column
    private Float rate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }
}
