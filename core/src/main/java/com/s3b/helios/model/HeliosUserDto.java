package com.s3b.helios.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Base model for a user
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeliosUserDto {

    /**
     * The id of a user.
     */
    private Long id;
    /**
     * The subject of a user
     */
    private String subject;

    /**
     * The firstname persisted of the user
     */
    private String firstName;

    /**
     * The lastname of the user
     */
    private String lastName;

    /**
     * This is the birthdate of the user
     */
    private LocalDate birthdate;

    /**
     * Every adresses of the user
     */
    private  List<HeliosAddressDto> address = new ArrayList<>();
}

