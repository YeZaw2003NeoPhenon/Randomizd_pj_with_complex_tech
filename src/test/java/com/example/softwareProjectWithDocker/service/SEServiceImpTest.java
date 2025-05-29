package com.example.softwareProjectWithDocker.service;

import com.example.softwareProjectWithDocker.entity.SoftwareEngineer;
import com.example.softwareProjectWithDocker.entity.enums.Gender;
import com.example.softwareProjectWithDocker.entity.record.SoftwareEngineerRecord;
import com.example.softwareProjectWithDocker.exception.EngineerNotFoundException;
import com.example.softwareProjectWithDocker.repository.SERepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class SEServiceImpTest {

    @Mock
    private SERepo seRepo;

    @InjectMocks
    private SEServiceImp seServiceImp;

    private SoftwareEngineer engineer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        engineer = new SoftwareEngineer(1L, "John", "Doe", "Java", Gender.MALE);
        when(seRepo.save(any(SoftwareEngineer.class))).thenReturn(engineer);
    }

    @Test
    void testGetAllSEs() {
        when(seRepo.findAll()).thenReturn(Collections.singletonList(engineer));

        List<SoftwareEngineerRecord> result = seServiceImp.getAllSEs();

        assertThat(result.get(0).first_name()).isEqualTo("John");
        assertThat(result.get(0).last_name()).isEqualTo("Doe");

        assertThat(result.size()).isEqualTo(1);

        verify(seRepo, times(1)).findAll();
    }

    @Test
    void testCreateSE() {
        SoftwareEngineerRecord record = new SoftwareEngineerRecord(1L, "John", "Doe", "Java", Gender.MALE);

        SoftwareEngineerRecord result = seServiceImp.createSE(record);
        assertThat(result.id()).isEqualTo(record.id());
        assertThat(result.first_name()).isEqualTo(record.first_name());
        assertThat(result.last_name()).isEqualTo(record.last_name());
        verify(seRepo, times(1)).save(any(SoftwareEngineer.class));
    }

    @Test
    void testUpdateSE() {
        when(seRepo.findAll()).thenReturn(Collections.singletonList(engineer));

        SoftwareEngineerRecord updatedRecord = new SoftwareEngineerRecord(1L, "Jane", "Doe", "Python", Gender.FEMALE);

        SoftwareEngineerRecord result = seServiceImp.updateSe(1L, updatedRecord);

        assertThat(result.first_name()).isEqualTo(updatedRecord.first_name());
        assertThat(result.last_name()).isEqualTo(updatedRecord.last_name());
        verify(seRepo, times(1)).save(any(SoftwareEngineer.class));
    }

    @Test
    void testUpdateSe_NotFound() {
        when(seRepo.findAll()).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> seServiceImp.updateSe(1L, new SoftwareEngineerRecord(1L, "John", "Doe", "Java", Gender.MALE)))
                .isInstanceOf(EngineerNotFoundException.class)
                .hasMessage("Engineer with id 1 not found");
    }

    @Test
    void testGetAllSEsWithPagination() {
        Page<SoftwareEngineer> page = new PageImpl<>(Collections.singletonList(engineer));

        when(seRepo.findAll(PageRequest.of(0, 1))).thenReturn(page);

        Page<SoftwareEngineerRecord> result = seServiceImp.getAllSEswithPagiantions(0, 1);

        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(1);


        verify(seRepo, times(1)).findAll(PageRequest.of(0, 1));
    }

    @Test
    void testGetSEsWithPaginationAndSorting() {
        Page<SoftwareEngineer> page = new PageImpl<>(Collections.singletonList(engineer));

        when(seRepo.findAll(any(PageRequest.class))).thenReturn(page);

        Page<SoftwareEngineerRecord> result = seServiceImp.getSEsWithPaginationAndSorting(0, 1, "id");

        assertThat(result.getContent().size()).isEqualTo(1);

        verify(seRepo, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testFindSeById_Success() {
        SoftwareEngineer engineer = new SoftwareEngineer(1L, "John", "Doe", "Java",  Gender.MALE);

        when(seRepo.findById(1L)).thenReturn(Optional.of(engineer));

        SoftwareEngineerRecord result = seServiceImp.findSeById(1L);

        assertThat(result.first_name()).isEqualTo("John");
        assertThat(result.last_name()).isEqualTo("Doe");
        verify(seRepo, times(1)).findById(1L);
    }

    @Test
    void testFindSeById_NotFound() {
        when(seRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EngineerNotFoundException.class, () -> seServiceImp.findSeById(1L));

        verify(seRepo, times(1)).findById(1L);
    }

}