package com.hopeback.repository.medicine;

import com.hopeback.entity.medicine.MedicineData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MedicineDataRepository extends ElasticsearchRepository<MedicineData, String> {
}
