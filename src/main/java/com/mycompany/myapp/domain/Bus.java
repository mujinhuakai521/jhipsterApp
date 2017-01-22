package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Bus.
 */
@Entity
@Table(name = "bus")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "bus_name")
    private String busName;

    @Column(name = "color")
    private String color;

    @Column(name = "car_number")
    private String carNumber;

    @ManyToOne
    private Human owner;

    @ManyToOne
    private Human driver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusName() {
        return busName;
    }

    public Bus busName(String busName) {
        this.busName = busName;
        return this;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getColor() {
        return color;
    }

    public Bus color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public Bus carNumber(String carNumber) {
        this.carNumber = carNumber;
        return this;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Human getOwner() {
        return owner;
    }

    public Bus owner(Human human) {
        this.owner = human;
        return this;
    }

    public void setOwner(Human human) {
        this.owner = human;
    }

    public Human getDriver() {
        return driver;
    }

    public Bus driver(Human human) {
        this.driver = human;
        return this;
    }

    public void setDriver(Human human) {
        this.driver = human;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bus bus = (Bus) o;
        if (bus.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bus{" +
            "id=" + id +
            ", busName='" + busName + "'" +
            ", color='" + color + "'" +
            ", carNumber='" + carNumber + "'" +
            '}';
    }
}
