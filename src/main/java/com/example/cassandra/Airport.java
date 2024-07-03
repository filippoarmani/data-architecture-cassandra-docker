package com.example.cassandra;

public class Airport {

    private String iata_code;
    private String country;
    private String country_code;
    private String geo_point;
    private String icao_code;
    private String name;
    private String name_en;
    private String name_fr;
    private String operator;
    private String phone;
    private int size;
    private String website;

    public Airport(String iata_code, String country, String country_code, String geo_point, String icao_code, String name, String name_en, String name_fr, String operator, String phone, int size, String website) {
        this.iata_code = iata_code;
        this.country = country;
        this.country_code = country_code;
        this.geo_point = geo_point;
        this.icao_code = icao_code;
        this.name = name;
        this.name_en = name_en;
        this.name_fr = name_fr;
        this.operator = operator;
        this.phone = phone;
        this.size = size;
        this.website = website;
    }

    public String getIata_code() {
        return iata_code;
    }

    public void setIata_code(String iata_code) {
        this.iata_code = iata_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(String geo_point) {
        this.geo_point = geo_point;
    }

    public String getIcao_code() {
        return icao_code;
    }

    public void setIcao_code(String icao_code) {
        this.icao_code = icao_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_fr() {
        return name_fr;
    }

    public void setName_fr(String name_fr) {
        this.name_fr = name_fr;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "iata_code='" + iata_code + '\'' +
                ", country='" + country + '\'' +
                ", country_code='" + country_code + '\'' +
                ", geo_point='" + geo_point + '\'' +
                ", icao_code='" + icao_code + '\'' +
                ", name='" + name + '\'' +
                ", name_en='" + name_en + '\'' +
                ", name_fr='" + name_fr + '\'' +
                ", operator='" + operator + '\'' +
                ", phone='" + phone + '\'' +
                ", size=" + size +
                ", website='" + website + '\'' +
                '}';
    }
}
