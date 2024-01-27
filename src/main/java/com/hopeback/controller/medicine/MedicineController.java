package com.hopeback.controller.medicine;

import com.hopeback.entity.medicine.MedicineData;
import com.hopeback.service.medicine.MedicineService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/elastic/medicine")
public class MedicineController {
    private final MedicineService medicineService;

    // 단일 필드 검색
    @GetMapping("/search")
    public ResponseEntity<?> searchKeyword(@RequestParam String keyword) throws Exception {
        return ResponseEntity.ok(medicineService.searchKeyword(keyword));
    }

    // 다중 필드 검색
    @GetMapping("/multi-search")
    public ResponseEntity<SearchResponse> searchByKeyword(@RequestParam String keyword, @RequestParam(required = false) String fields) {
        try {
            List<String> fieldList = fields != null ? Arrays.asList(fields.split(",")) : List.of("effect");
            SearchResponse response = medicineService.searchKeyword(keyword, fieldList);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

        // 전체 조회
    @GetMapping("/all")
    public ResponseEntity<List<MedicineData>> getAllMedicineData() {
        List<MedicineData> data = medicineService.findAll();
        return ResponseEntity.ok(data);
    }
}
