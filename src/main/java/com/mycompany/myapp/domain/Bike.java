package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Bike.
 */
@Entity
@Table(name = "bike")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bike implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "price")
    private String price;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "bike_driver",
               joinColumns = @JoinColumn(name="bikes_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="drivers_id", referencedColumnName="ID"))
    private Set<Driver> drivers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Bike name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public Bike color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public Bike price(String price) {
        this.price = price;
        return this;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Set<Driver> getDrivers() {
        return drivers;
    }

    public Bike drivers(Set<Driver> drivers) {
        this.drivers = drivers;
        return this;
    }

    public Bike addDriver(Driver driver) {
        drivers.add(driver);
        driver.getBikes().add(this);
        return this;
    }

    public Bike removeDriver(Driver driver) {
        drivers.remove(driver);
        driver.getBikes().remove(this);
        return this;
    }

    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bike bike = (Bike) o;
        if (bike.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bike.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bike{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", color='" + color + "'" +
            ", price='" + price + "'" +
            '}';
    }
}
