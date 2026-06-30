package com.zuhaisoft.linkedInProject.ConnectionsService.repository;

import com.zuhaisoft.linkedInProject.ConnectionsService.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> findByUserId(Long userId);

    @Query("match (personA:Person) -[:CONNECTED_TO]- (personB:Person) " +
            "where personA.userId = $userId "+
            "return DISTINCT personB")
    List<Person> getFirstDegreeConnections(Long userId);

    @Query("MATCH (personA:Person {userId: $userId})-[:CONNECTED_TO]-(mutual:Person)-[:CONNECTED_TO]-(personC:Person) " +
            "WHERE personA <> personC " +
            "AND NOT (personA)-[:CONNECTED_TO]-(personC) " +
            "RETURN DISTINCT personC")
    List<Person> getSecondDegreeConnections(Long userId);

    @Query("MATCH (personA:Person {userId: $userId})-[:CONNECTED_TO*3]-(personD:Person) " +
            "WHERE personA <> personD " +
            "AND NOT (personA)-[:CONNECTED_TO*1..2]-(personD) " +
            "RETURN DISTINCT personD")
    List<Person> getThirdDegreeConnections(Long userId);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean connectionRequestExists(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person)-[r:CONNECTED_TO]-(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean alreadyConnected(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person), (p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "CREATE (p1)-[:REQUESTED_TO]->(p2)")
    void addConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "DELETE r " +
            "CREATE (p1)-[:CONNECTED_TO]->(p2)")
    void acceptConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "DELETE r")
    void rejectConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (p:Person {userId: $senderId}) RETURN p.name")
    String findNameByUserId(Long senderId);
}
