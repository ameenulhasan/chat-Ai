package com.ameen.chat_ai.service;

import com.ameen.chat_ai.dto.RoleDto;
import com.ameen.chat_ai.response.ApiResponse;
import com.ameen.chat_ai.response.PaginationRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface RoleService {

    ResponseEntity<ApiResponse> saveOrUpdate(RoleDto roleDto);

    ResponseEntity<ApiResponse> getById(Long id);

    Page<RoleDto> pagesSearch(PaginationRequest request);

    ResponseEntity<ApiResponse> deleteRole(Long id);

}
