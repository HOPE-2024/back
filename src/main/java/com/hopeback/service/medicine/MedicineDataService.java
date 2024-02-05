package com.hopeback.service.medicine;

import com.hopeback.entity.medicine.MedicineData;
import com.hopeback.repository.medicine.MedicineDataRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineDataService {
    private final RestHighLevelClient client;
    private final MedicineDataRepository medicineDataRepository;

    // 단일 필드 검색
    public SearchResponse searchKeyword(String keyword) throws IOException {
        SearchRequest searchRequest = new SearchRequest("medicine");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("effect", keyword)); // effect 필드에 keyword가 포함된 문서를 검색
        searchRequest.source(searchSourceBuilder);
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    // 다중 필드 검색
    public SearchResponse searchKeyword(String keyword, List<String> fields, int page) throws IOException {
        SearchRequest searchRequest = new SearchRequest("medicine");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // Query 설정
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, fields.toArray(new String[0])));
        // Pagination 설정
        searchSourceBuilder.from((page - 1) * 10);
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    // 전체 조회
    public List<MedicineData> findAll() {
        List<MedicineData> dataList = new ArrayList<>();
        medicineDataRepository.findAll().forEach(dataList::add);
        return dataList;
    }

    // ID를 이용하여 검색하기
    public SearchResponse searchById(String documentId) throws  IOException {
        SearchRequest searchRequest = new SearchRequest("medicine");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // term 쿼리를 사용하여 _id 필드에 documentId가 일치하는 문서를 검색
        searchSourceBuilder.query(QueryBuilders.termQuery("_id", documentId));
        searchRequest.source(searchSourceBuilder);
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }
}
