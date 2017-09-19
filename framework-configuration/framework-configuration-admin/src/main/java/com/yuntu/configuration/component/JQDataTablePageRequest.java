package com.yuntu.configuration.component;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * Created by linxiaohui on 16/1/15.
 */
public class JQDataTablePageRequest implements Pageable {

    private int start;
    private int length;
    private List<ColumnMap> columns = Lists.newArrayList();
    private List<Map<?,?>> order = Lists.newArrayList();
    private Map<String,Object> search = Maps.newHashMapWithExpectedSize(2);

    public int getPageNumber() {
        return 0;
    }

    public int getPageSize() {
        return length;
    }

    public int getOffset() {
        return start;
    }

    public Sort getSort() {
        if( null == order || null == columns)
            return null;
        List<Sort.Order> orders = Lists.newArrayList();
        for(Map<?,?> _order : order){
            Integer column = Integer.valueOf( ""+_order.get("column") );
            if( columns.size()> column ){
                Object name = columns.get(column).get("data");
                if( null != name && Objects.equal("true",columns.get(column).get("orderable"))){
                    orders.add(new  Sort.Order( Sort.Direction.valueOf( ((String)_order.get("dir")).toUpperCase() ),String.valueOf(name)));
                }
            }
        }
        if( orders.isEmpty() )
            return null;
        return new Sort(orders);
    }

    public Pageable next() {
        return null;
    }

    public Pageable previousOrFirst() {
        return null;
    }

    public Pageable first() {
        return null;
    }

    public boolean hasPrevious() {
        return false;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<ColumnMap> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMap> columns) {
        this.columns = columns;
    }



    public Map<String, Object> getSearch() {
        return search;
    }

    public void setSearch(Map<String, Object> search) {
        this.search = search;
    }

    public List<Map<?,?>> getOrder() {
        return order;
    }

    public void setOrder(List<Map<?,?>> order) {
        this.order = order;
    }

    public String searchValue(){
       Object value = search.get("value");
        if( null == value )
            return null;
       return String.valueOf(value);
    }




}
