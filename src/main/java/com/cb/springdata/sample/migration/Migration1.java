package com.cb.springdata.sample.migration;

import com.cb.springdata.sample.entities.User;
import com.cb.springdata.sample.repositories.UserRepository;
import com.cb.springdata.sample.service.UserService;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.query.CreatePrimaryQueryIndexOptions;
import com.couchbase.client.java.manager.query.CreateQueryIndexOptions;
import com.github.couchversion.changeset.ChangeLog;
import com.github.couchversion.changeset.ChangeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;


/**
 * If you are using Spring, you can Autowire your Services or Repositories
 */
@Component
@ChangeLog(order = "001")
public class Migration1 {

    @Autowired // Yes, You can autowire your migrations
    private UserService userService;

    @Autowired
    private UserRepository userRepository;



    @ChangeSet(order = "001", id = "createDummyData", author = "testAuthor")
    public void createDummyData(Bucket bucket){

        User user1 = new User(UUID.randomUUID().toString(), "Denis", null, null, null, null);
        userRepository.save(user1);

        User user2 = new User(UUID.randomUUID().toString(), "John", null, null, null, null);
        userRepository.save(user2);
    }


    @ChangeSet(order = "002", id = "createPrimaryIndex", author = "testAuthor")
    public void createInitialIndes(Cluster cluster, Bucket bucket){
        cluster.queryIndexes().createPrimaryIndex(bucket.name(),
                CreatePrimaryQueryIndexOptions.createPrimaryQueryIndexOptions().ignoreIfExists(true));
    }

    @ChangeSet(order = "003", id = "someBasicIndes", author = "testAuthor")
    public void createSomeBasicIndes(Cluster cluster, Bucket bucket){
        cluster.queryIndexes().createIndex(bucket.name(), "nameIndex", Arrays.asList("name"),
                CreateQueryIndexOptions.createQueryIndexOptions().ignoreIfExists(true));
    }

    @ChangeSet(order = "004", id = "userPartialIndex", author = "testAuthor")
    public void createPartialIndex(Cluster cluster, Bucket bucket ){
        cluster.query("CREATE INDEX user_idx ON `"+bucket.name()+"`(`_class`, `firstName`) WHERE (`_class` = '"+ User.class.getName()+"')");
    }

    @ChangeSet(order = "005", id = "copyNameToFirstName", author = "testAuthor")
    public void copyNameToFirstName(Cluster cluster, Bucket bucket){
        cluster.query("update `"+bucket.name()+"` set firstName = name WHERE (`_class` = '"+ User.class.getName()+"')");
    }

    @ChangeSet(order = "006", id = "deleteUserName", author = "testAuthor")
    public void deleteUserName(Cluster cluster, Bucket bucket){
        cluster.query(" UPDATE `"+bucket.name()+"` UNSET name WHERE (`_class` = '"+ User.class.getName()+"')");
    }

//    Uncomment this if you would like to see how a failed migration stops the application start
//    @ChangeSet(order = "007", id = "failApplicationToStart", author = "testAuthor")
//    public void failApplicationToStart(){
//
//        System.out.println("------------------------------ FAILING");
//        throw new IllegalStateException("Some fake error that happened during the migration.");
//    }
}
