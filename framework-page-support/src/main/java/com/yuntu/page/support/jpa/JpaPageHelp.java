package com.yuntu.page.support.jpa;

import com.yuntu.page.PageCustom;
import com.yuntu.page.PageRequestCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.Collections;
import java.util.List;

/**
 *  16/3/17.
 */
public class JpaPageHelp {

    public static Pageable convert(PageRequestCustom pageRequestCustom){

        return new PageRequest(Math.max(pageRequestCustom.getPageIndex()-1,0), pageRequestCustom.getPageSize());
    }

    public static Pageable convert(PageRequestCustom pageRequestCustom, Sort sort){

        return new PageRequest(Math.max(pageRequestCustom.getPageIndex()-1,0), pageRequestCustom.getPageSize(),sort);
    }

    public static PageCustom emptyPageCustom(PageRequestCustom pageRequestCustom){

        return new PageCustom(0,pageRequestCustom, Collections.emptyList());
    }

    public static <T> PageCustom<T> convert(Long totalElements, PageRequestCustom pageRequestCustom, List<T> content){

        return new PageCustom<>(totalElements, pageRequestCustom,content);
    }

    public static <T> PageCustom<T> convert(Page<T> page, PageRequestCustom pageRequestCustom){

        return convert(page.getTotalElements(), pageRequestCustom,page.getContent());
    }
}
