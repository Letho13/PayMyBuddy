package com.ocr.paymybuddy.PayMyBuddy.services;


import com.ocr.paymybuddy.PayMyBuddy.models.BankAccount;
import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.models.UserConnection;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserConnectionRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.UserRegistrationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConnectionRepository userConnectionRepository;

    @Override
    @Transactional
    public User save(UserRegistrationDto registrationDto) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(BigDecimal.valueOf(0));
        bankAccount.setAccountNumber(UUID.randomUUID().toString());

        User savedUser = User.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .bankAccount(bankAccount)
                .build();

        return userRepository.save(savedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        return new org.springframework.security.core.userdetails.User(optionalUser.get().getEmail(), optionalUser.get().getPassword(),
                new HashSet<GrantedAuthority>());
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void addUserConnection(String email, String connectedUserEmail) {
        Optional<User> targetedUser = userRepository.findUserByEmail(email);

        if (targetedUser.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur avec cet e-mail introuvable.");
        }
        User connectedUser = userRepository.findUserByEmail(connectedUserEmail)
                .orElseThrow(() -> new NullPointerException("Utilisateur connecté non trouvé"));


        UserConnection userConnection = new UserConnection();
        userConnection.setFromUser(connectedUser);
        userConnection.setToTargeted(targetedUser.get());

        userConnectionRepository.save(userConnection);
        log.info("Connexion ajoutée entre {} et {}", connectedUserEmail, email);
    }

    public void updateUser(String username, String email, String password) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();

        User user = userRepository.findUserByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(username);
        user.setEmail(email);

        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);
    }
}
