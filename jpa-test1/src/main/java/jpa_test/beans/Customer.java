package jpa_test.beans;

import javax.persistence.*;

@Table(name="CUSTOMERS")
//@Entity(name="customers")
public class Customer {
    private Integer id;
    private String name;
    private String email;
    private int age;

    @Column(name="ID")
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="Email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="Age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
