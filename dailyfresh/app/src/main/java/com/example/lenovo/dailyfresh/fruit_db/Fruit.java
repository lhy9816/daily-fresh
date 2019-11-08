package com.example.lenovo.dailyfresh.fruit_db;

public class Fruit  {
    private Long id;
    private String name;
    private int balance;

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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Fruit(Long id, String name, int balance) {
        super();
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Fruit(String name, int balance) {
        super();
        this.name = name;
        this.balance = balance;
    }

    public Fruit() {
        super();
    }
}
