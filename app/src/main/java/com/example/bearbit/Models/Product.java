package com.example.bearbit.Models;

public class Product {

    public String qr;
    public String name;
    public String brand;
    public double carbs;
    public double protein;
    public double fat;
    public double sugar;
    public Long cal;

    public Product() {

    }

    public Product(String qr, String name, String brand, double carbs, double protein, double fat, double sugar, Long cal)
    {
        this.qr = qr;
        this.name = name;
        this.brand = brand;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.sugar = sugar;
        this.cal = cal;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public Long getCal() {
        return cal;
    }

    public void setCal(Long cal) {
        this.cal = cal;
    }




}
