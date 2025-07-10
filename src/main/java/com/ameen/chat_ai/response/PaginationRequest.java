package com.ameen.chat_ai.response;

import com.ameen.chat_ai.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Setter
@Getter
public class PaginationRequest {

    private int pageNo ;
    private int pageSize ;
    private Map<String, String> filters;
    private boolean hasFilters;
    private boolean hasSearch;
    private String sortField;
    private String sortValue;
    private boolean hasSort;
    private String search;

    private static final List<String> exclude = Arrays.asList("pageNo", "pageSize", "sort", "search");

    public static PaginationRequest getObject(HttpServletRequest httpServletRequest){
        PaginationRequest paginationRequest = new PaginationRequest();
        setPageValues(paginationRequest,httpServletRequest);
        setFilters(paginationRequest,httpServletRequest);
        setSearch(paginationRequest,httpServletRequest);
        setSort(paginationRequest,httpServletRequest);
        return paginationRequest;
    }

    private static void setPageValues(PaginationRequest paginationRequest, HttpServletRequest httpServletRequest){
        try{
            int pno = Integer.parseInt(Optional.ofNullable(httpServletRequest.getParameter("pageNo")).orElse("1"));
            if(pno <= 0){
                throw new CustomException("Page value must be greater than 0");
            }
            paginationRequest.pageNo = pno;
            int psize = Integer.parseInt(Optional.ofNullable(httpServletRequest.getParameter("pageSize")).orElse("10"));
            if(psize <= 0){
                throw new CustomException("Page value must be greater than 0");
            }
            paginationRequest.pageSize = psize;
        }catch (NumberFormatException numberFormatException){
            throw new CustomException("Page value must be in number");
        }
    }

    private static void setFilters(PaginationRequest paginationRequest, HttpServletRequest httpServletRequest){
        Map<String, String> filterMap = new HashMap<>();
        var keys = httpServletRequest.getParameterNames();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            String value = httpServletRequest.getParameter(key);
            if(!exclude.contains(key) && Objects.nonNull(value) && !value.isBlank()){
                filterMap.put(key,httpServletRequest.getParameter(key));
            }
        }
        paginationRequest.filters = filterMap;
        paginationRequest.hasFilters = !filterMap.isEmpty();
    }

    private static void setSearch(PaginationRequest paginationRequest, HttpServletRequest httpServletRequest) {
        String searchParam = httpServletRequest.getParameter("search");
        if (Objects.nonNull(searchParam) && !searchParam.isBlank()) {
            paginationRequest.search = searchParam.trim();
            paginationRequest.hasSearch = true;
        } else {
            paginationRequest.hasSearch = false;
        }
    }

    private static void setSort(PaginationRequest paginationRequest, HttpServletRequest httpServletRequest){
        String[] values = httpServletRequest.getParameterValues("sort");
        if(Objects.nonNull(values) && values.length == 2 && Objects.nonNull(values[0]) && !values[0].isBlank()) {
            paginationRequest.sortField = values[0];
            paginationRequest.sortValue = values[1];
            paginationRequest.hasSort = true;
        }else{
            paginationRequest.hasSort = false;
        }
    }
    public boolean hasSearch() {
        return hasSearch;
    }

    public boolean hasFilters() {
        return hasFilters;
    }

    public boolean hasSort() {
        return hasSort;
    }
}
