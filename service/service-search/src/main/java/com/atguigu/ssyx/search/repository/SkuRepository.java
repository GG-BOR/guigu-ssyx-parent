package com.atguigu.ssyx.search.repository;

import com.atguigu.ssyx.model.search.SkuEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SkuRepository extends ElasticsearchRepository<SkuEs,Long> {


    Page<SkuEs> findByOrderByHotScoreDesc(Pageable pageable);

    Page<SkuEs> findByCategoryIdAndWareId(Long wareId, Long categoryId, Pageable pageable);


    Page<SkuEs> findByKeywordAndWareId(String keyword, Long wareId, Pageable pageable);
}
