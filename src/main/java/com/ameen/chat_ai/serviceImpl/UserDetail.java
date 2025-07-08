package com.ameen.chat_ai.serviceImpl;

import com.ameen.chat_ai.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ameen.chat_ai.model.User;
import java.util.ArrayList;

@Service
public class UserDetail implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetail(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByMail(userEmail);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with userEmail: " + userEmail);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmailId(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}