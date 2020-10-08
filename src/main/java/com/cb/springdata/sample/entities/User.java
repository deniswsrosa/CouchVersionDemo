package com.cb.springdata.sample.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User extends BasicEntity {

    @Id
    private String id;
    private String name;
    private String firstName;
    private Address address;
    private List<Preference> preferences = new ArrayList<>();
    private List<String> securityRoles = new ArrayList<>();


    //old constructor
    public User(String id, String name, Address address, List<Preference> preferences, List<String> securityRoles) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.preferences = preferences;
        this.securityRoles = securityRoles;
    }
}
