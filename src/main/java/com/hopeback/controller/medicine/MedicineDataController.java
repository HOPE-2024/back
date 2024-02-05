package com.hopeback.controller.medicine;

//import com.hopeback.entity.medicine.MedicineData;
//import com.hopeback.service.medicine.MedicineService;
import com.hopeback.entity.medicine.MedicineData;
import com.hopeback.service.medicine.MedicineDataService;
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

    @GetMapping("/searchId")
    public ResponseEntity<SearchResponse> searchById(@RequestParam String documentId) throws IOException {
        return ResponseEntity.ok(medicineDataService.searchById(documentId));
    }
}
