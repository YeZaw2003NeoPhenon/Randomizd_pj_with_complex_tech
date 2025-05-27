package com.example.softwareProjectWithDocker.service;

import com.example.softwareProjectWithDocker.entity.SoftwareEngineer;
import com.example.softwareProjectWithDocker.entity.record.SoftwareEngineerRecord;
import com.example.softwareProjectWithDocker.exception.EngineerNotFoundException;
import com.example.softwareProjectWithDocker.repository.SERepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SEServiceImp implements SEService{

    private final SERepo seRepo;

    @Autowired
    public SEServiceImp(SERepo seRepo) {
        this.seRepo = seRepo;
    }

    @Override
    public List<SoftwareEngineerRecord> getAllSEs() {
        return seRepo.findAll().stream()
                .map(se -> new SoftwareEngineerRecord(se.getId(),se.getFirst_name(),se.getLast_name(), se.getTech_stack(), se.getGender()))
                .collect(Collectors.toList());
    }

    @Override
    public SoftwareEngineerRecord createSE(SoftwareEngineerRecord se) {
        SoftwareEngineer target = new SoftwareEngineer();

        setDataChanges(se,target);

        SoftwareEngineer responsed_data = seRepo.save(target);
        return se;
    }

    @Override
    public Page<SoftwareEngineerRecord> getAllSEswithPagiantions(int offset, int pageSize) {
        Pageable pageable = PageRequest.of(offset,pageSize);
        return seRepo.findAll(pageable)
                .map(se -> new SoftwareEngineerRecord(se.getId(),se.getFirst_name(),se.getLast_name(), se.getTech_stack(), se.getGender()));
    }

    @Override
    public Page<SoftwareEngineerRecord> getSEsWithPaginationAndSorting(int offset, int pageSize, String sortByField) {
        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(offset,pageSize,sort);
        return seRepo.findAll(pageable).map(se -> new SoftwareEngineerRecord(se.getId(), se.getFirst_name(),se.getLast_name(), se.getTech_stack(),se.getGender()));
    }

    @Override
    public SoftwareEngineerRecord findSeById(Long id) {
        return seRepo.findById(id).stream()
                .map(se -> new SoftwareEngineerRecord(se.getId(), se.getFirst_name(),se.getLast_name(), se.getTech_stack(),se.getGender()))
                .findFirst()
                .orElseThrow(() -> new EngineerNotFoundException("Enginner not trackable!"));
    }

    public void setDataChanges(SoftwareEngineerRecord se, SoftwareEngineer target) {
        target.setFirst_name(se.first_name());
        target.setLast_name(se.last_name());
        target.setTech_stack(se.tech_stack());
        target.setGender(se.gender());
    }

}
