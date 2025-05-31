package com.example.softwareProjectWithDocker.controller;

import com.example.softwareProjectWithDocker.entity.record.SoftwareEngineerRecord;
import com.example.softwareProjectWithDocker.response.ApiResponse;
import com.example.softwareProjectWithDocker.service.SEService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/se")
@Tag(name = "SoftwareEngineer", description = "API For Software Engineers") // swagger open Api
public class SEController {

    private final SEService service;

    private Logger se_log = LoggerFactory.getLogger(SEController.class);

    @Autowired
    public SEController(SEService service) {
        this.service = service;
    }

    @GetMapping(value = {"/","/all"})
    @Operation(summary = "Get All Software Engineers", description = "GET method")
    public ResponseEntity<ApiResponse<List<SoftwareEngineerRecord>>> getSedatas(){
       List<SoftwareEngineerRecord> sedatas = service.getAllSEs();
        se_log.info("SEDATAS: {}", sedatas);
       return ResponseEntity.ok(ApiResponse.success(sedatas, "List of software engineers"));
    }

    @GetMapping(value = "/get-all-by-multiple-entities")
    @Operation(summary = "Get All Software Engineers By Related Entities", description = "GET method")
    public ResponseEntity<ApiResponse<List<SoftwareEngineerRecord>>> getSEsByEntities(@RequestParam(name = "firstName") String firstName,
                                                                                      @RequestParam(name = "lastName")String lastName,
                                                                                      @RequestParam(name = "techStack")String techStack){
        List<SoftwareEngineerRecord> sedatas = service.getSesByMultipleEntities(firstName,lastName,techStack);
        return ResponseEntity.ok(ApiResponse.success(sedatas, "List of software engineers"));
    }

    @PostMapping(value = "/create")
    @Operation(summary = "Create Software Engineer", description = "POST method")
    public ResponseEntity<ApiResponse<SoftwareEngineerRecord>> createSE(@RequestBody SoftwareEngineerRecord se){
      SoftwareEngineerRecord record = service.createSE(se);
      se_log.info("SE is created: {}", record);
        return ResponseEntity.created(URI.create("/create")).body(ApiResponse.success(record, "SE is created flawlessly!"));
    }

    @PutMapping(value = "/update/{id}")
    @Operation(summary = "Update Software Engineer", description = "PUT method")
    public ResponseEntity<ApiResponse<SoftwareEngineerRecord>> updateSE(@RequestBody SoftwareEngineerRecord se, @PathVariable("id") Long id){
        SoftwareEngineerRecord record = service.updateSe(id,se);
        se_log.info("SE is created: {}", record);
        return ResponseEntity.created(URI.create("/update")).body(ApiResponse.success(record, "SE is updated flawlessly!"));
    }

    @GetMapping(value = "/with-pagination")
    @Operation(summary = "Get Software Engineers With Paginations", description = "GET method")
    public ResponseEntity<ApiResponse<Page<SoftwareEngineerRecord>>> getSedatasWithPagination(@RequestParam("offset") int offset,
                                                                                              @RequestParam("size") int pageSize){
        Page<SoftwareEngineerRecord> sedatas = service.getAllSEswithPagiantions(offset,pageSize);
        return ResponseEntity.ok(ApiResponse.success(sedatas, "List of software engineers with pagination"));
    }

    @GetMapping(value = "/with-pagination-and-sorting")
    @Operation(summary = "Get Software Engineers With Pagination And Sorting", description = "GET method")
    public ResponseEntity<ApiResponse<Page<SoftwareEngineerRecord>>> getSedatasWithPagination(@RequestParam(value = "offset") int offset,
                                                                                              @RequestParam("size") int pageSize,
                                                                                              @RequestParam("sortBy") String sortByField) {
        se_log.info("sort by {} ", sortByField);

        Page<SoftwareEngineerRecord> sedatas = service.getSEsWithPaginationAndSorting(offset, pageSize, sortByField);

        return ResponseEntity.ok(ApiResponse.success(sedatas, "List of software engineers with pagination"));
    }

//    @GetMapping(value = "/get-file")
//    public ResponseEntity<ApiResponse<String>> getSesMultipartDataFiles(@RequestParam("file")MultipartFile file, @RequestPart("name") String name){
//        return ResponseEntity.ok(ApiResponse.success(null, "File Uploaded!"));
//    }

    @GetMapping(value = "/{seId}")
    @Operation(summary = "Get Software Engineer By Id", description = "GET method")
    public ResponseEntity<ApiResponse<SoftwareEngineerRecord>> getSesById(@PathVariable("seId") Long id){
        SoftwareEngineerRecord se = service.findSeById(id);
        return ResponseEntity.ok(ApiResponse.success(se, "Tracked Engineer"));
    }



}
