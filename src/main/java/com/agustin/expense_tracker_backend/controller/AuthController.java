package com.agustin.expense_tracker_backend.controller;

import com.agustin.expense_tracker_backend.DTO.UserDTO;
import com.agustin.expense_tracker_backend.entity.User;
import com.agustin.expense_tracker_backend.exception.ErrorResponse;
import com.agustin.expense_tracker_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // registrar un nuevo usuario
    // http://localhost:3001/api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {

        try {
            // validar que tanto el username como el email y el password no sean null
            if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {

                ErrorResponse errorResponse = new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Username, email o contraseña no pueden ser nulos"
                );

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // guardar el usuario en la base de datos
            authService.createUser(user);

            UserDTO  userCreated = new UserDTO(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername(),
                    user.getEmail()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
        } catch (IllegalArgumentException error) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), error.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }
}
