package com.hopeback.service.medicine;

import com.hopeback.dto.Medicine.MedicineDto;
import com.hopeback.entity.medicine.Medicine;
import com.hopeback.repository.medicine.MedicineDataRepository;
import com.hopeback.repository.medicine.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final MedicineDataRepository medicineDataRepository;
    private final RestHighLevelClient client;  // Elasticsearch의 RestHighLevelClient를 주입 받아야 합니다.



    // es에서 "medicine" 인덱스의 모든 문서의 id와 name 값을 가져오는 쿼리.
    // 10000개의 데이터만 불러올 수 있어 테스트 실패
    public List<MedicineDto> transferDataFromEsToMySQL_ver1() {

        List<MedicineDto> dataList = new ArrayList<>();
        medicineDataRepository.findAll().forEach(medicine -> {
            MedicineDto dto = new MedicineDto(medicine.getId(),medicine.getName());
            dataList.add(dto);
        });
        return dataList;

    }


    // 위와 같은 기능을 수행
    // 10000개 이상의 데이터를 받아오기 위해 나누어서 가져오는 코드 추가
    // all shard failed 오류가 떠서 테스트 실패. 우선 인덱스를 지워서 매핑 다시 설정해야 할 듯
    public List<MedicineDto> transferDataFromEsToMySQL_ver2() throws IOException {
        List<MedicineDto> dataList = new ArrayList<>();

        // 처음 검색 요청
        SearchRequest searchRequest = new SearchRequest("medicine");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.matchAllQuery())
                .sort(SortBuilders.fieldSort("id").order(SortOrder.ASC))  // id로 정렬
                .size(1000);  // 한 페이지에 가져올 문서 수

        searchRequest.source(searchSourceBuilder);

        // Elasticsearch에 검색 요청 및 결과 받기
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        // 페이지 반복
        while (true) {
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits.getHits()) {
                String documentId = hit.getId();
                // Elasticsearch 문서에서 필요한 데이터 추출
                Map<String, Object> source = hit.getSourceAsMap();
                String name = (String) source.get("name");

                // MySQL로 데이터 전송
                MedicineDto dto = new MedicineDto(documentId, name);
                dataList.add(dto);
            }

            // 다음 페이지 가져오기
            if (hits.getHits().length > 0) {
                SearchHit lastHit = hits.getHits()[hits.getHits().length - 1];
                Object[] searchAfterValues = lastHit.getSortValues();

                searchSourceBuilder.searchAfter(searchAfterValues);
                searchRequest.source(searchSourceBuilder);

                response = client.search(searchRequest, RequestOptions.DEFAULT);
            } else {
                // 더 이상 데이터가 없으면 반복 종료
                break;
            }
        }

        // MySQL에 데이터 저장 (예시: save 메소드가 있다고 가정)
        for (MedicineDto dto : dataList) {
            Medicine medicine = new Medicine();
            medicine.setDocumentId(dto.getDocumentId());
            medicine.setName(dto.getName());
            medicineRepository.save(medicine);
        }

        return dataList;
    }
}
