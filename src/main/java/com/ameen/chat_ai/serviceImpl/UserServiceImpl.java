package com.ameen.chat_ai.serviceImpl;

import com.ameen.chat_ai.constants.Constant;
import com.ameen.chat_ai.dto.*;
import com.ameen.chat_ai.exception.CustomException;
import com.ameen.chat_ai.model.Role;
import com.ameen.chat_ai.model.User;
import com.ameen.chat_ai.model.UserRoleMapping;
import com.ameen.chat_ai.repository.RoleRepository;
import com.ameen.chat_ai.repository.UserRepository;
import com.ameen.chat_ai.repository.UserRoleMappingRepository;
import com.ameen.chat_ai.response.ApiResponse;
import com.ameen.chat_ai.response.UserContextHolder;
import com.ameen.chat_ai.service.EmailService;
import com.ameen.chat_ai.service.UserService;
import com.ameen.chat_ai.util.CommonUtil;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "User not found";
    private static final String USER_EMAIL_NOT_FOUND = "User email Id not found";
    private static final String OLD_PASSWORD_IS_INCORRECT = "old password is incorrect";
    private static final String RESET_PASSWORD_OTP = "Reset Password OTP";
    private static final String OTP_IS = "OTP is: ";
    private static final Random RANDOM = new Random();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleMappingRepository userRoleMappingRepository;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                           UserRoleMappingRepository userRoleMappingRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRoleMappingRepository = userRoleMappingRepository;
        this.emailService = emailService;
    }

    @Override
    public ResponseEntity<ApiResponse> createUser(UserDto userDto) {
        User user = null;
        if (userDto.getId() != null) {
            user = userRepository.findById(userDto.getId())
                    .orElseThrow(() -> new CustomException(Constant.USER_NOT_FOUND));
        }
        Optional<User> userOptional = userRepository.findByEmailId(userDto.getEmailId());
        if (userOptional.isPresent() && !userOptional.get().getId().equals(userDto.getId())) {
            throw new CustomException(Constant.USER_EMAIL_ALREADY_EXIST);
        }
        if (user == null) {
            user = new User();
        }
        user.setUserName(userDto.getUserName());
        user.setEmailId(userDto.getEmailId());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAddress(userDto.getAddress());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setGender(userDto.getGender());
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setIsActive(true);
        user.setDeletedFlag(false);
        User savedUser = userRepository.save(user);
        Optional<Role> roleOptional = roleRepository.findByRoleUserIsActive("User");
        roleOptional.ifPresent(role -> {
            UserRoleMapping mapping = new UserRoleMapping();
            mapping.setUser(savedUser);
            mapping.setRole(role);
            mapping.setIsActive(true);
            mapping.setDeletedFlag(false);
            userRoleMappingRepository.save(mapping);
        });
        return CommonUtil.getOkResponse(Constant.USER_CREATED);
    }
    public User userLogin(LoginDto loginDto) {
        User user;
        Optional<User> userOptional=userRepository.findByEmailId(loginDto.getEmail());
        if (userOptional.isPresent()){
            if (passwordEncoder.matches(loginDto.getPassword(), userOptional.get().getPassword())){
                user=userOptional.get();
            }else {
                throw new CustomException(Constant.INVALID_PASSWORD);
            }
        }else {
            throw new CustomException(Constant.INVALID_EMAIL);
        }
        return user;
    }

    @Override
    public ResponseEntity<ApiResponse> getUserId(Long id) {
        User user = userRepository.findByIdIsActive(id)
                .orElseThrow(() -> new CustomException(Constant.USER_NOT_FOUND));
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setGender(user.getGender());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setEmailId(user.getEmailId());
        userDto.setDateOfBirth(user.getDateOfBirth());
        userDto.setAddress(user.getAddress());
        return CommonUtil.getOkResponse(userDto);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteId(Long id) {
        User user = userRepository.findByIdIsActive(id)
                .orElseThrow(() -> new CustomException(Constant.USER_NOT_FOUND));
        user.setIsActive(false);
        user.setDeletedFlag(true);
        userRepository.save(user);
        return CommonUtil.getOkResponse(Constant.USER_DELETED);
    }

    @Override
    public void changesPassword(PasswordChange passwordChange) {
        UserTokenDto userDto = UserContextHolder.getUserTokenDto();
        User user = userRepository.findByIdIsActive(userDto.getId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if (!passwordEncoder.matches(passwordChange.getOldPassword(),user.getPassword())){
            throw new CustomException(OLD_PASSWORD_IS_INCORRECT);
        }
        user.setPassword(passwordEncoder.encode(passwordChange.getNewPassWord()));
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(ForgotPassword request) throws MessagingException {
        User user = userRepository.findByEmailId(request.getEmail())
                .orElseThrow(() -> new CustomException(USER_EMAIL_NOT_FOUND));

        String otp = String.valueOf(RANDOM.nextInt(900000) + 100000);
        user.setOtp(otp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(3));
        userRepository.save(user);
        emailService.sendOtpMail(user.getEmailId(), user.getUserName(), otp);
    }

    @Override
    public void verifyOtp(VerifyOtpRequest req) {
        User user = userRepository.findByEmailId(req.getEmail())
                .orElseThrow(() -> new CustomException(USER_EMAIL_NOT_FOUND));
        if (!req.getOtp().equals(user.getOtp())) {
            throw new CustomException(Constant.OTP_INCORRECT);
        }
        if (user.getOtpExpiryTime() == null || user.getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            throw new CustomException(Constant.OTP_EXPIRED);
        }
        user.setOtp(null);
        user.setOtpExpiryTime(null);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(UpdatePassword request) {
        User user = userRepository.findByEmailId(request.getEmail())
                .orElseThrow(() -> new CustomException(USER_EMAIL_NOT_FOUND));
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

}
