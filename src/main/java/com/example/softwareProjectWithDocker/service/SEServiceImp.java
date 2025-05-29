package com.example.softwareProjectWithDocker.service;

import com.example.softwareProjectWithDocker.entity.SoftwareEngineer;
import com.example.softwareProjectWithDocker.entity.record.SoftwareEngineerRecord;
import com.example.softwareProjectWithDocker.exception.EngineerNotFoundException;
import com.example.softwareProjectWithDocker.repository.SERepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SEServiceImp implements SEService{

    private final SERepo seRepo;

    @Autowired
    public SEServiceImp(SERepo seRepo) {
        this.seRepo = seRepo;
    }

    @Override
    @Cacheable(value = "allSoftwareEngineers")
    public List<SoftwareEngineerRecord> getAllSEs() {

//        String key = all_se_cache_keys;
//
//       Object cached = redisTemplate.opsForValue().get(all_se_cache_keys);

//        if(cached != null && cached instanceof List){
//            return (List<SoftwareEngineerRecord>)cached;
//        }

        // if there is already cached datas in server side, then we just return excepted values from cached

        return seRepo.findAll().stream()
                .map(se -> new SoftwareEngineerRecord(se.getId(),se.getFirst_name(),se.getLast_name(), se.getTech_stack(), se.getGender()))
                .collect(Collectors.toList());

        // if no values from cached, then we add those values into redis cache
//        redisTemplate.opsForValue().set(key,softwareEngineerRecords, Duration.ofMinutes(60));

    }

    @Override
    @CacheEvict(value = {"allSoftwareEngineers",
            "softwareEngineerWithPagination",
            "softwareEngineerWithPaginationAndSorting",
            "softwareEngineerById"}, key = "#se.id", allEntries = true)
    public SoftwareEngineerRecord createSE(SoftwareEngineerRecord se) {
//       String key =  all_se_cache_keys + se.id();
//
//        SoftwareEngineerRecord cached = (SoftwareEngineerRecord) redisTemplate.opsForValue().get(key);
//
//        if(cached != null){
//            return cached;
//        }

        SoftwareEngineer target = new SoftwareEngineer();

        setDataChanges(se,target);

        SoftwareEngineer responsed_data = seRepo.save(target);

//        redisTemplate.opsForValue().set(key,responsed_data, Duration.ofMinutes(60));

        return se;
    }

    @Override
    @CacheEvict(value = {"allSoftwareEngineers",
                        "softwareEngineerWithPagination",
                        "softwareEngineerWithPaginationAndSorting",
                         "softwareEngineerById"}, key = "#id", allEntries = true)
    public SoftwareEngineerRecord updateSe(Long id, SoftwareEngineerRecord seRecord) {

      SoftwareEngineer foundedEngineer = seRepo.findAll().stream()
                .filter(se -> se.getId().equals(id))
                .findFirst()
               .orElseThrow(() -> new EngineerNotFoundException("Enginner not trackable!"));

      setDataChanges(seRecord,foundedEngineer);

      SoftwareEngineer softwareEngineer = seRepo.save(foundedEngineer);
      return new SoftwareEngineerRecord(softwareEngineer.getId(),softwareEngineer.getFirst_name(),softwareEngineer.getLast_name(), softwareEngineer.getTech_stack(), softwareEngineer.getGender());
    }

    @Override
    @Cacheable(value = "softwareEngineerWithPagination", key = "#offset + #pageSize")
    public Page<SoftwareEngineerRecord> getAllSEswithPagiantions(int offset, int pageSize) {
//        String key = all_se_cache_keys + "#offset + #pageSize";
//        Object cached = redisTemplate.opsForValue().get(key);
//
//        if(cached != null ){
//            return (Page<SoftwareEngineerRecord>) cached;
//        }

        Pageable pageable = PageRequest.of(offset,pageSize);
        return seRepo.findAll(pageable)
                .map(se -> new SoftwareEngineerRecord(se.getId(),se.getFirst_name(),se.getLast_name(), se.getTech_stack(), se.getGender()));
//        redisTemplate.opsForValue().set(key,page, Duration.ofMinutes(60));
    }

    @Override
    @Cacheable(value = "softwareEngineerWithPaginationAndSorting", key = "#offset + #pageSize + #sortByField")
    public Page<SoftwareEngineerRecord> getSEsWithPaginationAndSorting(int offset, int pageSize, String sortByField) {
        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(offset,pageSize,sort);
        return seRepo.findAll(pageable).map(se -> new SoftwareEngineerRecord(se.getId(), se.getFirst_name(),se.getLast_name(), se.getTech_stack(),se.getGender()));
    }

    @Override
    @Cacheable(value = "softwareEngineerById",key = "#id")
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
