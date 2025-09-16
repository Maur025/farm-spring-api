/* Facebook */
INSERT INTO reaction_types (name, code, social_network_id)
VALUES ('Me Gusta', 'ME_GUSTA_FB', (SELECT id FROM social_networks WHERE code = 'FACEBOOK')),
       ('Me Encanta', 'ME_ENCANTA', (SELECT id FROM social_networks WHERE code = 'FACEBOOK')),
       ('Me Importa', 'ME_IMPORTA', (SELECT id FROM social_networks WHERE code = 'FACEBOOK')),
       ('Me Divierte', 'ME_DIVIERTE', (SELECT id FROM social_networks WHERE code = 'FACEBOOK')),
       ('Me Asombra', 'ME_ASOMBRA', (SELECT id FROM social_networks WHERE code = 'FACEBOOK')),
       ('Me Entristece', 'ME_ENTRISTECE', (SELECT id FROM social_networks WHERE code = 'FACEBOOK')),
       ('Me Enfada', 'ME_ENFADA', (SELECT id FROM social_networks WHERE code = 'FACEBOOK'));

/* Tiktok */
INSERT INTO reaction_types (name, code, social_network_id)
VALUES ('Me Gusta', 'ME_GUSTA_TK', (SELECT id FROM social_networks WHERE code = 'TIKTOK'));

/* Youtube */
INSERT INTO reaction_types (name, code, social_network_id)
VALUES ('Me Gusta', 'ME_GUSTA_YT', (SELECT id FROM social_networks WHERE code = 'YOUTUBE')),
       ('No Me Gusta', 'NO_ME_GUSTA', (SELECT id FROM social_networks WHERE code = 'YOUTUBE'));

/* Instagram */
INSERT INTO reaction_types (name, code, social_network_id)
VALUES ('Me Gusta', 'ME_GUSTA_IG', (SELECT id FROM social_networks WHERE code = 'INSTAGRAM'));