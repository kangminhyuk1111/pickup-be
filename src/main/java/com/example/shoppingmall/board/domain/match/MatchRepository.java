package com.example.shoppingmall.board.domain.match;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
  @Query("SELECT m FROM Match m JOIN FETCH m.member ORDER BY m.createdAt DESC")
  List<Match> findAllWithMember();
}
