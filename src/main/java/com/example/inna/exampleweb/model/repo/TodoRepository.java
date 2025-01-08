package com.example.inna.exampleweb.model.repo;

import com.example.inna.exampleweb.model.Todo;

import com.example.inna.exampleweb.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
 @Query("""
Select t from Todo t where t.user = :id and t.dateOfCompletion is null
""")
  List<Todo> findAllNotCompleteByUserId(@Param("id") User user);

  List<Todo> findAllByUser(User user);
}
