package com.cb.springdata.sample.service;

import com.cb.springdata.sample.entities.User;

import java.util.List;

public interface UserService {

    User save(User user);

    User findById(String userId);

    List<User> findByName(String name);

    List<User> findUsersByPreferenceName(String name);

    boolean hasRole(String userId, String role);

    List<User> findUserByAddress(String streetName, String number, String postalCode,
                                 String city, String country);

}
