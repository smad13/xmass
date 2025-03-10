package com.xmas.greet.servicio;

import com.xmas.greet.modelo.PasswordResetToken;
import com.xmas.greet.modelo.Usuario;

public interface PasswordResetTokenService {
    PasswordResetToken createToken(Usuario usuario);
    PasswordResetToken validateToken(String token);
    void deleteToken(PasswordResetToken token);
}

