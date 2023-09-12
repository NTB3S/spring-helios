package com.s3b.helios.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Base model for an address of a user
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeliosAddressDto {

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
    private String zipcode;
    /**
     * This is the city where the address is located
     */
    private String city;
    /**
     * This is the country where the address is located
     */
    private String country;
}
