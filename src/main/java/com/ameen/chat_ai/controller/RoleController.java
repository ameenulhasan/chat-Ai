package com.ameen.chat_ai.controller;

import com.ameen.chat_ai.dto.RoleDto;
import com.ameen.chat_ai.response.ApiResponse;
import com.ameen.chat_ai.response.PaginationRequest;
import com.ameen.chat_ai.service.RoleService;
import com.ameen.chat_ai.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> saveOrUpdate(@RequestBody RoleDto roleDto) {
        return roleService.saveOrUpdate(roleDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return roleService.getById(id);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> listRoles(HttpServletRequest request) {
        PaginationRequest paginationRequest = PaginationRequest.getObject(request);
        Page<RoleDto> pageResponse = roleService.pagesSearch(paginationRequest);
        return CommonUtil.getOkResponse(pageResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }
}
