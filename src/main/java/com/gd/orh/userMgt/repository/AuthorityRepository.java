package com.gd.orh.userMgt.repository;

import com.gd.orh.entity.Authority;
import com.gd.orh.entity.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
//@RepositoryRestResource(collectionResourceRel = "authority", path = "authority")
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    @Transactional(readOnly = true)
    Authority findByName(@Param("name") AuthorityName authorityName);
}
