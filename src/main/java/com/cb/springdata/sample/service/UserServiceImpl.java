package com.cb.springdata.sample.service;

import com.cb.springdata.sample.entities.Address;
import com.cb.springdata.sample.entities.User;
import com.cb.springdata.sample.repositories.UserRepository;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouchbaseTemplate template;

    @Autowired
    private Cluster cluster;

    @Override
    public List<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public User findById(String userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findUsersByPreferenceName(String name){
        return  userRepository.findUsersByPreferenceName(name);
    }

    @Override
    public boolean hasRole(String userId, String role) {
        return userRepository.hasRole(userId, role) != null;
    }

    //demo of how to run an ad hoc query
    @Override
    public List<User> findUserByAddress(String streetName, String number, String postalCode,
                                        String city, String country) {

        String bucketName = template.getBucketName();

        String queryString = "SELECT meta(b).id as id, b.* FROM "+bucketName+" b WHERE  b._class = '"+User.class.getName() +"' ";

        if( streetName != null ){
            queryString += " and b.address.streetName = '"+streetName+"' ";
        }

        if(number != null){
            queryString += " and b.address.houseNumber = '"+number+"' ";
        }

        if(postalCode != null){
            queryString += " and b.address.postalCode = '"+postalCode+"' ";
        }

        if(city != null){
            queryString += " and b.address.city = '"+city+"' ";
        }

        if(country != null){
            queryString += " and b.address.country = '"+country+"' ";
        }

        return cluster.query(queryString, QueryOptions.queryOptions().adhoc(true)
                .scanConsistency(QueryScanConsistency.REQUEST_PLUS) ).rowsAs(User.class);

    }

}
