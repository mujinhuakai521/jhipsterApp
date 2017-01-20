package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Driver.
 */
@Entity
@Table(name = "driver")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private String age;

    @Column(name = "sex")
    private String sex;

    @ManyToMany(mappedBy = "drivers")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Bike> bikes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Driver name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public Driver age(String age) {
        this.age = age;
        return this;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public Driver sex(String sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Set<Bike> getBikes() {
        return bikes;
    }

    public Driver bikes(Set<Bike> bikes) {
        this.bikes = bikes;
        return this;
    }

    public Driver addBike(Bike bike) {
        bikes.add(bike);
        bike.getDrivers().add(this);
        return this;
    }

    public Driver removeBike(Bike bike) {
        bikes.remove(bike);
        bike.getDrivers().remove(this);
        return this;
    }

    public void setBikes(Set<Bike> bikes) {
        this.bikes = bikes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Driver driver = (Driver) o;
        if (driver.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, driver.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Driver{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", age='" + age + "'" +
            ", sex='" + sex + "'" +
            '}';
    }
}
