UPDATE activity_types
SET name = 'Página'
WHERE code = 'FOLLOW';

UPDATE activity_types
SET name = 'Reacción'
WHERE code = 'REACCION';

UPDATE activity_types
SET name = 'Publicación'
WHERE code = 'PUBLICACION';

INSERT INTO activity_types (name, code)
VALUES ('Perfil', 'FOLLOW_TIKTOK')