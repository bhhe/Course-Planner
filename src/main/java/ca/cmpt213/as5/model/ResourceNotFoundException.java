package ca.cmpt213.as5.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/*
 * ResourceNotFoundException.java
 * Class Description: Simple Exception thrown when
 *                    database tries to retrieve data that's
 *                    not exisitent.
 * Last modified on: April 9th, 2018
 * Authors: Bowen He & Jordan Hoang
 * Emails : bhhe@sfu.ca & jha257@sfu.ca
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception{
    public ResourceNotFoundException(String errMsg) {
        super(errMsg);
    }
}
