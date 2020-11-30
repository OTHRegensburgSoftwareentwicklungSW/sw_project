package de.othr.sw.bank.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public class Customer extends Person {

    private String taxNumber;

    @ManyToOne()
    @JoinColumn(name="address_id")
    private Address address;

    @OneToMany(mappedBy = "customer")
    private List<Account> accounts;

    public Customer(){}

    public Customer(String forename, String surname, String username, Date birthDate, String password, String taxNumber) {
        super(forename, surname, username, birthDate, password);
        this.taxNumber = taxNumber;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
