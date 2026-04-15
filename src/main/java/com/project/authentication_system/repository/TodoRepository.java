package com.project.authentication_system.repository;

import com.project.authentication_system.entity.Todo;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@NullMarked
public interface TodoRepository extends JpaRepository<Todo, UUID> {
    List<Todo> findAllByUser_Id(UUID userId);
}
