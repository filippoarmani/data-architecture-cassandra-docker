package com.example.cassandra;

public class Flight {

    private String departure;
    private String day;
    private String destination;
    private String hour;
    private String id;
    private String duration;
    private int number_of_seats;
    private String operator;
    private int price_per_person;

    public Flight(String departure, String day, String destination, String hour, String id, String duration, int number_of_seats, String operator, int price_per_person) {
        this.departure = departure;
        this.day = day;
        this.destination = destination;
        this.hour = hour;
        this.id = id;
        this.duration = duration;
        this.number_of_seats = number_of_seats;
        this.operator = operator;
        this.price_per_person = price_per_person;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getNumber_of_seats() {
        return number_of_seats;
    }

    public void setNumber_of_seats(int number_of_seats) {
        this.number_of_seats = number_of_seats;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getPrice_per_person() {
        return price_per_person;
    }

    public void setPrice_per_person(int price_per_person) {
        this.price_per_person = price_per_person;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "departure='" + departure + '\'' +
                ", day='" + day + '\'' +
                ", destination='" + destination + '\'' +
                ", hour='" + hour + '\'' +
                ", id='" + id + '\'' +
                ", duration='" + duration + '\'' +
                ", number_of_seats=" + number_of_seats +
                ", operator='" + operator + '\'' +
                ", price_per_person=" + price_per_person +
                '}';
    }
}
