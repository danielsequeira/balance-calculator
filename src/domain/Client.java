package domain;

import java.math.BigDecimal;

public class Client {

    private String email;
    private String name;
    private BigDecimal balance;

    public Client(final String email, final String name, final BigDecimal balance) {
        super();
        this.email = email;
        this.name = name;
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }

    public void updateBalance(final BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
}
