package domain;

import java.math.BigDecimal;

import enumeration.Currency;

public class Transaction {

    private int id;
    private String email;
    private Currency currency;
    private BigDecimal amount;

    public Transaction(final int id, final String email, final Currency currency, final BigDecimal amount) {
        super();
        this.id = id;
        this.email = email;
        this.currency = currency;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }
}
