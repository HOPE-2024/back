//package com.hopeback.service.medicine;
//
//import com.hopeback.entity.medicine.MedicineData;
//import com.hopeback.repository.medicine.MedicineRepository;
//import lombok.RequiredArgsConstructor;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.springframework.stereotype.Service;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class MedicineService {
//    private final RestHighLevelClient client;
//    private final MedicineRepository medicineRepository;
//
//    // 단일 필드 검색
//    public SearchResponse searchKeyword(String keyword) throws IOException {
//        SearchRequest searchRequest = new SearchRequest("medicine");
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchQuery("effect", keyword)); // effect 필드에 keyword가 포함된 문서를 검색
//        searchRequest.source(searchSourceBuilder);
//        return client.search(searchRequest, RequestOptions.DEFAULT);
//    }
//
//    // 다중 필드 검색
//    public SearchResponse searchKeyword(String keyword, List<String> fields) throws IOException {
//        SearchRequest searchRequest = new SearchRequest("medicine");
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, fields.toArray(new String[0])));
//        searchRequest.source(searchSourceBuilder);
//        return client.search(searchRequest, RequestOptions.DEFAULT);
//    }
//
//    // 전체 조회
//    public List<MedicineData> findAll() {
//        List<MedicineData> dataList = new ArrayList<>();
//        medicineRepository.findAll().forEach(dataList::add);
//        return dataList;
//    }
//}
