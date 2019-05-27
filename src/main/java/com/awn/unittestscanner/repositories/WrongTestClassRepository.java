package com.awn.unittestscanner.repositories;

import com.awn.unittestscanner.entities.Project;
import com.awn.unittestscanner.entities.WrongTestClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WrongTestClassRepository extends JpaRepository<WrongTestClass, Long> {

    @Override
    <S extends WrongTestClass> S save(S s);

    void deleteWrongTestClassesByProject(Project project);

}