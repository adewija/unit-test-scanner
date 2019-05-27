package com.awn.unittestscanner.repositories;

import com.awn.unittestscanner.entities.Project;
import com.awn.unittestscanner.entities.WrongTestCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WrongTestCodeRepository extends JpaRepository<WrongTestCode, Long> {

    @Override
    <S extends WrongTestCode> S save(S s);

    void deleteWrongTestCodesByProject(Project project);
}
