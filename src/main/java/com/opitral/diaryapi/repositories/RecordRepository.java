package com.opitral.diaryapi.repositories;

import com.opitral.diaryapi.entities.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Long> {
    List<RecordEntity> findAllByOrderByDateDesc();
    List<RecordEntity> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
    Boolean existsByDate(LocalDate date);
    Boolean existsByDateAndIdNot(LocalDate date, Long id);
}
