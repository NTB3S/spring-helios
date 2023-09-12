package com.s3b.helios.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A class to define the corresponding address data table
 * @author Sébastien SAEZ
 */
@NoArgsConstructor(force = true)
@Getter
@Setter
@Entity(name = "HeliosAddress")
@Table(name = "helios_address")
public class HeliosAddressEntity {
    /**
     * This is the technical id of the address
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    /**
     * This is a full address of a user
     */
    private String address;
    /**
     * This is more information on the address of a user
     */
    private String addressComplement;
    /**
     * This is the public zipcode of the city
     */
    private String zipCode;
    /**
     * This is the city where the address is located
     */
    private String city;
    /**
     * This is the country where the address is located
     */
    private String country;
    /**
     * This is the relation with the user
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helios_user_id")
    private HeliosUserEntity heliosUser;

    public HeliosAddressEntity(String address, String addressComplement, String zipcode, String city, String country, HeliosUserEntity heliosUser) {
        this.address = address;
        this.addressComplement = addressComplement;
        this.zipCode = zipcode;
        this.city = city;
        this.country = country;
        this.heliosUser = heliosUser;
    }
}
