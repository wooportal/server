/*!40101 SET character_set_client = utf8 */;

INSERT INTO `organisations` (`id`, `approved`, `name`, `website`, `mail`, `phone`, `address_id`) VALUES
('00000001-0000-0000-0008-000000000000', 0, 'notApprovedOrga', 'www.notApprovedOrga.de', 'notApprovedOrga@1.de', '01234567890', '00000000-0000-0000-0006-100000000000'),
('00000002-0000-0000-0008-000000000000', 0, 'notApprovedOrga2', 'www.notApprovedOrga2.de', 'notApprovedOrga2@1.de', '01234567890', '00000000-0000-0000-0006-100000000000'),
('00000003-0000-0000-0008-000000000000', 0, 'notApprovedOrga3', 'www.notApprovedOrga3.de', 'notApprovedOrga3@1.de', '01234567890', '00000000-0000-0000-0006-100000000000');

INSERT INTO `users` (`id`, `superuser`, `username`, `password`, `name`, `phone`) VALUES
('00000001-0000-0000-0004-000000000000', 0, 'notapprovedorga@user', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'notapprovedorga', '01234567890'),
('00000002-0000-0000-0004-000000000000', 0, 'createorga@user', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'createorga', '01234567890'),
('00000003-0000-0000-0004-000000000000', 0, 'notapprovedorga2@user', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'notapprovedorga2', '01234567890'),
('00000004-0000-0000-0004-000000000000', 0, 'blog1@user', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'blog1', '01234567890'),
('00000005-0000-0000-0004-000000000000', 0, 'blog2@user', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'blog2', '01234567890'),
('00000006-0000-0000-0004-000000000000', 0, 'blogNotApproved@user', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'blogNotApproved', '01234567890'),
('00000007-0000-0000-0004-000000000000', 0, 'bloggerApply@user', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'bloggerApply', '01234567890'),
('00000008-0000-0000-0004-000000000000', 0, 'bloggerTakeApproval@user', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'blogNotApproved', '01234567890'),
('00000009-0000-0000-0004-000000000000', 0, 'blogdelete1@user', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'blogdelete1', '01234567890'),
('00000011-0000-0000-0004-000000000000', 0, 'blogdelete2@user', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'blogdelete2', '01234567890');

INSERT INTO `providers` (`id`, `organisation_id`, `user_id`, `admin`, `approved`) VALUES
('00000001-0000-0000-0009-000000000000', '00000001-0000-0000-0008-000000000000', '00000001-0000-0000-0004-000000000000', 1, 1),
('00000002-0000-0000-0009-000000000000', '00000002-0000-0000-0008-000000000000', '00000003-0000-0000-0004-000000000000', 1, 1);

INSERT INTO `bloggers`(`id`, `approved`, `user_id`) VALUES
('00000000-0000-0000-0015-100000000000', 1, '00000004-0000-0000-0004-000000000000'),
('00000000-0000-0000-0015-200000000000', 1, '00000005-0000-0000-0004-000000000000'),
('00000000-0000-0000-0015-300000000000', 0, '00000006-0000-0000-0004-000000000000'),
('00000000-0000-0000-0015-400000000000', 1, '00000008-0000-0000-0004-000000000000'),
('00000000-0000-0000-0015-500000000000', 1, '00000009-0000-0000-0004-000000000000'),
('00000000-0000-0000-0015-600000000000', 1, '00000011-0000-0000-0004-000000000000');

INSERT INTO `topics` (`id`) VALUES
('00000000-0000-0000-0014-100000000000'),
('00000000-0000-0000-0014-200000000000'),
('00000000-0000-0000-0014-300000000000');

INSERT INTO `topic_translatables` (`id`, `name`, `language_id`, `parent_id`) VALUES
('00000000-0000-0000-0015-100000000000', 'topic1', '00000000-0000-0000-0013-400000000000', '00000000-0000-0000-0014-100000000000'),
('00000000-0000-0000-0015-200000000000', 'topicToChange', '00000000-0000-0000-0013-400000000000', '00000000-0000-0000-0014-200000000000'),
('00000000-0000-0000-0015-300000000000', 'topicToDelete', '00000000-0000-0000-0013-400000000000', '00000000-0000-0000-0014-300000000000');

INSERT INTO `pages` (`id`, `topic_id`) VALUES
('00000000-0000-0000-0016-100000000000', '00000000-0000-0000-0014-100000000000'),
('00000000-0000-0000-0016-200000000000', '00000000-0000-0000-0014-100000000000'),
('00000000-0000-0000-0016-300000000000', '00000000-0000-0000-0014-100000000000');

INSERT INTO `page_translatables` (`id`, `title`, `content`, `language_id`, `parent_id`) VALUES
('00000000-0000-0000-0017-100000000000', 'page1', '<p>page1</p>', '00000000-0000-0000-0013-400000000000', '00000000-0000-0000-0016-100000000000'),
('00000000-0000-0000-0017-200000000000', 'pageToChange', '<p>pageToChange</p>', '00000000-0000-0000-0013-400000000000', '00000000-0000-0000-0016-200000000000'),
('00000000-0000-0000-0017-300000000000', 'pageToDelete', '<p>pageToDelete</p>', '00000000-0000-0000-0013-400000000000', '00000000-0000-0000-0016-300000000000');

INSERT INTO `blogs`(`id`, `activity_id`, `blogger_id`) VALUES
('00000000-0000-0000-0016-100000000000', '00000000-0000-0000-0010-100000000000', '00000000-0000-0000-0015-100000000000'),
('00000000-0000-0000-0016-200000000000', null, '00000000-0000-0000-0015-100000000000'),
('00000000-0000-0000-0016-300000000000', '00000000-0000-0000-0010-100000000000', '00000000-0000-0000-0015-200000000000'),
('00000000-0000-0000-0016-400000000000', '00000000-0000-0000-0010-100000000000', '00000000-0000-0000-0015-100000000000'),
('00000000-0000-0000-0016-500000000000', '00000000-0000-0000-0010-100000000000', '00000000-0000-0000-0015-100000000000'),
('00000000-0000-0000-0016-600000000000', '00000000-0000-0000-0010-100000000000', '00000000-0000-0000-0015-100000000000'),
('00000000-0000-0000-0016-700000000000', '00000000-0000-0000-0010-100000000000', '00000000-0000-0000-0015-100000000000');

INSERT INTO `blog_translatables` (`id`, `parent_id`, `language_id`, `title`, `content`) VALUES
('00000000-0000-0000-0017-100000000000', '00000000-0000-0000-0016-100000000000', '00000000-0000-0000-0013-400000000000', 'blog1', '<p>blog1</p>'),
('00000000-0000-0000-0017-200000000000', '00000000-0000-0000-0016-200000000000', '00000000-0000-0000-0013-400000000000', 'blog1', '<p>blog1</p>'),
('00000000-0000-0000-0017-300000000000', '00000000-0000-0000-0016-300000000000', '00000000-0000-0000-0013-400000000000', 'blog2', '<p>blog2</p>'),
('00000000-0000-0000-0017-400000000000', '00000000-0000-0000-0016-400000000000', '00000000-0000-0000-0013-400000000000', 'toDeleteBlogControllerSuper', '<p>toDeleteBlogControllerSuper</p>'),
('00000000-0000-0000-0017-500000000000', '00000000-0000-0000-0016-500000000000', '00000000-0000-0000-0013-400000000000', 'toDeleteBlogControllerOwn', '<p>toDeleteBlogControllerOwn</p>'),
('00000000-0000-0000-0017-600000000000', '00000000-0000-0000-0016-600000000000', '00000000-0000-0000-0013-400000000000', 'toDeleteUserControllerSuper', '<p>toDeleteUserControllerSuper</p>'),
('00000000-0000-0000-0017-700000000000', '00000000-0000-0000-0016-700000000000', '00000000-0000-0000-0013-400000000000', 'toDeleteUserControllerOwn', '<p>toDeleteUserControllerOwn</p>');

INSERT INTO `images` (`id`, `caption`, `mime_type`, `image`) VALUES
('00000000-0000-0000-0017-100000000000', 'image1', 'mimetype', x'89504E470D0A1A0A0000000D494844520000001000000010080200000090916836000000017352474200AECE1CE90000000467414D410000B18F0BFC6105000000097048597300000EC300000EC301C76FA8640000001E49444154384F6350DAE843126220493550F1A80662426C349406472801006AC91F1040F796BD0000000049454E44AE426082'),
('00000000-0000-0000-0017-200000000000', 'image2', 'mimetype', x'89504E470D0A1A0A0000000D494844520000001000000010080200000090916836000000017352474200AECE1CE90000000467414D410000B18F0BFC6105000000097048597300000EC300000EC301C76FA8640000001E49444154384F6350DAE843126220493550F1A80662426C349406472801006AC91F1040F796BD0000000049454E44AE426082'),
('00000000-0000-0000-0017-300000000000', 'image3', 'mimetype', x'89504E470D0A1A0A0000000D494844520000001000000010080200000090916836000000017352474200AECE1CE90000000467414D410000B18F0BFC6105000000097048597300000EC300000EC301C76FA8640000001E49444154384F6350DAE843126220493550F1A80662426C349406472801006AC91F1040F796BD0000000049454E44AE426082');

INSERT INTO `organisations_images` (`id`, `organisation_id`, `image_id`) VALUES
('00000000-0000-0000-0018-100000000000', '00000000-0000-0000-0008-100000000000', '00000000-0000-0000-0017-100000000000');

INSERT INTO `activities_images` (`id`, `activity_id`, `image_id`) VALUES
('00000000-0000-0000-0019-100000000000', '00000000-0000-0000-0010-100000000000', '00000000-0000-0000-0017-200000000000');

INSERT INTO `blogs_images` (`id`, `blog_id`, `image_id`) VALUES
('00000000-0000-0000-0019-100000000000', '00000000-0000-0000-0016-100000000000', '00000000-0000-0000-0017-300000000000');
