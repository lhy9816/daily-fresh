package com.example.lenovo.dailyfresh.shop_db;

public class Shop  {
    private Long id;
    private String name;
    private String balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Shop(Long id, String name, String balance) {
        super();
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Shop(String name, String balance) {
        super();
        this.name = name;
        this.balance = balance;
    }

    public Shop() {
        super();
    }
}
