package com.inovagab.backend.config;

import com.inovagab.backend.model.Perfil;
import com.inovagab.backend.model.Usuario;
import com.inovagab.backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            if (!usuarioRepository.existsByEmail(
                    "lider@inovagab.com"
            )) {

                Usuario lider = new Usuario(
                        "Líder InovaGAB",
                        "lider@inovagab.com",
                        passwordEncoder.encode("123456"),
                        Perfil.LIDER
                );

                usuarioRepository.save(lider);
            }

            if (!usuarioRepository.existsByEmail(
                    "gestor@inovagab.com"
            )) {

                Usuario gestor = new Usuario(
                        "Gestor InovaGAB",
                        "gestor@inovagab.com",
                        passwordEncoder.encode("123456"),
                        Perfil.GESTOR
                );

                usuarioRepository.save(gestor);
            }

            if (!usuarioRepository.existsByEmail(
                    "operador@inovagab.com"
            )) {

                Usuario operador = new Usuario(
                        "Operador InovaGAB",
                        "operador@inovagab.com",
                        passwordEncoder.encode("123456"),
                        Perfil.OPERADOR
                );

                usuarioRepository.save(operador);
            }
        };
    }
}