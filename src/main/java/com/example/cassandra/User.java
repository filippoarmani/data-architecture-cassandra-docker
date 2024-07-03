package com.example.cassandra;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class User {

    private int balance;
    private String date_of_birth;
    private String document_info;
    private String name;
    private String surname;

    public User(int balance, String date_of_birth, String document_info, String name, String surname) {
        this.balance = balance;
        this.date_of_birth = date_of_birth;
        this.document_info = document_info;
        this.name = name;
        this.surname = surname;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getDocument_info() {
        return document_info;
    }

    public void setDocument_info(String document_info) {
        this.document_info = document_info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "User{" +
                "balance=" + balance +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", document_info='" + document_info + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
