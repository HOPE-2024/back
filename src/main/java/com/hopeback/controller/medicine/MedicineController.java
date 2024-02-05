package com.hopeback.controller.medicine;

import com.hopeback.dto.Medicine.MedicineDto;
import com.hopeback.service.medicine.MedicineDataService;
import com.hopeback.service.medicine.MedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/medicine")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineDataService medicineDataService;
    private final MedicineService medicineService;

    // es의 모든 id와 name 값 mysql에 적재
    @GetMapping("/all-transfer")
    public ResponseEntity<List<MedicineDto>> transferDataFromEsToMySQL() {
        List<MedicineDto> data = new ArrayList<>();
        try {
            data = medicineService.transferDataFromEsToMySQL_ver2();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(data);
    }
}
