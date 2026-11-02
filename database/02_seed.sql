-- Cinco categorías requeridas por el caso.
INSERT INTO FORUM_CATEGORIES (NAME, DESCRIPTION) VALUES ('Java y Spring', 'Desarrollo con Java, Spring Boot y APIs REST');
INSERT INTO FORUM_CATEGORIES (NAME, DESCRIPTION) VALUES ('Bases de datos', 'Oracle, SQL, modelado y persistencia');
INSERT INTO FORUM_CATEGORIES (NAME, DESCRIPTION) VALUES ('Frontend', 'Interfaces web y consumo de APIs');
INSERT INTO FORUM_CATEGORIES (NAME, DESCRIPTION) VALUES ('DevOps y Git', 'Control de versiones, despliegue e integración');
INSERT INTO FORUM_CATEGORIES (NAME, DESCRIPTION) VALUES ('Ayuda y consultas', 'Preguntas generales de programación');
COMMIT;

-- Para completar los registros de muestra, registra usuarios desde POST /api/auth/register
-- y crea temas/comentarios mediante la colección Postman. Así las claves quedan cifradas
-- y los datos iniciales pasan por los mismos endpoints que la demostración.
