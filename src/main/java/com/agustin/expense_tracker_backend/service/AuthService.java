package com.agustin.expense_tracker_backend.service;

import com.agustin.expense_tracker_backend.DTO.UserDTO;
import com.agustin.expense_tracker_backend.entity.User;
import com.agustin.expense_tracker_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    // servicio para crear un nuevo usuario
    public UserDTO createUser(User user) {

        // verificar que el usuario no exista
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        // verificar que el email no exista
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya existe");
        }

        // instanciar el objeto User para encriptar contraseña
        User usr = new User();
        usr.setFirstName(user.getFirstName());
        usr.setLastName(user.getLastName());
        usr.setUsername(user.getUsername());
        usr.setEmail(user.getEmail());
        usr.setPassword(passwordEncoder.encode(user.getPassword()));

        // guardar usuario en la base de datos
        userRepository.save(usr);

        // devuelve los datos del usuario sin la contraseña
        return new UserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
