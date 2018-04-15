package com.gd.orh.userMgt.repository;

import com.gd.orh.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    Page<User> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    User findByUsername(@Param("username") String username);
}
