INSERT INTO social_network_actions(social_network_id, activity_type_id)
VALUES ((SELECT id FROM social_networks WHERE code = 'TIKTOK'),
        (SELECT id FROM activity_types WHERE code = 'FOLLOW_TIKTOK'));

DELETE
FROM social_network_actions
WHERE social_network_id = (SELECT id FROM social_networks WHERE code = 'TIKTOK')
  AND activity_type_id = (SELECT id FROM activity_types WHERE code = 'FOLLOW');

