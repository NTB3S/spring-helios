package com.s3b.helios.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to define the corresponding User data table
 * @author Sébastien SAEZ
 */
@NoArgsConstructor
@Getter @Setter
@Entity(name = "HeliosUser")
@Table(name = "helios_user")
public class HeliosUserEntity {
    /**
     * The technical id of the row
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    /**
     * The subject of the saved user
     */
    private String subject;

    /**
     * The firstname persisted of the user
     */
    private String firstName;

    /**
     * The lastname persisted of the user
     */
    private String lastName;

    /**
     * This is the birthdate of the user
     */
    private LocalDate birthdate;

    /**
     * Every address of the user
     */
    @OneToMany(mappedBy = "heliosUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HeliosAddressEntity> address = new ArrayList<>();

    public HeliosUserEntity(String subject, String firstName, String lastName){
        this.subject = subject;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addAllAddress(List<HeliosAddressEntity> address){
        address.addAll(this.address);
        this.address = address;
    }
}
