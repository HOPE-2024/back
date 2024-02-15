package com.hopeback.controller.medicine;

//import com.hopeback.entity.medicine.MedicineData;
//import com.hopeback.service.medicine.MedicineService;
import com.hopeback.dto.Medicine.MedicineDataDto;
import com.hopeback.entity.medicine.MedicineData;
import com.hopeback.service.medicine.MedicineDataService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/elastic/medicine")
public class MedicineDataController {
    private final MedicineDataService medicineDataService;

    // 단일 필드 검색
    @GetMapping("/search")
    public ResponseEntity<?> searchKeyword(@RequestParam String keyword) throws Exception {
        return ResponseEntity.ok(medicineDataService.searchKeyword(keyword));
    }

    // 다중 필드 검색
    @GetMapping("/multi-search")
    public ResponseEntity<SearchResponse> searchByKeyword(@RequestParam String keyword, @RequestParam(required = false) String fields, @RequestParam int page) {
        try {
            System.out.println("다중 필드 검색 : "+ keyword + fields);
            List<String> fieldList = fields != null ? Arrays.asList(fields.split(",")) : List.of("effect", "name", "name_en", "ingredient",
                    "ingredient_en", "company", "company_en");
            SearchResponse response = medicineDataService.searchKeyword(keyword, fieldList, page);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

        // 전체 조회
    @GetMapping("/all")
    public ResponseEntity<List<MedicineData>> getAllMedicineData() {
        List<MedicineData> data = medicineDataService.findAll();
        return ResponseEntity.ok(data);
    }

    // Id를 이용하여 검색
    @GetMapping("/search-id")
    public ResponseEntity<SearchResponse> searchById(@RequestParam String documentId) throws IOException {
        return ResponseEntity.ok(medicineDataService.searchById(documentId));
    }

    // 색인
    @PostMapping("/add")
    public ResponseEntity<IndexResponse> addData(@RequestBody MedicineDataDto medicineDataDto) throws IOException {
        System.out.println("medicineDataDtoCon : " + medicineDataDto.getName());
        return ResponseEntity.ok(medicineDataService.addData(medicineDataDto));
    }

    // 삭제 (documentId를 이용)
    @DeleteMapping("/delete")
    public ResponseEntity<DeleteResponse> deleteData(@RequestParam String documentId) throws IOException {
        System.out.println("delete : "+ documentId);
        return ResponseEntity.ok(medicineDataService.deleteData(documentId));
    }

    // 검색어 저장
    @PostMapping("/add-search-log")
    public ResponseEntity<IndexResponse> addSearchLog(@RequestParam String keyword) throws IOException {
        System.out.print("keyword : " + keyword);
        return ResponseEntity.ok(medicineDataService.addSearchLog(keyword));
    }

    // 검색어 빈도 집계
    @GetMapping("/get-search-log")
    public ResponseEntity<List<Map<String, Object>>> getSearchLog() throws IOException {
        return ResponseEntity.ok(medicineDataService.getSearchLog());
    }
}
