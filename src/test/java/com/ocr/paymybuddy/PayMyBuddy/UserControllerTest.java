package com.ocr.paymybuddy.PayMyBuddy;

import com.ocr.paymybuddy.PayMyBuddy.controllers.UserController;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.services.UserServiceImplementation;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.AddRelationDto;
import com.ocr.paymybuddy.PayMyBuddy.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserServiceImplementation userServiceImplementation;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void testProfil() {
        org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User("test@example.com", "password", new ArrayList<>());
        when(authentication.getPrincipal()).thenReturn(securityUser);
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        String viewName = userController.profil(model, null);

        verify(model).addAttribute("activePage", "profil");
        verify(model).addAttribute("user", user);
        assertEquals("profil_page", viewName);
    }

    @Test
    void testUpdateUser_Success() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        String viewName = userController.updateUser(user, "newPassword", model);

        verify(userServiceImplementation).updateUser("testUser", "test@example.com", "newPassword");
        assertEquals("redirect:/profil?success=true", viewName);
    }

    @Test
    void testUpdateUser_Failure() {
        doThrow(new RuntimeException("Update failed")).when(userServiceImplementation).updateUser(any(), any(), any());

        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        String viewName = userController.updateUser(user, "newPassword", model);

        verify(model).addAttribute("message", "Erreur lors de la mise à jour du profil.");
        assertEquals("profil_page", viewName);
    }

    @Test
    void testAddRelation() {
        String viewName = userController.addRelation(model);

        verify(model).addAttribute("activePage", "add-relation");
        verify(model).addAttribute(eq("addRelationDto"), any(AddRelationDto.class));
        assertEquals("add_relation_page", viewName);
    }

    @Test
    void testAddConnection_Success() {
        when(authentication.getName()).thenReturn("current@example.com");
        AddRelationDto dto = new AddRelationDto();
        dto.setEmailAddedUser("friend@example.com");

        String viewName = userController.addConnection(dto, model);

        verify(userServiceImplementation).addUserConnection("friend@example.com", "current@example.com");
        verify(model).addAttribute("success", "Connexion ajoutée avec succès !");
        assertEquals("add_relation_page", viewName);
    }


}
