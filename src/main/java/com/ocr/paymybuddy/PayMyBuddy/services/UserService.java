package com.ocr.paymybuddy.PayMyBuddy.services;

import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.models.UserConnection;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserConnectionRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final UserConnectionRepository userConnectionRepository;

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUsersByEmail(email);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void addUserConnection(String email, String connectedUserEmail) {
        Optional<User> optionalUser = userRepository.findUsersByEmail(email);
        if (optionalUser.isPresent()) {
            User connectedUser = userRepository.findUsersByEmail(connectedUserEmail)
                    .orElseThrow(() -> new NullPointerException("User not found"));
            UserConnection userConnection = new UserConnection();
            userConnection.setUsername(optionalUser.get().getUsername());
            userConnection.setEmail(optionalUser.get().getEmail());
            userConnection = userConnectionRepository.save(userConnection);
            connectedUser.getConnections().add(userConnection);
            userRepository.save(connectedUser);
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("user not found with username " + username);
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
    }
}
