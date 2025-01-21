INSERT INTO usuarios (nombre, correo_electronico, contrasena)
VALUES ('admin', 'admin@mail.com', '$2a$12$zzrjFi1vpWehhzK1PjXmRORHuNXotw7Jdt5yhQgoR8uR3T0nXK/sW');

-- Insertar curso de prueba
INSERT INTO cursos (nombre, categoria)
VALUES
    ('Spring Boot 3', 'Desarrollo Backend'),
    ('Java y JPA', 'Backend'),
    ('API REST', 'Backend');

-- Asignar rol de admin
INSERT INTO usuario_perfil (usuario_id, perfil_id)
SELECT
    (SELECT id FROM usuarios WHERE correo_electronico = 'admin@mail.com'),
    (SELECT id FROM perfiles WHERE nombre = 'ROLE_ADMIN');