package com.repinsky.copywise.services;

import com.repinsky.copywise.dtos.RegisterUserRequest;
import com.repinsky.copywise.exceptions.InputDataException;
import com.repinsky.copywise.models.User;
import com.repinsky.copywise.repositories.UserRepository;
import com.repinsky.copywise.utils.InputValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import static com.repinsky.copywise.constants.InfoMessage.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final InputValidationUtil validationService = new InputValidationUtil();

    public void createNewUser(RegisterUserRequest registerUserRequest) throws InputDataException {
        if (registerUserRequest.getPassword() == null) {
            throw new InputDataException(PASSWORD_CANNOT_BE_EMPTY.getValue());
        } else if (!registerUserRequest.getPassword().equals(registerUserRequest.getConfirmPassword())) {
            throw new InputDataException(PASSWORD_MISMATCH.getValue());
        } else {
            User user = new User();
            String encryptedPassword = passwordEncoder.encode(registerUserRequest.getPassword());
            String validationMessage = validateAndSaveUserFields(registerUserRequest, encryptedPassword, user);
            if (validationMessage != null) {
                throw new InputDataException(validationMessage);
            }

            userRepository.save(user);
        }
    }

    private String validateAndSaveUserFields(RegisterUserRequest registerUserRequest, String encryptedPassword, User user) {
        user.setRoles(Collections.singleton(roleService.getUserRole()));

        String email = registerUserRequest.getEmail();
        String validationMessage = "";

        validationMessage = validationService.acceptableEmail(email);
        if (validationMessage.equals("")) {
            Optional<User> userByEmail = findByEmail(email);
            if (userByEmail.isPresent()) {
                return EMAIL_ALREADY_EXISTS.getValue();
            }
            user.setEmail(email);
        } else {
            return validationMessage;
        }

        validationMessage = validationService.acceptablePassword(registerUserRequest.getPassword());
        // we take the password from the DTO, because we check the validity of the unencrypted password and save it
        // to the database
        if (validationMessage.equals("")) {
            user.setPassword(encryptedPassword);
        } else {
            return validationMessage;
        }

        return null;    //  successful validation
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        return null;
    }
}
