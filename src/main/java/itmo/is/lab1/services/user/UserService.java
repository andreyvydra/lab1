package itmo.is.lab1.services.user;

import itmo.is.lab1.models.user.User;
import itmo.is.lab1.models.user.enums.Role;
import itmo.is.lab1.repositories.UserRepository;
import itmo.is.lab1.services.common.errors.BadRequestException;
import itmo.is.lab1.services.common.errors.GeneralException;
import itmo.is.lab1.services.common.responses.GeneralMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }


    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Пользователь с таким именем уже существует");
        }

        return save(user);
    }


    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }


    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }
}
