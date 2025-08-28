/* Facebook */
INSERT INTO social_network_actions(social_network_id, activity_type_id)
VALUES ((SELECT id FROM social_networks WHERE code = 'FACEBOOK'),
        (SELECT id FROM activity_types WHERE code = 'REACCION')),
       ((SELECT id FROM social_networks WHERE code = 'FACEBOOK'),
        (SELECT id FROM activity_types WHERE code = 'COMENTARIO')),
       ((SELECT id FROM social_networks WHERE code = 'FACEBOOK'),
        (SELECT id FROM activity_types WHERE code = 'PUBLICACION')),
       ((SELECT id FROM social_networks WHERE code = 'FACEBOOK'),
        (SELECT id FROM activity_types WHERE code = 'GRUPO')),
       ((SELECT id FROM social_networks WHERE code = 'FACEBOOK'),
        (SELECT id FROM activity_types WHERE code = 'AMISTAD')),
       ((SELECT id FROM social_networks WHERE code = 'FACEBOOK'),
        (SELECT id FROM activity_types WHERE code = 'FOLLOW'));

/* Tiktok */
INSERT INTO social_network_actions(social_network_id, activity_type_id)
VALUES ((SELECT id FROM social_networks WHERE code = 'TIKTOK'),
        (SELECT id FROM activity_types WHERE code = 'REACCION')),
       ((SELECT id FROM social_networks WHERE code = 'TIKTOK'),
        (SELECT id FROM activity_types WHERE code = 'COMENTARIO')),
       ((SELECT id FROM social_networks WHERE code = 'TIKTOK'),
        (SELECT id FROM activity_types WHERE code = 'PUBLICACION')),
       ((SELECT id FROM social_networks WHERE code = 'TIKTOK'),
        (SELECT id FROM activity_types WHERE code = 'FOLLOW'));

/* X */
INSERT INTO social_network_actions(social_network_id, activity_type_id)
VALUES ((SELECT id FROM social_networks WHERE code = 'X'),
        (SELECT id FROM activity_types WHERE code = 'REACCION')),
       ((SELECT id FROM social_networks WHERE code = 'X'),
        (SELECT id FROM activity_types WHERE code = 'COMENTARIO')),
       ((SELECT id FROM social_networks WHERE code = 'X'),
        (SELECT id FROM activity_types WHERE code = 'PUBLICACION')),
       ((SELECT id FROM social_networks WHERE code = 'X'),
        (SELECT id FROM activity_types WHERE code = 'FOLLOW'));

/* Youtube */
INSERT INTO social_network_actions(social_network_id, activity_type_id)
VALUES ((SELECT id FROM social_networks WHERE code = 'YOUTUBE'),
        (SELECT id FROM activity_types WHERE code = 'REACCION')),
       ((SELECT id FROM social_networks WHERE code = 'YOUTUBE'),
        (SELECT id FROM activity_types WHERE code = 'COMENTARIO'));

/* Instagram */
INSERT INTO social_network_actions(social_network_id, activity_type_id)
VALUES ((SELECT id FROM social_networks WHERE code = 'INSTAGRAM'),
        (SELECT id FROM activity_types WHERE code = 'REACCION')),
       ((SELECT id FROM social_networks WHERE code = 'INSTAGRAM'),
        (SELECT id FROM activity_types WHERE code = 'COMENTARIO')),
       ((SELECT id FROM social_networks WHERE code = 'INSTAGRAM'),
        (SELECT id FROM activity_types WHERE code = 'PUBLICACION')),
       ((SELECT id FROM social_networks WHERE code = 'INSTAGRAM'),
        (SELECT id FROM activity_types WHERE code = 'FOLLOW'));