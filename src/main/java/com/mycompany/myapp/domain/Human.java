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
 * A Human.
 */
@Entity
@Table(name = "human")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Human implements Serializable {

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

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Bus> ownedBuses = new HashSet<>();

    @OneToMany(mappedBy = "driver")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Bus> drivedBuses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Human name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public Human age(String age) {
        this.age = age;
        return this;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public Human sex(String sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Set<Bus> getOwnedBuses() {
        return ownedBuses;
    }

    public Human ownedBuses(Set<Bus> buses) {
        this.ownedBuses = buses;
        return this;
    }

    public Human addOwnedBus(Bus bus) {
        ownedBuses.add(bus);
        bus.setOwner(this);
        return this;
    }

    public Human removeOwnedBus(Bus bus) {
        ownedBuses.remove(bus);
        bus.setOwner(null);
        return this;
    }

    public void setOwnedBuses(Set<Bus> buses) {
        this.ownedBuses = buses;
    }

    public Set<Bus> getDrivedBuses() {
        return drivedBuses;
    }

    public Human drivedBuses(Set<Bus> buses) {
        this.drivedBuses = buses;
        return this;
    }

    public Human addDrivedBus(Bus bus) {
        drivedBuses.add(bus);
        bus.setDriver(this);
        return this;
    }

    public Human removeDrivedBus(Bus bus) {
        drivedBuses.remove(bus);
        bus.setDriver(null);
        return this;
    }

    public void setDrivedBuses(Set<Bus> buses) {
        this.drivedBuses = buses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Human human = (Human) o;
        if (human.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, human.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Human{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", age='" + age + "'" +
            ", sex='" + sex + "'" +
            '}';
    }
}
