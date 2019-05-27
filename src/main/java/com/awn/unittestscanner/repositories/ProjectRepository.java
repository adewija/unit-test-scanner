package com.awn.unittestscanner.repositories;

import com.awn.unittestscanner.entities.Account;
import com.awn.unittestscanner.entities.Project;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Override
    <S extends Project> S save(S s);

    @Override
    List<Project> findAll();

    @Override
    void delete(Project project);

    @Override
    Project getOne(Long aLong);

    @Override
    Optional<Project> findById(Long aLong);

    Optional<List<Project>> findAllByAccount_IdAccount(Long idAccount);

    Optional<Project> findProjectByGroupIdAndArtifactIdAndAccount(String groupId, String artifactId, Account account);

}
