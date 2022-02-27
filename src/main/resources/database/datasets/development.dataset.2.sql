/*!40101 SET character_set_client = utf8 */;

INSERT INTO `social_media` (`id`, `name`, `url`, `icon`) VALUES
('00000001-0000-0000-0008-000000000000', 'facebook', 'https://facebook.com/', 'facebook'),
('00000002-0000-0000-0008-000000000000', 'instagram', 'https://instagram.com/', 'instagram'),
('00000003-0000-0000-0008-000000000000', 'twitter', 'https://twitter.com/', 'twitter');

UPDATE `activities` SET admission_fee = 0.50;

INSERT INTO `markups` (`id`, `tag_id`) VALUES
('00000000-0000-0000-0029-100000000000', 'markup1'),
('00000000-0000-0000-0029-200000000000', 'markupToChange'),
('00000000-0000-0000-0029-300000000000', 'markupToDelete');

INSERT INTO `markup_translatables` (`id`, `title`, `content`, `language_id`, `parent_id`) VALUES
('00000000-0000-0000-0029-100000000000', 'title1', '<p>markup1</p>', '00000000-0000-0000-0013-400000000000', '00000000-0000-0000-0029-100000000000'),
('00000000-0000-0000-0029-200000000000', 'title2', '<p>markupToChange</p>', '00000000-0000-0000-0013-400000000000', '00000000-0000-0000-0029-200000000000'),
('00000000-0000-0000-0029-300000000000', 'title3', '<p>markupToDelete</p>', '00000000-0000-0000-0013-400000000000', '00000000-0000-0000-0029-300000000000'),
('00000000-0000-0000-0029-400000000000', 'title4', '<p>markup2</p>', '00000000-0000-0000-0013-100000000000', '00000000-0000-0000-0029-100000000000');