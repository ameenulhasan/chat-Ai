package com.ameen.chat_ai.serviceImpl;

import com.ameen.chat_ai.constants.Constant;
import com.ameen.chat_ai.dto.RoleDto;
import com.ameen.chat_ai.dto.UserTokenDto;
import com.ameen.chat_ai.exception.CustomException;
import com.ameen.chat_ai.model.Role;
import com.ameen.chat_ai.repository.RoleRepository;
import com.ameen.chat_ai.response.ApiResponse;
import com.ameen.chat_ai.response.PaginationRequest;
import com.ameen.chat_ai.response.UserContextHolder;
import com.ameen.chat_ai.service.RoleService;
import com.ameen.chat_ai.util.CommonUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseEntity<ApiResponse> saveOrUpdate(RoleDto roleDto) {
        UserTokenDto userDto = UserContextHolder.getUserTokenDto();
        Optional<Role> dup = roleRepository.findByRoleName(roleDto.getRoleName());
        if (dup.isPresent() && (roleDto.getId() == null || !dup.get().getId().equals(roleDto.getId()))) {
            throw new CustomException(Constant.ROLE_ALREADY_EXIST);
        }
        Role role;
        String statusMsg;
        if (roleDto.getId() != null) {
            // UPDATE
            role = roleRepository.findById(roleDto.getId())
                    .orElseThrow(() -> new CustomException(Constant.ROLE_NOT_FOUND));
            role.setRoleName(roleDto.getRoleName());
            role.setIsActive(true);
            role.setDeletedFlag(false);
            role.setModifiedBy(userDto.getId());
            role.setModifiedAt(Timestamp.from(Instant.now()));
            statusMsg = Constant.ROLE_UPDATED;
        } else {
            // CREATE
            role = new Role();
            role.setRoleName(roleDto.getRoleName());
            role.setIsActive(true);
            role.setDeletedFlag(false);
            role.setCreatedBy(userDto.getId());
            role.setCreatedAt(Timestamp.from(Instant.now()));
            statusMsg = Constant.ROLE_CREATED;
        }
        roleRepository.save(role);
        return CommonUtil.getOkResponse(statusMsg);
    }

    @Override
    public ResponseEntity<ApiResponse> getById(Long id) {
        Optional<Role> roleOptional = roleRepository.findByRoleIsActiveTrue(id);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            RoleDto dto = new RoleDto();
            dto.setId(role.getId());
            dto.setRoleName(role.getRoleName());
            return CommonUtil.getOkResponse(Constant.ROLE_ID_FOUND);
        } else {
            throw new CustomException(Constant.ROLE_NOT_FOUND);
        }
    }

    @Override
    public Page<RoleDto> pagesSearch(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNo() - 1, request.getPageSize());
        Page<Role> rolePage;
        if (request.hasSearch()) {
            String keyword = request.getSearch().trim();
            rolePage = roleRepository.findByPageableRoleName(keyword, pageable);
        } else {
            rolePage = roleRepository.findByRoleIsActive(pageable);
        }
        List<RoleDto> roleDtoList = rolePage.getContent().stream().map(role -> {
            RoleDto dto = new RoleDto();
            dto.setId(role.getId());
            dto.setRoleName(role.getRoleName());
            return dto;
        }).toList();
        return new PageImpl<>(roleDtoList);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .filter(Role::getIsActive)
                .orElseThrow(() -> new CustomException(Constant.ROLE_ALREADY_INACTIVE));
        role.setIsActive(false);
        role.setDeletedFlag(true);
        roleRepository.save(role);
        return CommonUtil.getOkResponse(Constant.ROLE_DELETED);
    }

}
