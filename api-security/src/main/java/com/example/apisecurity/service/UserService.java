package com.example.apisecurity.service;

import com.example.apisecurity.data.Token;
import com.example.apisecurity.data.User;
import com.example.apisecurity.data.UserRepo;
import com.example.apisecurity.exception.BadCredentialError;
import com.example.apisecurity.exception.EmailNotFoundError;
import com.example.apisecurity.exception.PasswordDoNotMatchError;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String ACCESS_SECRET = "gaD-DmPUJCNdgbHsR1vM0GoKE0TtXQDxAUdaSYEOC7g";
    private static final String REFRESH_SECRET = "gaD-DmPUJCNdgbHsR1vM0GoKE0TtXQDxAUdaSYEOC7g";
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public User getUserFromToken(String token) {
        return userRepo.findById(Jwt.from(token, ACCESS_SECRET).getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found!"));
    }

    public User register(String firstName, String lastName,
                         String password, String passwordConfirm, String email) {
        if(!Objects.equals(password, passwordConfirm)) {
            throw new PasswordDoNotMatchError();
        }
        return userRepo.save(
                new User(null, firstName, lastName, email, passwordEncoder.encode(password))
        );
    }

    public Login login(String email, String password) {
        var user = userRepo.findByEmail(email)
                .orElseThrow(EmailNotFoundError::new);

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialError();
        }
        var login = Login.of(user.getId(), ACCESS_SECRET, REFRESH_SECRET);
        var refreshToken = login.getRefreshToken();
        user.addToken(new Token(
                refreshToken.getToken(),
                refreshToken.getIssuedAt(),
                refreshToken.getExpiredAt()
        ));
        userRepo.save(user);
        return login;
    }

    public Login refreshAccess(String refreshToken) {
        var refreshJwt = Jwt.from(refreshToken, REFRESH_SECRET);
        var user = userRepo.findUserIdAAndTokenByRefreshToken(
                refreshJwt.getUserId(),
                refreshJwt.getToken(),
                refreshJwt.getExpiredAt()
        ).orElseThrow(EntityNotFoundException::new);

        return Login.of(
                user.getId(),
                ACCESS_SECRET,
                REFRESH_SECRET
        );
    }


















}
