package com.example.softwareProjectWithDocker.service;
import com.example.softwareProjectWithDocker.entity.record.SoftwareEngineerRecord;
import org.springframework.data.domain.Page;
import java.util.List;

public interface SEService {

    List<SoftwareEngineerRecord> getAllSEs();

    SoftwareEngineerRecord createSE(SoftwareEngineerRecord se);

    Page<SoftwareEngineerRecord> getAllSEswithPagiantions(int offset, int pageSize);

    Page<SoftwareEngineerRecord> getSEsWithPaginationAndSorting(int offset, int pageSize, String sortByField);

    SoftwareEngineerRecord findSeById(Long id);
}
