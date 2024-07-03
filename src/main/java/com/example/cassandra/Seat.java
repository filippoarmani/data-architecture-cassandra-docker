package com.example.cassandra;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Seat {

    private String flight;
    private String id;
    private int balance;
    private String date_of_birth;
    private String document_info;
    private String name;
    private String status;
    private String surname;

    public Seat(String flight, String id, int balance, String date_of_birth, String document_info, String name, String status, String surname) {
        this.flight = flight;
        this.id = id;
        this.balance = balance;
        this.date_of_birth = date_of_birth;
        this.document_info = document_info;
        this.name = name;
        this.status = status;
        this.surname = surname;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "flight='" + flight + '\'' +
                ", id='" + id + '\'' +
                ", balance=" + balance +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", document_info='" + document_info + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
