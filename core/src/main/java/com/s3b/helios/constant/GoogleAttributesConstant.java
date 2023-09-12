package com.s3b.helios.constant;


/**
 * String constants for the Google user infos.
 * @author Sébastien SAEZ
 */
public final class GoogleAttributesConstant {
    /**
     * Prevent class instantiation.
     */
    private GoogleAttributesConstant(){}

    /**
     * This is the subject key returned by google openId object.
     */
    public static final String SUBJECT_KEY = "sub";
    /**
     * This is the firstname key returned by google openId object.
     */
    public static final String GIVEN_NAME_KEY = "given_name";
    /**
     * This is the lastname key returned by google openId object.
     */
    public static final String FAMILY_NAME_KEY = "family_name";
    /**
     * This is the fullname key returned by google openId object.
     */
    public static final String FULLNAME_KEY = "name";
    /**
     * This is the email key returned by google openId object.
     */
    public static final String EMAIL_KEY = "email";
}
