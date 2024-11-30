package com.opitral.diaryapi.services;

import com.opitral.diaryapi.dto.RecordDto;
import com.opitral.diaryapi.entities.RecordEntity;
import com.opitral.diaryapi.exceptions.NoSuchEntityException;
import com.opitral.diaryapi.exceptions.ValidationException;
import com.opitral.diaryapi.repositories.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;

    public List<RecordEntity> getAll() {
        return recordRepository.findAllByOrderByDateDesc();
    }

    public RecordEntity getById(Long id) {
        return recordRepository.findById(id).orElseThrow(
                () -> new NoSuchEntityException(RecordEntity.class.getName(), "by id: " + id)
        );
    }

    public List<RecordEntity> getBetweenDates(LocalDate startDate, LocalDate endDate) {
        return recordRepository.findAllByDateBetween(startDate, endDate);
    }

    public RecordEntity save(RecordEntity record) {
        return recordRepository.save(record);
    }

    public void delete(Long id) {
        RecordEntity record = recordRepository.findById(id).orElseThrow(
                () -> new NoSuchEntityException(RecordEntity.class.getName(), "by id: " + id)
        );
        recordRepository.delete(record);
    }

    public List<RecordDto> getAllRecords() {
        return getAll().stream()
                .map(this::buildRecordResponseDto)
                .collect(Collectors.toList());
    }

    public RecordDto getRecordById(Long id) {
        return buildRecordResponseDto(getById(id));
    }

    public List<RecordDto> getRecordsBetweenDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            throw new ValidationException("At least one date must be provided");
        } else if (startDate == null) {
            startDate = LocalDate.of(1970, 1, 1);
        } else if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date must be before end date");
        }
        return getBetweenDates(startDate, endDate).stream()
                .map(this::buildRecordResponseDto)
                .collect(Collectors.toList());
    }

    public RecordDto getRecordByDate(LocalDate date) {
        RecordEntity record = recordRepository.findByDate(date);
        if (record == null) {
            throw new NoSuchEntityException(RecordEntity.class.getName(), "by date: " + date);
        }
        return buildRecordResponseDto(record);
    }

    public Long createRecord(RecordDto record) {
        LocalDate date;
        if (record.getDate() != null) {
            date = record.getDate();
        } else {
            date = LocalDate.now();
        }

        validateRecordDate(date);
        if (recordRepository.existsByDate(date)) {
            throw new ValidationException("Record for this date already exists");
        }

        RecordEntity recordEntity = RecordEntity.builder()
                .content(record.getContent())
                .date(date)
                .build();
        return save(recordEntity).getId();
    }

    public Boolean updateRecord(Long id, RecordDto record) {
        if (record.getDate() != null) {
            validateRecordDate(record.getDate());
            if (recordRepository.existsByDateAndIdNot(record.getDate(), record.getId())) {
                throw new ValidationException("Record for this date already exists");
            }
        }

        RecordEntity recordEntity = getById(id);
        if (record.getContent() != null)
            recordEntity.setContent(record.getContent());
        if (record.getDate() != null)
            recordEntity.setDate(record.getDate());
        save(recordEntity);
        return true;
    }

    public Boolean deleteRecord(Long id) {
        delete(id);
        return true;
    }

    public RecordDto buildRecordResponseDto(RecordEntity record) {
        RecordDto recordDto = new RecordDto();
        recordDto.setId(record.getId());
        recordDto.setContent(record.getContent());
        recordDto.setDate(record.getDate());
        return recordDto;
    }

    public void validateRecordDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new ValidationException("Record date cannot be in the future");
        } else if (date.isBefore(LocalDate.of(1970, 1, 1))) {
            throw new ValidationException("Record date cannot be before 1970");
        }
    }
}
