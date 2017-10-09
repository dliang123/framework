package com.yuntu.web.core.dao;


import com.yuntu.jpa.extend.JpaSpecificationExecutorExt;
import com.yuntu.web.core.entity.MarketingActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


/**
 * 营销活动定义
 * Created by jackdeng on 17/8/4.
 */
@Repository
public interface MarketingActivityDao extends JpaRepository<MarketingActivityEntity, Long>, JpaSpecificationExecutorExt<MarketingActivityEntity> {

    @Query(value = "select a.* from marketing_activity_define a where start_time <= ?1 and end_time >=?1 order by created_date desc", nativeQuery = true)
//    @Query(value = "select a.* from marketing_activity_define a " , nativeQuery = true)
    List<MarketingActivityEntity> findActivities(Date date);
}
