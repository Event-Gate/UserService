package com.eventgate.userservice.repositories;

import com.eventgate.userservice.entities.User;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    @Aggregation(pipeline = {
            "{ $match: { '_id': ?0 } }",
            "{ $project: { '_id': 0, 'followerIds': 1 } }",
            "{ $unwind: '$followerIds' }",
            "{ $group: { '_id': null, 'ids': { $addToSet: '$followerIds' } } }"
    })
    Set<String> findFollowerIdsById(String userId);

    @Aggregation(pipeline = {
            "{ $match: { '_id': ?0 } }",
            "{ $project: { '_id': 0, 'followingIds': 1 } }",
            "{ $unwind: '$followingIds' }",
            "{ $group: { '_id': null, 'ids': { $addToSet: '$followingIds' } } }"
    })
    Set<String> findFollowingIdsById(String userId);
}
