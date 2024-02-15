package com.hopeback.service.medicine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopeback.dto.Medicine.MedicineDataDto;
import com.hopeback.entity.medicine.MedicineData;
import com.hopeback.repository.medicine.MedicineDataRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        /* fields.toArray(new String[0]) : 리스트 fields를 배열로 변환하는 것.
        multiMatchQuery가 배열을 기대하기 때문에 리스트를 배열로 변환하여야 함.
        new String[0] : 배열을 생성할 때마다 크기가 0인 문자열을 생성함. */
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, fields.toArray(new String[0])));
        // 만약 filter가 이름(가나다) 순으로 정렬이라면 수행
//        if(filter.equals("name")) {
//            // 이름(가나다) 순으로 정렬
//            /* name.keyword : es에서 문자열은 토큰으로 나누어져 저장됨. 만약 이름순으로 정렬하게 되면 토큰으로 분리된 문자들이 아닌,
//            분리되기 전 문자열을 정렬해야 하기 때문에 정렬 조건을 지정해주는 것임. */
//            searchSourceBuilder.sort(new FieldSortBuilder("name").order(SortOrder.ASC));
//        }
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

    // ID를 이용하여 검색
    public SearchResponse searchById(String documentId) throws  IOException {
        SearchRequest searchRequest = new SearchRequest("medicine");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // term 쿼리를 사용하여 _id 필드에 documentId가 일치하는 문서를 검색
        searchSourceBuilder.query(QueryBuilders.termQuery("_id", documentId));
        searchRequest.source(searchSourceBuilder);
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    // 색인
    public IndexResponse addData(MedicineDataDto medicineDataDto) throws  IOException {
        System.out.println("medicineDataDto : " + medicineDataDto.getName());
        String jsonData = convertToJson(medicineDataDto);
        System.out.println("medicineDataDtoJson : " + jsonData);
        IndexRequest indexRequest = new IndexRequest("medicine")
                .source(jsonData, XContentType.JSON);
        return client.index(indexRequest, RequestOptions.DEFAULT);
    }

    // 삭제 (documentId를 이용)
    public DeleteResponse deleteData(String documentId) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("medicine", documentId);
    return client.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    // 업데이트

    // 검색 로그 저장
    public IndexResponse addSearchLog(String keyword) throws IOException {
        // JSON 형식으로 저장하기 위해 HashMap 사용
        Map<String, Object> logData = new HashMap<>();
        logData.put("search_keyword", keyword); // 검색어
        logData.put("timestamp", Instant.now().toEpochMilli()); // 현재 시간
        IndexRequest indexRequest = new IndexRequest("search_logs") // 인덱싱
                .source(logData, XContentType.JSON);
        return client.index(indexRequest, RequestOptions.DEFAULT);
    }

    // 검색어 빈도 집계
    public List<Map<String, Object>> getSearchLog() throws IOException {
        SearchRequest searchRequest = new SearchRequest("search_logs"); // 인덱스에 대한 검색을 요청하는 객체 생성
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); // 검색 요청의 세부적인 내용을 설정하는 빌더 객체 생성

        // 어그리게이션(집계) 설정
        TermsAggregationBuilder aggregation = AggregationBuilders // 용어(term) 어그리게이션 생성 : 특정 필드의 값들을 기준으로 빈도를 집계
                .terms("top_search_terms") // 어그리게이션의 이름으로 사용되며, 나중에 결과에서 이 이름으로 어그리게이션 결과 참조 가능
                .field("search_keyword.keyword") // 해당 필드 기준으로 집계 수행 .keyword는 검색어를 정확히 일치하는 값으로 집계하도록 함
                .size(10); // 상위 10개 검색어만 가져오도록 설정
        searchSourceBuilder.aggregation(aggregation); // search 요청에 어그리게이션 적용
        searchRequest.source(searchSourceBuilder); // search 요청 수행 및 결과 처리
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT); // es에 검색을 요청하고 searchResponse로 결과 받음

        // 어그리게이션 결과 처리 (프론트에서 출력된 값 보고 수정해야 함)
        Terms termsAggregation = searchResponse.getAggregations().get("top_search_terms"); // 집계 결과를 가져옴

        // termsAggregation.getBuckets()로 어그리게이션 결과의 버킷들을 가져오고, getKeyAsString()을 사용하여 각 버킷의 키(검색어)를 가져와 List<String>로 변환
        List<Map<String, Object>> topSearchTerms = termsAggregation.getBuckets().stream()
                .map(bucket -> {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("key", bucket.getKeyAsString());
                    entry.put("doc_count", bucket.getDocCount());
                    return entry;
                })
                .collect(Collectors.toList());
        return topSearchTerms;
    }



    // Dto -> JSON 문자열로 변환
    public static String convertToJson(MedicineDataDto medicineDataDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Java 객체를 JSON 문자열로 변환
            return objectMapper.writeValueAsString(medicineDataDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // 예외 처리: 변환 실패 시 빈 문자열 또는 다른 기본값 반환
            return "";
        }
    }
}
