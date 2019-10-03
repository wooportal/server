/*!40101 SET character_set_client = utf8 */;

INSERT INTO `configurations` (`id`, `item`, `value`) VALUES
('5f7229ef-7d83-40a3-adc3-c5025e223340', 'mapLongitude', '7.1756'),
('8902443d-e7c6-4cf4-9d9e-de1f0bb5bcc0', 'mapLatitude', '51.2640'),
('4e916814-f13e-4be5-8c91-65a105c0bbb4', 'mapZoomfactor', '11.5'),
('a6d1737b-24f9-44f6-a249-7eb0f056d16c', 'mapProjection', 'EPSG:4326'),
('f648fb08-85fc-4018-9316-eb9e1d0d85eb', 'mapCluster', '27.5'),
('c7064ccd-5ebd-4da0-98de-71f53b92fe25', 'portalName', 'wooportal'),
('d5af659f-1256-4c08-b0c9-5945150dc2b4', 'portalSubtitle', 'conventional event mapping'),
('6066b2c7-5e3f-46b8-a3ff-1c76b6af6b59', 'portalMail', 'info@codeschluss.de');

INSERT INTO `languages` (`id`, `locale`, `name`) VALUES
('4bc63526-fc8d-4e1a-87fb-a74269a0e0ae', 'es', 'Español'),
('502d6dab-c0d6-472c-84b8-1f1c2bbcc4fd', 'fr', 'Français'),
('0c851b5f-e239-4de0-9458-ccb1d079de55', 'en', 'English'),
('b6baaee8-cb61-4921-b432-798b69525fde', 'de', 'Deutsch'),
('3f40991f-731f-48c7-a480-f8b47eb4ee76', 'ar', 'العربية');

INSERT INTO `tags` (`id`, `description`) VALUES
('8fb34237-d179-4667-987b-a0d61a7e7a23', 'sport'),
('7c35b9fd-5822-4bec-87e0-fc4207b5addb', 'freizeit'),
('20b66cec-ac0f-42ae-b8c5-ecbb0defe85c', 'religion'),
('f88a8909-fe8f-4751-b85d-bcba3b36d9e8', 'sonntags'),
('fdb86a24-c4e3-4eb2-b675-53d7d269de32', 'liebe'),
('a742b32f-0f30-4c70-bd6a-6e03cc865081', 'feiern'),
('606c1344-31e9-4f24-8a22-b0a4929b2473', 'tanzen'),
('ba63541b-2c0a-41fa-9768-d3c08e0ac1e9', 'zusammen'),
('be8c49f1-7160-4531-b4ec-f7b81af9e565', 'freitags'),
('d1a33688-90b7-487e-9a71-69964eb6ef67', 'beratung');

INSERT INTO `tag_translatables` (`id`, `name`, `language_id`, `parent_id`) VALUES
('bfbc2ff4-4b9c-42d6-ab87-fdfee44ea263', 'sport', 'b6baaee8-cb61-4921-b432-798b69525fde', '8fb34237-d179-4667-987b-a0d61a7e7a23'),
('19a679d2-2dc0-4677-8c37-749fa04302f2', 'freizeit', 'b6baaee8-cb61-4921-b432-798b69525fde', '7c35b9fd-5822-4bec-87e0-fc4207b5addb'),
('fd6b54f8-b0de-489e-8de5-9c6c68b0977c', 'religion', 'b6baaee8-cb61-4921-b432-798b69525fde', '20b66cec-ac0f-42ae-b8c5-ecbb0defe85c'),
('42a1b656-5c11-4be6-93d1-20ded0000c04', 'sonntags', 'b6baaee8-cb61-4921-b432-798b69525fde', 'f88a8909-fe8f-4751-b85d-bcba3b36d9e8'),
('69a6b68a-31b9-4f9a-b2f8-35cc6212b278', 'liebe', 'b6baaee8-cb61-4921-b432-798b69525fde', 'fdb86a24-c4e3-4eb2-b675-53d7d269de32'),
('da2d4d8d-4173-4628-a6af-886b19f2d316', 'feiern', 'b6baaee8-cb61-4921-b432-798b69525fde', 'a742b32f-0f30-4c70-bd6a-6e03cc865081'),
('3e780e0d-6af0-4cc0-a98e-121fa6caf25b', 'tanzen', 'b6baaee8-cb61-4921-b432-798b69525fde', '606c1344-31e9-4f24-8a22-b0a4929b2473'),
('1059f7f0-7be8-41ed-b441-ab511c9e14e2', 'zusammen', 'b6baaee8-cb61-4921-b432-798b69525fde', 'ba63541b-2c0a-41fa-9768-d3c08e0ac1e9'),
('bbed45f7-a791-4de2-88cb-b35ab02e26e6', 'freitags', 'b6baaee8-cb61-4921-b432-798b69525fde', 'be8c49f1-7160-4531-b4ec-f7b81af9e565'),
('f2d7f017-ba73-4514-a3c9-f33110357f36', 'beratung', 'b6baaee8-cb61-4921-b432-798b69525fde', 'd1a33688-90b7-487e-9a71-69964eb6ef67');

INSERT INTO `target_groups` (`id`, `description`) VALUES
('f5bc2819-49a9-49f5-ac6f-7f850abece14', 'Junge Erwachsene'),
('82584686-6f67-4cea-963b-2ef15704efe2', 'Jungen'),
('43392d8f-77d7-46d1-94c2-ee4c479315b5', 'Mädchen'),
('aafd5b0d-d076-4e9c-93ce-9f066019731b', 'LSBTIQ'),
('438cb20a-06f2-431a-a40d-0e5eede2f5d7', 'Jugendliche mit Behinderung'),
('13291244-2865-4b2b-90a3-4525d30f6076', 'Migrant*innen'),
('955376f8-6a35-4a3e-952e-345ce49d48b5', 'Geflüchtete');

INSERT INTO `target_group_translatables` (`id`, `name`, `language_id`, `parent_id`) VALUES
('301239db-6bf4-4844-8f8f-0ae8b0957d42', 'Junge Erwachsene', 'b6baaee8-cb61-4921-b432-798b69525fde', 'f5bc2819-49a9-49f5-ac6f-7f850abece14'),
('7b9a2a78-e97e-4bd3-9903-1361f864bde7', 'Jungen', 'b6baaee8-cb61-4921-b432-798b69525fde', '82584686-6f67-4cea-963b-2ef15704efe2'),
('67d643aa-6242-45f9-a1ef-981b5d077d57', 'Mädchen', 'b6baaee8-cb61-4921-b432-798b69525fde', '43392d8f-77d7-46d1-94c2-ee4c479315b5'),
('d56bca91-03e8-4abf-9112-fbe40fa9fb2a', 'LSBTIQ', 'b6baaee8-cb61-4921-b432-798b69525fde', 'aafd5b0d-d076-4e9c-93ce-9f066019731b'),
('2d0bdaf3-4631-427f-89f2-189f8ad821ce', 'Jugendliche mit Behinderung', 'b6baaee8-cb61-4921-b432-798b69525fde', '438cb20a-06f2-431a-a40d-0e5eede2f5d7'),
('5b961145-cb6e-47db-a357-6cabb85d6139', 'Migrant*innen','b6baaee8-cb61-4921-b432-798b69525fde', '13291244-2865-4b2b-90a3-4525d30f6076'),
('fae468db-1a34-4614-a0f8-758c466c29da', 'Geflüchtete', 'b6baaee8-cb61-4921-b432-798b69525fde', '955376f8-6a35-4a3e-952e-345ce49d48b5');

INSERT INTO `users` (`id`, `superuser`, `username`, `password`, `name`, `phone`) VALUES
('b77c3d20-9053-430d-bebb-47548da554be', 1, 'super@user.de', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'Sebastian Superuser', '01234567890'),
('d52ab269-a12a-42fe-94ce-08bed27211a8', 0, 'admin@user.de', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'Annette Adminuser', '09876543210'),
('a6a00424-0d53-4ca5-96a0-5d975fc763c0', 0, 'anbieter1@user.de', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'Anton Anbieter', '09876543210'),
('b3da62a2-ef6b-4af9-84e1-9c98c5a40eec', 0, 'anbieter2@user.de', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'Alfred Anbieter', '09876543210'),
('747acfae-fde7-4767-9920-4b79d9061ebc', 0, 'blogger1@user.de', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'Brigitte Blogger', '09876543210'),
('000af454-8535-4788-b353-0177e98651d1', 0, 'blogger2@user.de', '$2a$10$0pLpBHF8gWe9UFz1eJzAHOwwUHMIjfkaImWTP1BX9wAmWLdcOvbNW', 'Barbara Blogger', '09876543210');

INSERT INTO `suburbs` (`id`, `name`, `created`, `modified`) VALUES
('02957cb5-a93c-4f7e-b016-20c1e7ee0c87','Uellendahl-Katernberg','2017-12-18 01:36:47','2017-12-18 01:36:47'),
('18e97a95-877e-418f-a136-03edda67e3ae','Elberfeld','2017-12-18 01:36:47','2017-12-18 01:36:47'),
('1fed19c5-2d04-481c-aedc-5c9ed0a509b1','Elberfeld-West','2017-12-18 01:36:47','2017-12-18 01:36:47'),
('2abe9d1e-983f-4a5b-89bc-4c4714d01e67','Heckinghausen','2017-12-18 01:36:47','2017-12-18 01:36:47'),
('2afef02e-211c-4885-a975-419135697066','Oberbarmen','2017-12-18 01:36:47','2017-12-18 01:36:47'),
('3a502d34-64cd-41ee-9ff8-c8242f44721b','Vohwinkel','2017-12-18 01:36:47','2017-12-18 01:36:47'),
('61fea5a4-8b0e-44fa-9f94-a85740280027','Ronsdorf','2017-12-18 01:36:47','2017-12-18 01:36:47'),
('621fa8f0-7dac-4ee9-8a48-77a89d33de66','Langerfeld-Bexenburg','2017-12-18 01:36:47','2017-12-18 01:36:47'),
('8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','Barmen','2017-12-18 01:36:47','2017-12-18 01:36:47'),
('9254f173-e645-4e38-a17a-b8b17ce4dfe9','Cronenberg','2017-12-18 01:36:47','2017-12-18 01:36:47');

INSERT INTO `addresses` (`id`, `latitude`, `longitude`, `street`, `house_number`, `postal_code`, `place`, `suburb_id`, `created`, `modified`) VALUES
('0bd40722-bc42-4fb5-aa51-97374a46f4a3',51.2856063843,7.2379789352,'Heinrich-Böll-Straße','258','42277','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-09-15 11:22:28','2018-09-15 11:22:28'),
('0d8653e1-1fd7-49dc-8b29-83fa436658d8',51.2635536194,7.1742868423,'Hünefeldstraße','54a','42285','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-02-14 12:33:19','2018-02-14 12:33:19'),
('0eceda06-486c-4f6c-a24c-b5b5d406537e',51.2457427979,7.1483640671,'Gaußstraße','20','42119','Wuppertal','18e97a95-877e-418f-a136-03edda67e3ae','2018-08-16 12:18:04','2018-08-16 12:18:04'),
('1575e1f9-85f9-4439-8df5-7c9a320f83bc',51.2317123413,7.0700817108,'Bahnstraße','9','42327','Wuppertal','3a502d34-64cd-41ee-9ff8-c8242f44721b','2018-08-25 11:44:49','2018-08-25 11:44:49'),
('3be5b8cd-25ff-434f-85db-0816122807b5',51.2100372314,7.1391000748,'Hauptstraße','96','42349','Wuppertal','9254f173-e645-4e38-a17a-b8b17ce4dfe9','2018-02-14 12:46:16','2018-02-14 12:46:16'),
('3cfc27de-6419-4674-8323-32d7b9368f25',52.1262741089,11.6005344391,'Fröbelstraße','1a','42117','Wuppertal','1fed19c5-2d04-481c-aedc-5c9ed0a509b1','2018-04-09 10:04:54','2018-04-09 10:04:54'),
('4049df9b-6907-449a-be16-d6a389893674',51.2656860352,7.1805038452,'Martin-Luther-Straße ','13','42285 ','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-02-14 12:55:43','2018-02-14 12:55:43'),
('4095f470-a72a-4f66-acc9-51b9ac1e95a4',51.2140960693,7.1460165977,'Hastener Straße','4','42349','Wuppertal','9254f173-e645-4e38-a17a-b8b17ce4dfe9','2018-06-18 09:03:46','2018-06-18 09:03:46'),
('4146a199-9b08-4d58-ab91-ae2ed69abcf2',51.2641448975,7.1467270851,'Gathe','6','42107','Wuppertal','18e97a95-877e-418f-a136-03edda67e3ae','2018-02-20 10:27:40','2018-02-20 10:27:40'),
('4233ef60-80ff-4a70-ac3b-450621f3b245',51.2704238892,7.1948719025,'Bernhard-Letterhaus-Straße','08','42275','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-01-30 12:38:53','2018-01-30 12:38:53'),
('4476e4cd-9541-4fec-be3b-a45fb83eba6f',51.2755241394,7.2214918137,'Berliner Straße','173','42277','Wuppertal','2afef02e-211c-4885-a975-419135697066','2018-02-14 12:51:14','2018-05-11 14:05:23'),
('4a10c8c9-31cd-4e7e-a945-66541445a2d6',51.2625122070,7.1420779228,'Ludwigstraße ','56','42105','Wuppertal','18e97a95-877e-418f-a136-03edda67e3ae','2018-02-14 12:27:21','2018-02-14 12:27:21'),
('4d075a87-57f3-430a-9936-49b5dfa8d7f1',51.2639732361,7.1467843056,'Gathe','6','42107','Wuppertal','18e97a95-877e-418f-a136-03edda67e3ae','2018-01-17 08:24:40','2018-01-17 08:24:40'),
('59c0c0c2-591c-479b-8445-0d4c60d36c89',51.2729835510,7.2067446709,'Höhne','43','42275','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-08-08 15:12:27','2018-08-08 15:12:27'),
('69bb35c1-7271-429b-864e-c7f316ea9a79',51.2640151978,7.1467399597,'Gathe','6','42107','Wuppertal','18e97a95-877e-418f-a136-03edda67e3ae','2018-01-17 08:39:00','2018-01-17 08:39:00'),
('6b38c621-cdee-4b25-82d2-533dbb338aef',51.2648620605,7.1483478546,'Platz der Republik','24','42107','Wuppertal','18e97a95-877e-418f-a136-03edda67e3ae','2018-08-24 14:37:30','2018-08-24 14:37:30'),
('6d3ba767-a5e9-4d1d-b252-2d3ebfee2cca',51.2640151978,7.1467399597,'Gathe','6','42107','Wuppertal','18e97a95-877e-418f-a136-03edda67e3ae','2018-01-17 08:28:09','2018-01-17 08:28:09'),
('6d3d0f6a-197d-4bdf-9d6d-014e77340df8',51.2688865662,7.1964454651,'Stresemannstraße','3','42275','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-01-18 17:59:30','2018-01-18 17:59:30'),
('6ff3b7f5-b9ea-45fe-b7b3-93a0bcf8cfdb',51.2624168396,7.1721982956,'Hünefeldstraße','14a','42285','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-09-21 09:11:46','2018-09-21 09:11:46'),
('72a75937-1724-4631-af60-6c726f72997a',51.2635536194,7.1742868423,'Hünefeldstraße','54a','42285','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-02-14 12:18:33','2018-02-14 12:18:33'),
('72f235ab-191b-4421-a6fe-c516a8a54683',51.2595939636,7.1712403297,'Elberfelder Straße','87','42285','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-06-19 13:59:18','2018-06-19 13:59:18'),
('7abdd488-ce07-4893-9012-799a00ccfa28',51.2289810181,7.0696187019,'Gräfrather Straße','15','42329','Wuppertal','3a502d34-64cd-41ee-9ff8-c8242f44721b','2018-09-13 15:36:46','2018-09-13 15:36:46'),
('7d0ab2cb-9fb8-4c82-933a-343db499ede5',51.2295570374,7.0676479340,'Rubensstraße','4','42329','Wuppertal','3a502d34-64cd-41ee-9ff8-c8242f44721b','2018-08-25 11:19:16','2018-08-25 11:19:16'),
('8291a3d4-e84c-438b-83f2-78636f26014d',51.2721557617,7.2001295090,'Johannes-Rauh-Platz','1','42275 ','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-02-19 17:03:33','2018-02-19 17:03:33'),
('870fc695-5cf3-4b24-9024-e66c8a921c07',51.2641448975,7.1467270851,'Gathe','6','42107','Wuppertal Elberfeld','18e97a95-877e-418f-a136-03edda67e3ae','2018-02-26 21:44:02','2018-02-26 21:44:02'),
('8d932c0c-c5e0-4fc4-9235-63bde4b8f063',51.2308731079,7.1311569214,'Spessartweg','25','42349','Wuppertal','9254f173-e645-4e38-a17a-b8b17ce4dfe9','2018-06-18 08:42:25','2018-06-18 08:42:25'),
('90ac3a97-dd86-4482-9b64-4dacf5696ec0',51.2625122070,7.1420779228,'Ludwigstraße','56','42105','Wuppertal','18e97a95-877e-418f-a136-03edda67e3ae','2018-02-14 12:23:06','2018-02-14 12:23:06'),
('96b4809d-93a7-4455-9c20-5dc0072d541a',51.2774238586,7.2040734291,'Eintrachtstraße','45','42275','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-02-27 09:00:07','2018-02-27 09:00:07'),
('a0b85371-db5b-46a1-a921-4f0d3c4e010b',51.2481155396,7.1268663406,'Fröbelstraße','1a','42117','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-04-09 09:31:44','2018-04-09 09:31:44'),
('a59f8140-6e8a-4f60-8db4-3b197591321f',51.2565040588,7.1641497612,'Bendahler Straße','29','42285','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-08-21 08:01:28','2018-08-21 08:01:28'),
('a66ad6d8-29c7-4b15-a7ae-fee1b3d8239f',51.2755241394,7.2214918137,'Berliner Straße','173','42277','Wuppertal','2afef02e-211c-4885-a975-419135697066','2018-02-14 12:48:45','2018-05-11 14:04:56'),
('b94df03a-9999-4183-a9f7-8ed8ca3a3a99',51.2641448975,7.1467270851,'Gathe','6','42107','Wuppertal','18e97a95-877e-418f-a136-03edda67e3ae','2018-02-26 16:18:57','2018-02-26 16:18:57'),
('bd590d22-c82e-425f-9ebe-7b0f44433ef5',51.2307624817,7.0746173859,'Westring','74','42329','Wuppertal','3a502d34-64cd-41ee-9ff8-c8242f44721b','2018-09-15 10:51:55','2018-09-15 10:51:55'),
('c461e0e9-ad0a-47e0-bfcb-b686fdcadfeb',51.2782173157,7.2186446190,'Wichlinghauser Straße','38','42277','Wuppertal','2afef02e-211c-4885-a975-419135697066','2018-10-23 09:04:25','2018-10-23 09:04:25'),
('d2063650-3314-4102-9de6-64726781fa16',51.2569274902,7.1404852867,'Kolpingstraße','13','42103','Wuppertal','18e97a95-877e-418f-a136-03edda67e3ae','2018-02-01 14:27:04','2018-02-01 14:27:04'),
('d6212acd-72db-4da2-9d1f-b32eb426cdb5',51.2691879272,7.2025456429,'Saarbrücker Straße','40','42289','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-08-02 07:02:57','2018-08-02 07:02:57'),
('d8912840-958e-4265-a64d-c71202c56fc9',51.2625808716,7.1455755234,'Friedrichstraße','2','42105','Wuppertal','18e97a95-877e-418f-a136-03edda67e3ae','2018-02-03 21:02:29','2018-02-03 21:02:29'),
('da30a7a4-3995-4bb3-b5ed-6319b2e266c5',51.2594261169,7.1526308060,'Deweerthstraße','117','42107','Wuppertal','18e97a95-877e-418f-a136-03edda67e3ae','2018-03-27 12:20:49','2018-03-27 12:20:49'),
('db2527da-f0df-4da7-8ef2-ec10d56aa613',51.2594642639,7.1714754105,'Elberfelder Straße','87-89','42285','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-03-27 12:27:12','2018-03-27 12:27:12'),
('e558777d-192a-4f25-b05b-2d1c82b5c445',51.2284202576,7.1394515038,'Küllenhahner Straße','145','42349','Wuppertal','9254f173-e645-4e38-a17a-b8b17ce4dfe9','2018-06-18 09:28:02','2018-06-18 09:28:02'),
('edbd167f-2cd3-4fdd-9e8e-71328a628841',51.2704353333,7.1950840950,'Bernhard-Letterhaus-Straße','8','42275','Wuppertal','8c9c7379-e6f8-4949-afb2-d8ca1e76e03a','2018-01-30 12:49:52','2018-01-30 12:49:52'),
('eff7b6b0-ee69-4b8d-8247-ecc112a860f5',51.2365646362,7.0955810547,'Unten Vorm Steeg','1','42329','Wuppertal','1fed19c5-2d04-481c-aedc-5c9ed0a509b1','2018-09-21 09:37:58','2018-10-10 08:28:53');

INSERT INTO `categories` (`id`, `description`, `color`) VALUES
('44813d47-6f0f-468a-994a-29d4d46ee376', 'Sport', '#4C9FBA'),
('96075110-64b9-4be5-ac5d-5175da09459a', 'Essen / Trinken', '#81A3FF'),
('ffa5821b-4ecc-4525-a695-d2d1b174d150', 'Musik / Tanz', '#DD574A'),
('f14f0c1a-fbcd-43c9-802e-d47a3b889e52', 'Kreativität', '#79A838'),
('5129ca29-2edf-47ef-8c18-62acd25e5c3b', 'Gaming', '#D36396'),
('08f14d83-e76a-4a6d-a033-1cadec1bdf5c', 'Unterwegs', '#7062BF'),
('20059afe-a7ca-413e-9d78-70893137732d', 'Events', '#F2DE9E'),
('679808eb-dece-4d29-888e-15bdc32e0641', 'Help me, please!', '#A83886'),
('46ab1eb7-4cdd-4cfd-b13b-49f38765e11e', 'Chillen', '#EFCE63'),
('005d164a-b215-40cb-91cf-9d100e471fe5', 'Politisch organisieren', '#6FC9A3'),
('53711473-3649-4f2c-9b03-5feb6aaaf413', 'Ferien', '#FFA16F'),
('3f01896f-4374-41f1-baa4-3bcc62513740', 'Religion', '#236E4E');

INSERT INTO `category_translatables` (`id`, `name`, `language_id`, `parent_id`) VALUES
('7966e6ce-fb98-4d6a-bbf7-efefc74be045', 'Sport', 'b6baaee8-cb61-4921-b432-798b69525fde', '44813d47-6f0f-468a-994a-29d4d46ee376'),
('70526964-5692-46e1-be0b-0604ffbb6e6c', 'Essen / Trinken', 'b6baaee8-cb61-4921-b432-798b69525fde', '96075110-64b9-4be5-ac5d-5175da09459a'),
('a2904298-1121-4793-b5e5-c09d0652e52d', 'Musik / Tanz', 'b6baaee8-cb61-4921-b432-798b69525fde', 'ffa5821b-4ecc-4525-a695-d2d1b174d150'),
('ca1c913f-adca-4e82-a1f8-773336e2a88e', 'Kreativität', 'b6baaee8-cb61-4921-b432-798b69525fde', 'f14f0c1a-fbcd-43c9-802e-d47a3b889e52'),
('81602349-5dff-4784-964d-e1aaf146d802', 'Gaming', 'b6baaee8-cb61-4921-b432-798b69525fde', '5129ca29-2edf-47ef-8c18-62acd25e5c3b'),
('b2ebb67d-7416-47a8-beed-f70773e76cef', 'Unterwegs', 'b6baaee8-cb61-4921-b432-798b69525fde', '08f14d83-e76a-4a6d-a033-1cadec1bdf5c'),
('1b1d7e5a-585f-46e5-9899-980d25a717c8', 'Events', 'b6baaee8-cb61-4921-b432-798b69525fde', '20059afe-a7ca-413e-9d78-70893137732d'),
('1ec9945f-dc46-4f27-ae53-2ef80f405354', 'Help me, please!', 'b6baaee8-cb61-4921-b432-798b69525fde', '679808eb-dece-4d29-888e-15bdc32e0641'),
('952bcd32-9092-4893-bd50-6b7e2742e6d2', 'Chillen', 'b6baaee8-cb61-4921-b432-798b69525fde', '46ab1eb7-4cdd-4cfd-b13b-49f38765e11e'),
('4a0e1984-5ce4-4282-b0e2-fcacaa50fa77', 'Politisch organisieren', 'b6baaee8-cb61-4921-b432-798b69525fde', '005d164a-b215-40cb-91cf-9d100e471fe5'),
('322909b2-dd09-4c01-835f-76680df93067', 'Ferien', 'b6baaee8-cb61-4921-b432-798b69525fde', '53711473-3649-4f2c-9b03-5feb6aaaf413'),
('626bef9d-be89-43e8-b0a3-a92b0f54c1fc', 'Religion', 'b6baaee8-cb61-4921-b432-798b69525fde', '3f01896f-4374-41f1-baa4-3bcc62513740');

INSERT INTO `organisations` (`id`, `name`, `website`, `mail`, `phone`, `address_id`, `approved`) VALUES
('7abe3193-4dd1-41a0-9013-1c3106bb0dfe', 'Sport zum Mitmachen e.V.', 'http://www.szmeV.de', 'szmeV@1.de', '01234567890', 'edbd167f-2cd3-4fdd-9e8e-71328a628841', 1),
('e110bc85-2b51-4269-a605-e78828cb72a2', 'Chillclub', 'http://www.chillclub.com', 'chillclub@2.com', '09876543210', 'db2527da-f0df-4da7-8ef2-ec10d56aa613', 1),
('1779192b-7d83-4fdb-8f44-ef9acb92d6a3', 'Die Wuppertaler Musiker', 'http://www.wuppertal-musik.com', 'wuppertal-musik@3.com', '09876543210', 'd8912840-958e-4265-a64d-c71202c56fc9', 1),
('025388dd-2ed9-4761-980b-43d50598e137', 'Wupps Gaming', 'http://www.wuppsgaming.com', 'wuppsgaming@mail.com', '09876543210', '96b4809d-93a7-4455-9c20-5dc0072d541a', 1),
('46f36319-05e2-473d-b9b6-740053b0a957', 'Religion in Wuppertal', 'http://www.rel-wuppertal.com', 'rel-wuppertal@mail.com', '09876543210', 'a59f8140-6e8a-4f60-8db4-3b197591321f', 1),
('0c09dcd9-2bbc-4b94-8214-1902f512573d', 'Hilfe zur Selbsthilfe', 'http://www.h-z-s.com', 'h-z-s@translate.com', '09876543210', '4146a199-9b08-4d58-ab91-ae2ed69abcf2', 1),
('075dbdaa-cc60-44c7-94f0-f1547372304c', 'Freizeit Hunters', 'http://www.freizeithunters.com', 'freizeithunters@noadmin.com', '09876543210', '4095f470-a72a-4f66-acc9-51b9ac1e95a4', 1),
('1eb78966-ac73-4493-870b-ce369a998715', 'Wuppertal Politics', 'http://www.wuppertalpolitics.com', 'wuppertalpolitics@orgaMail.com', '09876543210', '59c0c0c2-591c-479b-8445-0d4c60d36c89', 1),
('6d73353b-d51c-4c7e-8267-c05d808e9d6b', 'Wuppgngoes Online', 'http://www.wuppngoesonline.com', 'wuppngoesonline@orgaMap.com', '09876543210', '4049df9b-6907-449a-be16-d6a389893674', 1);

INSERT INTO `organisation_translatables` (`id`, `parent_id`, `language_id`, `description`) VALUES
('e0512402-cb28-4017-9641-de440b031cdd','7abe3193-4dd1-41a0-9013-1c3106bb0dfe', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Wir sind eine Organisation, die sich zum Ziel gesetzt hat Jugendlichen die Freude an der Bewegung näher zu bringen'),
('820a05d1-c496-4270-9f99-829821958c7e','e110bc85-2b51-4269-a605-e78828cb72a2', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Wir chillen den ganzen Tag und gehen ab und zu auch mal raus in die weite Welt'),
('5e86c39b-6d85-4d9f-a8c3-f9e6d6f1023b','1779192b-7d83-4fdb-8f44-ef9acb92d6a3', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Wir sind eine Einrichtung, die zusammen mit Jugendlichen Musik macht'),
('ddd81021-5ff5-4f72-96e4-ac71162d60e2','025388dd-2ed9-4761-980b-43d50598e137', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Wir veranstalten öfters Gaming Sessions'),
('e409b4df-1510-4c48-b93c-8985a3c4f7e6','46f36319-05e2-473d-b9b6-740053b0a957', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Religion in Wuppertal ist eine Organisation, die sich mit mit Jugendlich den religiösen Fragen des Lebens verschrieben haben'),
('3cdaf699-f099-49ae-9df1-1a7ff3eceda6','0c09dcd9-2bbc-4b94-8214-1902f512573d', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Wir helfen euch wie man Probleme selbst lösen kann. Kommt vorbei!'),
('5adfdd2a-5a86-4d11-b91b-9cbaef79ee32','075dbdaa-cc60-44c7-94f0-f1547372304c', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Wir machen gerne Urlaub und nehmen euch gerne dabei mit!'),
('9f417282-e0fd-44b2-997c-8a9d00e10e71','1eb78966-ac73-4493-870b-ce369a998715', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Wir möchten, dass sich Jugendliche mehr in der Politik engagieren'),
('5fddffde-dfe4-4821-9d77-eca8738d46f9','6d73353b-d51c-4c7e-8267-c05d808e9d6b', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Wir programmieren mit euch und basteln spannende Anwendungen');

INSERT INTO `providers` (`id`, `organisation_id`, `user_id`, `admin`, `approved`) VALUES
('f11d6edf-bf8e-43c0-8d5e-0256b8c9addf', '7abe3193-4dd1-41a0-9013-1c3106bb0dfe', 'd52ab269-a12a-42fe-94ce-08bed27211a8', 1, 1),
('de7ccfb2-bf1c-45e0-b826-02399fd89f74', 'e110bc85-2b51-4269-a605-e78828cb72a2', 'd52ab269-a12a-42fe-94ce-08bed27211a8', 1, 1),
('53f101a0-641f-497f-9642-0f953d70dcac', '1779192b-7d83-4fdb-8f44-ef9acb92d6a3', 'd52ab269-a12a-42fe-94ce-08bed27211a8', 1, 1),
('9ae54cba-2265-4972-87bc-000440cd8fd8', '025388dd-2ed9-4761-980b-43d50598e137', 'd52ab269-a12a-42fe-94ce-08bed27211a8', 1, 0),
('62a9e6f9-22be-4d5c-805d-a1f77e1bd511', '46f36319-05e2-473d-b9b6-740053b0a957', 'd52ab269-a12a-42fe-94ce-08bed27211a8', 1, 1),
('7154e824-e2f7-481c-a8b1-1ad834557c57', '0c09dcd9-2bbc-4b94-8214-1902f512573d', 'd52ab269-a12a-42fe-94ce-08bed27211a8', 1, 1),
('4249325c-db6f-47e9-9943-bd1f61725c5d', '075dbdaa-cc60-44c7-94f0-f1547372304c', 'd52ab269-a12a-42fe-94ce-08bed27211a8', 1, 0),
('839bce9a-fdce-4d55-ad0b-bd5d74a33575', '1eb78966-ac73-4493-870b-ce369a998715', 'd52ab269-a12a-42fe-94ce-08bed27211a8', 1, 1),
('cc24af55-027b-4371-8a67-a6f85ecef3f8', '6d73353b-d51c-4c7e-8267-c05d808e9d6b', 'd52ab269-a12a-42fe-94ce-08bed27211a8', 1, 1),
('8e67104f-6809-4dec-b85e-aeb8c8434cc1', '7abe3193-4dd1-41a0-9013-1c3106bb0dfe', 'a6a00424-0d53-4ca5-96a0-5d975fc763c0', 1, 1),
('3a3b869a-f94a-403b-a46f-c687a753d72f', 'e110bc85-2b51-4269-a605-e78828cb72a2', 'b3da62a2-ef6b-4af9-84e1-9c98c5a40eec', 0, 1),
('3418d88d-5477-42fd-9cbe-23fc8884ea32', '1779192b-7d83-4fdb-8f44-ef9acb92d6a3', 'a6a00424-0d53-4ca5-96a0-5d975fc763c0', 0, 1),
('8a0dade9-3e40-4420-b92d-793e94cc2478', '025388dd-2ed9-4761-980b-43d50598e137', 'b3da62a2-ef6b-4af9-84e1-9c98c5a40eec', 0, 1),
('432e2366-c0f3-42b8-bf83-022a832f791e', '46f36319-05e2-473d-b9b6-740053b0a957', 'a6a00424-0d53-4ca5-96a0-5d975fc763c0', 0, 1),
('27f22436-f182-4ca7-b3e8-7f1124f2c0b2', '0c09dcd9-2bbc-4b94-8214-1902f512573d', 'b3da62a2-ef6b-4af9-84e1-9c98c5a40eec', 0, 1),
('a1ccd97d-f7aa-4080-9380-e4de2a17ffa6', '075dbdaa-cc60-44c7-94f0-f1547372304c', 'a6a00424-0d53-4ca5-96a0-5d975fc763c0', 0, 1),
('5d1abb9f-8a32-4343-8346-77ea664e561b', '1eb78966-ac73-4493-870b-ce369a998715', 'b3da62a2-ef6b-4af9-84e1-9c98c5a40eec', 0, 1),
('e150af05-6750-4f76-85cc-f8f1208cb3cb', '6d73353b-d51c-4c7e-8267-c05d808e9d6b', 'a6a00424-0d53-4ca5-96a0-5d975fc763c0', 0, 1);

INSERT INTO `activities` (`id`, `address_id`, `provider_id`, `category_id`, `contact_name`, `mail`, `phone`, `likes`) VALUES
('0d44adfc-7b57-43fc-bed0-a5efd8aec6ef', '72a75937-1724-4631-af60-6c726f72997a', 'f11d6edf-bf8e-43c0-8d5e-0256b8c9addf', '44813d47-6f0f-468a-994a-29d4d46ee376', 'Kontaktname', 'mail@kontakt.de', '123456789', 12),
('e3088c34-14ca-4034-b2d6-5e821a0fc22d', '72f235ab-191b-4421-a6fe-c516a8a54683', 'de7ccfb2-bf1c-45e0-b826-02399fd89f74', '46ab1eb7-4cdd-4cfd-b13b-49f38765e11e', 'Kontaktname', 'mail@kontakt.de', '123456789', 20),
('866a86a3-271f-414d-ba2f-c6ec23b434ef', '7abdd488-ce07-4893-9012-799a00ccfa28', '53f101a0-641f-497f-9642-0f953d70dcac', 'ffa5821b-4ecc-4525-a695-d2d1b174d150', 'Kontaktname', 'mail@kontakt.de', '123456789', 5),
('e41d0798-db07-4064-bcc7-75c4674501e4', '7d0ab2cb-9fb8-4c82-933a-343db499ede5', '9ae54cba-2265-4972-87bc-000440cd8fd8', '5129ca29-2edf-47ef-8c18-62acd25e5c3b', 'Kontaktname', 'mail@kontakt.de', '123456789', 27),
('2a89749e-ba9f-4146-944e-4c1f2f339ea9', '8291a3d4-e84c-438b-83f2-78636f26014d', '62a9e6f9-22be-4d5c-805d-a1f77e1bd511', '08f14d83-e76a-4a6d-a033-1cadec1bdf5c', 'Kontaktname', 'mail@kontakt.de', '123456789', 18),
('746ccbd0-8947-42ec-9a99-741768ffcb23', '870fc695-5cf3-4b24-9024-e66c8a921c07', '7154e824-e2f7-481c-a8b1-1ad834557c57', '679808eb-dece-4d29-888e-15bdc32e0641', 'Kontaktname', 'mail@kontakt.de', '123456789', 9),
('4e1829a7-fb08-419a-8910-781c8ae7cfec', '8d932c0c-c5e0-4fc4-9235-63bde4b8f063', '4249325c-db6f-47e9-9943-bd1f61725c5d', '53711473-3649-4f2c-9b03-5feb6aaaf413', 'Kontaktname', 'mail@kontakt.de', '123456789', 11),
('20a7c475-c04f-401f-9801-2c8839cf7ead', '90ac3a97-dd86-4482-9b64-4dacf5696ec0', '839bce9a-fdce-4d55-ad0b-bd5d74a33575', '005d164a-b215-40cb-91cf-9d100e471fe5', 'Kontaktname', 'mail@kontakt.de', '123456789', 2),
('8534edaf-f42d-4da2-81e5-5a5917ca95f0', '96b4809d-93a7-4455-9c20-5dc0072d541a', 'cc24af55-027b-4371-8a67-a6f85ecef3f8', '20059afe-a7ca-413e-9d78-70893137732d', 'Kontaktname', 'mail@kontakt.de', '123456789', 0),
('43f903d7-d977-4ca2-8ea7-fd0b88bf3e70', 'a0b85371-db5b-46a1-a921-4f0d3c4e010b', '8e67104f-6809-4dec-b85e-aeb8c8434cc1', '44813d47-6f0f-468a-994a-29d4d46ee376', 'Kontaktname', 'mail@kontakt.de', '123456789', 57),
('bfa2d7e7-96b3-467d-8f89-2a11dad48dff', 'a59f8140-6e8a-4f60-8db4-3b197591321f', '3a3b869a-f94a-403b-a46f-c687a753d72f', 'f14f0c1a-fbcd-43c9-802e-d47a3b889e52', 'Kontaktname', 'mail@kontakt.de', '123456789', 4),
('0e803fad-d6f5-49d0-be34-9d2f8b5a7a5f', 'a66ad6d8-29c7-4b15-a7ae-fee1b3d8239f', '3418d88d-5477-42fd-9cbe-23fc8884ea32', 'ffa5821b-4ecc-4525-a695-d2d1b174d150', 'Kontaktname', 'mail@kontakt.de', '123456789', 135),
('03b7a3dd-8754-43d0-bd99-45c3625df096', 'b94df03a-9999-4183-a9f7-8ed8ca3a3a99', '8a0dade9-3e40-4420-b92d-793e94cc2478', '5129ca29-2edf-47ef-8c18-62acd25e5c3b', 'Kontaktname', 'mail@kontakt.de', '123456789', 87),
('d2917356-5a38-4426-903e-78607f45b566', 'bd590d22-c82e-425f-9ebe-7b0f44433ef5', '432e2366-c0f3-42b8-bf83-022a832f791e', '3f01896f-4374-41f1-baa4-3bcc62513740', 'Kontaktname', 'mail@kontakt.de', '123456789', 2),
('91adc0bf-672c-4caf-a9a0-0c59ea29e322', 'c461e0e9-ad0a-47e0-bfcb-b686fdcadfeb', '27f22436-f182-4ca7-b3e8-7f1124f2c0b2', '08f14d83-e76a-4a6d-a033-1cadec1bdf5c', 'Kontaktname', 'mail@kontakt.de', '123456789', 4),
('48ecadc2-e8fb-47ee-bd00-55c7cb842e6f', 'd2063650-3314-4102-9de6-64726781fa16', 'a1ccd97d-f7aa-4080-9380-e4de2a17ffa6', '53711473-3649-4f2c-9b03-5feb6aaaf413', 'Kontaktname', 'mail@kontakt.de', '123456789', 49),
('c29152f8-4e6f-4d05-8606-4ceef69cd47c', 'd6212acd-72db-4da2-9d1f-b32eb426cdb5', '5d1abb9f-8a32-4343-8346-77ea664e561b', '20059afe-a7ca-413e-9d78-70893137732d', 'Kontaktname', 'mail@kontakt.de', '123456789', 4),
('7c225870-7cf2-4ce0-98fa-db18b3b6aebc', 'd8912840-958e-4265-a64d-c71202c56fc9', 'e150af05-6750-4f76-85cc-f8f1208cb3cb', '20059afe-a7ca-413e-9d78-70893137732d', 'Kontaktname', 'mail@kontakt.de', '123456789', 97),
('415bed60-1d7f-4562-b920-4011f1a0b1ec', 'da30a7a4-3995-4bb3-b5ed-6319b2e266c5', 'f11d6edf-bf8e-43c0-8d5e-0256b8c9addf', '08f14d83-e76a-4a6d-a033-1cadec1bdf5c', 'Kontaktname', 'mail@kontakt.de', '123456789', 0),
('4bfb4694-73c9-42b6-949e-fec984dbd7c4', 'e558777d-192a-4f25-b05b-2d1c82b5c445', 'de7ccfb2-bf1c-45e0-b826-02399fd89f74', 'ffa5821b-4ecc-4525-a695-d2d1b174d150', 'Kontaktname', 'mail@kontakt.de', '123456789', 1),
('61b42a08-da2e-4a3b-8824-fc3a3cb6e9de', 'db2527da-f0df-4da7-8ef2-ec10d56aa613', '53f101a0-641f-497f-9642-0f953d70dcac', '5129ca29-2edf-47ef-8c18-62acd25e5c3b', 'Kontaktname', 'mail@kontakt.de', '123456789', 7);

INSERT INTO `activity_translatables` (`id`, `parent_id`, `language_id`, `name`, `description`) VALUES
('5ffe5fe7-71fc-4f2a-ad70-91e0e7dd7999', '0d44adfc-7b57-43fc-bed0-a5efd8aec6ef', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Sportaktivität', 'Eine Sportaktivität bei der alle mitmachen können'),
('bc4bea45-d82e-4cab-8e96-2ab343613ac9', 'e3088c34-14ca-4034-b2d6-5e821a0fc22d', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Chillen unter der Woche', 'Wir chillen sau gern unter der Woche, vor allem wenns draußen so kalt ist'),
('7bfded2d-2cdf-4380-8bd1-c56930c1d03d', '866a86a3-271f-414d-ba2f-c6ec23b434ef', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Music gathering', 'Bring dein Instrument mit und dann wird gejammt!'),
('68f53fc0-35f7-4f83-93e0-62b618966717', 'e41d0798-db07-4064-bcc7-75c4674501e4', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Gaming am Wochenende', 'Am Wochenende wird zusammen gezockt. Alles ist erlaubt.'),
('3d5f0b0f-acfb-4df4-ad6f-892345ec24a1', '2a89749e-ba9f-4146-944e-4c1f2f339ea9', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Tag der offenen Tür', 'Wir veranstalten einen Tag der offenen Tür und jeder ist herzlich eingeladen. Es gibt Essen und zu trinken!'),
('503d1bf7-6ba8-47d1-9720-becc173ef9d8', '746ccbd0-8947-42ec-9a99-741768ffcb23', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Selbsthilfegruppe', 'Wir wollen euch bei bringen wir ihr euch am besten selbst hilft und wie man Informationen aus dem Netz ziehen kann.'),
('977e7426-80e2-448d-bff3-482c9141cc60', '4e1829a7-fb08-419a-8910-781c8ae7cfec', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Ausflug ins Fantasialand', 'Wir machen einen Ausflug ins Fantasialand. Achtung, es gibt nur begrenzt Plätze!'),
('8559145d-aadc-4d96-9097-52af3cf2cc7b', '20a7c475-c04f-401f-9801-2c8839cf7ead', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Jugendversammlung der Partei XY', 'Wir von der Partei YX wollen eine neue Jugendorganisation in Wuppertal gründen und laden hiermit jeden herzlich ein zu kommen.'),
('c2acaabd-6128-45aa-b98f-6d3776529aea', '8534edaf-f42d-4da2-81e5-5a5917ca95f0', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Hackathon', 'Wir wollen zusammen ein Portal basteln, das für Jugendliche aus Wuppertal genutzt werden soll.'),
('2622521e-6ff6-4b30-895b-ae918e0c9e19', '43f903d7-d977-4ca2-8ea7-fd0b88bf3e70', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Sportlern will gelernt sein', 'So ist es!'),
('af0abfbc-fef8-4a76-95c9-08443a7d4e1b', 'bfa2d7e7-96b3-467d-8f89-2a11dad48dff', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Theatergruppe', 'Wer schon mal Lust auf Theaterimprovisation hatte ist bei uns richtig.'),
('3e5fbd35-5c3c-40c1-b8c4-1e134031dd41', '0e803fad-d6f5-49d0-be34-9d2f8b5a7a5f', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Einsteigerkurs Gitarre', 'Wer Lust hat, kann beim Schnupperkurs mit machen. Wir haben nur begrenzt Gitarren und wer eine hat, darf sie gerne mitbringen'),
('5526b516-94cc-4277-a221-da3e54a8edb1', '03b7a3dd-8754-43d0-bd99-45c3625df096', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Retro zocken', 'Von Mario bis Sonic wollen wir alle Retroklassiker auspacken und zusammen zocken.'),
('1eccd553-46ca-4f41-91b2-5dbff46aa764', 'd2917356-5a38-4426-903e-78607f45b566', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Treffpunkt Religion', 'Für Interessierte ein Treffpunkt zum Austausch'),
('7d30a49b-3b48-4a60-af16-074020fa023b', '91adc0bf-672c-4caf-a9a0-0c59ea29e322', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Kennenlernen', 'Was man eben beim kennen lernen so macht :-)'),
('0f9b09e3-7302-44c7-9c71-f45cd2249a0c', '48ecadc2-e8fb-47ee-bd00-55c7cb842e6f', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Ausflug nach Köln', 'Wir machen einen Ausflug nach Kölle. Achtung, es gibt nur begrenzt Plätze!'),
('3735544c-8a60-4dd5-9128-3e4aefe7a63b', 'c29152f8-4e6f-4d05-8606-4ceef69cd47c', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Wahlparty Jugendratswahl', 'Wir wollen nach der Wahl etwas feiern.'),
('f1db7b86-1b4b-4dac-96c3-508ae878f31e', '7c225870-7cf2-4ce0-98fa-db18b3b6aebc', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Launchparty Wuppngo', 'Kommt vorbei wenn das Wuppngo offiziell online geht'),
('15ea5d51-5317-4cef-bc32-1a6c50a71c2b', '415bed60-1d7f-4562-b920-4011f1a0b1ec', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Stadtrundlauf durch Wuppertal', 'Wir wollen euch Stadtteile und Orte in Wuppertal zeigen, die ihr selbst nicht mal kennt.'),
('8743295e-d842-480b-b02b-764bc4e3f2fc', '4bfb4694-73c9-42b6-949e-fec984dbd7c4', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Chor zum Mitmachen', 'Egal, ob die Stimme gut oder schlecht ist. Wir finden für jeden einen Platz im Chor.'),
('4abd319c-5602-40f6-8d3a-e84210d3008b', '61b42a08-da2e-4a3b-8824-fc3a3cb6e9de', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Chillen und zocken', 'Genau das: chillen und zocken.');

INSERT INTO `activities_target_groups` (`id`, `activity_id`, `target_group_id`) VALUES
('b34373be-b52d-45bb-89b7-a97fe700cf29', '0d44adfc-7b57-43fc-bed0-a5efd8aec6ef', 'f5bc2819-49a9-49f5-ac6f-7f850abece14'),
('4c9d27ad-bd10-4ace-8fc4-a79410fb1522', 'e3088c34-14ca-4034-b2d6-5e821a0fc22d', '82584686-6f67-4cea-963b-2ef15704efe2'),
('d1789770-15bd-4559-b9c2-3de5919574ee', '866a86a3-271f-414d-ba2f-c6ec23b434ef', '43392d8f-77d7-46d1-94c2-ee4c479315b5'),
('c21ae707-2e22-4126-b739-3cd74e109887', 'e41d0798-db07-4064-bcc7-75c4674501e4', 'aafd5b0d-d076-4e9c-93ce-9f066019731b'),
('bbdfab74-7a52-47f3-ab04-77e9c14bafc4', '2a89749e-ba9f-4146-944e-4c1f2f339ea9', '438cb20a-06f2-431a-a40d-0e5eede2f5d7'),
('a2e27f8b-b97c-45c9-8e21-cda32cced4aa', '746ccbd0-8947-42ec-9a99-741768ffcb23', '13291244-2865-4b2b-90a3-4525d30f6076'),
('a145ac1c-7005-43f5-85b5-47987d24e168', '4e1829a7-fb08-419a-8910-781c8ae7cfec', '955376f8-6a35-4a3e-952e-345ce49d48b5'),
('617811df-6622-4389-a2b6-4eec806fa6b1', '20a7c475-c04f-401f-9801-2c8839cf7ead', 'f5bc2819-49a9-49f5-ac6f-7f850abece14'),
('dac63daa-89b2-4f55-8e46-2d6b52febf3a', '8534edaf-f42d-4da2-81e5-5a5917ca95f0', '82584686-6f67-4cea-963b-2ef15704efe2'),
('c971f0dc-9ded-4e46-8ae0-0643c22eb704', '43f903d7-d977-4ca2-8ea7-fd0b88bf3e70', '43392d8f-77d7-46d1-94c2-ee4c479315b5'),
('9319271d-fe60-456c-9828-39b2ee56645b', 'bfa2d7e7-96b3-467d-8f89-2a11dad48dff', 'aafd5b0d-d076-4e9c-93ce-9f066019731b'),
('815b2635-6d84-41fd-93c7-ea2719219148', '0e803fad-d6f5-49d0-be34-9d2f8b5a7a5f', '438cb20a-06f2-431a-a40d-0e5eede2f5d7'),
('7f6dda69-7a47-445b-b10e-e1ce6968b68f', '03b7a3dd-8754-43d0-bd99-45c3625df096', '13291244-2865-4b2b-90a3-4525d30f6076'),
('10d4a0ac-b5ba-48c1-9648-fb484c582481', 'd2917356-5a38-4426-903e-78607f45b566', '955376f8-6a35-4a3e-952e-345ce49d48b5'),
('5950712f-808b-4624-a8e1-4eba687ac20b', '91adc0bf-672c-4caf-a9a0-0c59ea29e322', 'f5bc2819-49a9-49f5-ac6f-7f850abece14'),
('5890e75e-e75e-4ebd-a5b1-5401262c101d', '48ecadc2-e8fb-47ee-bd00-55c7cb842e6f', '82584686-6f67-4cea-963b-2ef15704efe2'),
('1ac34429-8d77-46ed-98dd-6cf94019bd2d', 'c29152f8-4e6f-4d05-8606-4ceef69cd47c', '43392d8f-77d7-46d1-94c2-ee4c479315b5'),
('f4022510-39f1-4cbb-91c1-fc36339a1c2f', '7c225870-7cf2-4ce0-98fa-db18b3b6aebc', 'aafd5b0d-d076-4e9c-93ce-9f066019731b'),
('0918823f-ab8a-4b83-a320-f8c82cd40cd6', '415bed60-1d7f-4562-b920-4011f1a0b1ec', '438cb20a-06f2-431a-a40d-0e5eede2f5d7'),
('876e3b78-462d-4284-8eb0-4f7e483a061a', '4bfb4694-73c9-42b6-949e-fec984dbd7c4', '13291244-2865-4b2b-90a3-4525d30f6076'),
('efd12c5e-7c48-41ed-99a9-73bffee86d59', '61b42a08-da2e-4a3b-8824-fc3a3cb6e9de', '955376f8-6a35-4a3e-952e-345ce49d48b5');

INSERT INTO `schedules` (`id`, `start_date`, `end_date`, `activity_id`) VALUES
('08fd397a-fbb0-41be-a994-3ccbaf1b4ec1', '2099-04-19 13:00:00', '2099-04-19 15:00:00', '0d44adfc-7b57-43fc-bed0-a5efd8aec6ef'),
('5f94b50b-253b-4c90-8a6f-fe29c59ae5ec', '2099-04-20 13:00:00', '2099-04-20 15:00:00', 'e3088c34-14ca-4034-b2d6-5e821a0fc22d'),
('8b2dc4f9-4798-45ed-96e6-e228ba5ce56e', '2099-04-21 13:00:00', '2099-04-21 15:00:00', '866a86a3-271f-414d-ba2f-c6ec23b434ef'),
('5546b92b-b6ef-49b7-86d9-dad8268f188e', '2099-04-21 14:00:00', '2099-04-22 16:00:00', 'e41d0798-db07-4064-bcc7-75c4674501e4'),
('45058383-f608-41e0-bff9-e0be8254ec5a', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '2a89749e-ba9f-4146-944e-4c1f2f339ea9'),
('6bfa6046-e4b4-49b6-bd21-9cf4d606af1e', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '746ccbd0-8947-42ec-9a99-741768ffcb23'),
('375058ef-2482-4e8d-b42c-481ceeda48e9', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '4e1829a7-fb08-419a-8910-781c8ae7cfec'),
('f0882432-9664-4ac9-a300-66abe00165a2', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '20a7c475-c04f-401f-9801-2c8839cf7ead'),
('586bfd4c-447c-41ce-b796-99ab2f5793a6', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '8534edaf-f42d-4da2-81e5-5a5917ca95f0'),
('d21f22cc-c7bf-4bfc-a3d0-5e6117daff82', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '43f903d7-d977-4ca2-8ea7-fd0b88bf3e70'),
('5810bd11-993e-4761-b40f-9bb2de5793d3', '2099-04-21 14:00:00', '2099-04-22 16:00:00', 'bfa2d7e7-96b3-467d-8f89-2a11dad48dff'),
('05aa83a3-45b5-4b49-a0ea-2a71ea1bb135', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '0e803fad-d6f5-49d0-be34-9d2f8b5a7a5f'),
('209a5fbf-eb2b-4813-92b9-6b1dc8192a4a', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '03b7a3dd-8754-43d0-bd99-45c3625df096'),
('3668c7a5-f080-4993-a1fa-bcf872bbbd98', '2099-04-21 14:00:00', '2099-04-22 16:00:00', 'd2917356-5a38-4426-903e-78607f45b566'),
('c897df41-feba-4a68-851f-e833538d2565', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '91adc0bf-672c-4caf-a9a0-0c59ea29e322'),
('01a3ba36-aa54-4645-9b26-d6212e2e543f', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '48ecadc2-e8fb-47ee-bd00-55c7cb842e6f'),
('ab4bd0e4-0ebe-4134-9d0e-f23d694e4f66', '2099-04-21 14:00:00', '2099-04-22 16:00:00', 'c29152f8-4e6f-4d05-8606-4ceef69cd47c'),
('36337f3d-4424-4b71-b6be-2e3cd151818a', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '7c225870-7cf2-4ce0-98fa-db18b3b6aebc'),
('7d2f1008-8e19-42c9-b933-0df58885a188', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '415bed60-1d7f-4562-b920-4011f1a0b1ec'),
('6fa359eb-1f09-4f40-8e58-ef5193bce518', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '4bfb4694-73c9-42b6-949e-fec984dbd7c4'),
('db7369c5-3e6a-4f1c-8b0e-f8f19e707788', '2099-04-21 14:00:00', '2099-04-22 16:00:00', '61b42a08-da2e-4a3b-8824-fc3a3cb6e9de');

INSERT INTO `bloggers`(`id`, `approved`, `user_id`) VALUES
('8e534655-a843-4d60-b9e8-2e1f2ee0ab54', 1, '747acfae-fde7-4767-9920-4b79d9061ebc'),
('0f92532e-02ff-4860-83c3-fbde999ff0bf', 1, '000af454-8535-4788-b353-0177e98651d1');

INSERT INTO `topics` (`id`) VALUES
('f18700d0-e560-4e47-84e7-6f517eaa55a6'),
('7e80eadb-7cfc-4d7e-a7d9-f884d714937b'),
('08188445-798e-44f7-a70f-a675bcc453ca'),
('b6022559-c226-4e45-8b61-dd0f9b8d41d2'),
('0cad66be-4a91-4335-b7da-86756300712c'),
('d8c5d11a-a3a8-4713-8ac6-68cc41aad8d2'),
('c70f2ced-20b5-45d2-b092-1ab2944bf35a');

INSERT INTO `topic_translatables` (`id`, `name`, `language_id`, `parent_id`) VALUES
('9caea1b4-2cbe-43bc-abea-4b9cbc3af653', 'Finanzen', 'b6baaee8-cb61-4921-b432-798b69525fde', 'f18700d0-e560-4e47-84e7-6f517eaa55a6'),
('7f00a8f0-5ac2-45f8-8a31-4839abb8f8a6', 'Familie', 'b6baaee8-cb61-4921-b432-798b69525fde', '7e80eadb-7cfc-4d7e-a7d9-f884d714937b'),
('fb71ddef-1ec0-41ea-962f-b9caa696ed31', 'Partnerschaft', 'b6baaee8-cb61-4921-b432-798b69525fde', '08188445-798e-44f7-a70f-a675bcc453ca'),
('93c64bcd-d468-4fc8-a579-c3fd1748fdbc', 'Arbeit', 'b6baaee8-cb61-4921-b432-798b69525fde', 'b6022559-c226-4e45-8b61-dd0f9b8d41d2'),
('4b208826-6342-4be3-9e73-5bd42d00f17d', 'Bildung', 'b6baaee8-cb61-4921-b432-798b69525fde', '0cad66be-4a91-4335-b7da-86756300712c'),
('104c16b7-e1af-411a-820c-c5c44ad74b78', 'Gesundheit', 'b6baaee8-cb61-4921-b432-798b69525fde', 'd8c5d11a-a3a8-4713-8ac6-68cc41aad8d2'),
('818bcaa1-6809-4f04-8bad-afa6fb72a79e', 'FAQ', 'b6baaee8-cb61-4921-b432-798b69525fde', 'c70f2ced-20b5-45d2-b092-1ab2944bf35a');

INSERT INTO `pages` (`id`, `topic_id`) VALUES
('7becf19c-85ee-4b0b-820e-56fb868f853a', 'f18700d0-e560-4e47-84e7-6f517eaa55a6'),
('ba264bd4-38b1-48c1-9f3f-4554162686dd', '7e80eadb-7cfc-4d7e-a7d9-f884d714937b'),
('67ab0274-49e0-4948-9066-4b55c6aa096b', '08188445-798e-44f7-a70f-a675bcc453ca'),
('6fea17c5-fd06-4e0d-b359-c89331eb5b3a', 'b6022559-c226-4e45-8b61-dd0f9b8d41d2'),
('d78d056e-bfb3-4aaa-a6c6-20dfe8cbe92a', '0cad66be-4a91-4335-b7da-86756300712c'),
('91c4a41e-0968-4a3f-93e2-608caef340dd', 'd8c5d11a-a3a8-4713-8ac6-68cc41aad8d2'),
('9fd9dcb0-03c1-4b0c-b6c6-5e0d5c808be2', 'c70f2ced-20b5-45d2-b092-1ab2944bf35a');

INSERT INTO `page_translatables` (`id`, `title`, `content`, `language_id`, `parent_id`) VALUES
('cdeef097-e9fe-4a41-a233-3dbe5f2beaec', 'Wie spare ich Geld', 'Das wird irgendwann mal ein Text bei dem es ums Geld sparen geht.', 'b6baaee8-cb61-4921-b432-798b69525fde', '7becf19c-85ee-4b0b-820e-56fb868f853a'),
('45f1ad43-d0ff-4244-b2a0-6e7edcdacc55', 'Wissenswertes über Familie', 'Das wird irgendwann mal ein Text bei dem es um Familie geht.', 'b6baaee8-cb61-4921-b432-798b69525fde', 'ba264bd4-38b1-48c1-9f3f-4554162686dd'),
('ba9cdc6d-a12c-4884-a0be-98fad16c5a60', 'Ein Beitrag zu Partnerschaft ', 'Das wird irgendwann mal ein Text bei dem es um Partnerschaft geht.', 'b6baaee8-cb61-4921-b432-798b69525fde', '67ab0274-49e0-4948-9066-4b55c6aa096b'),
('d360420c-d9ba-47b6-bebe-63b75dba6061', 'Ein Beitrag zu Arbeit', 'Das wird irgendwann mal ein Text bei dem es um Arbeit geht.', 'b6baaee8-cb61-4921-b432-798b69525fde', '6fea17c5-fd06-4e0d-b359-c89331eb5b3a'),
('2d944299-035e-4013-9bed-940596d6c68f', 'Ein Beitrag zu Bildung', 'Das wird irgendwann mal ein Text bei dem es um Bildung geht.', 'b6baaee8-cb61-4921-b432-798b69525fde', 'd78d056e-bfb3-4aaa-a6c6-20dfe8cbe92a'),
('bfcf5310-9dd1-4e19-9141-3913ab042926', 'Ein Beitrag zu Gesundheit', 'Das wird irgendwann mal ein Text bei dem es um Gesundheit geht.', 'b6baaee8-cb61-4921-b432-798b69525fde', '91c4a41e-0968-4a3f-93e2-608caef340dd'),
('632db816-0919-4a14-8c3e-5436503dba3e', 'Ein Beitrag zu FAQ', 'Das wird irgendwann mal ein Text bei dem es um FAQ geht.', 'b6baaee8-cb61-4921-b432-798b69525fde', '9fd9dcb0-03c1-4b0c-b6c6-5e0d5c808be2');

INSERT INTO `blogs`(`id`, `activity_id`, `blogger_id`, `likes`) VALUES
('f9bd3e81-88d4-46fc-b7cf-ffd32e39387a', '0d44adfc-7b57-43fc-bed0-a5efd8aec6ef', '8e534655-a843-4d60-b9e8-2e1f2ee0ab54', 26),
('4086ee98-e9f5-4ed2-b15f-840705f3f2ee', null, '8e534655-a843-4d60-b9e8-2e1f2ee0ab54', 74),
('efb8aefb-8d84-45fb-9626-d70d22b5730c', 'e3088c34-14ca-4034-b2d6-5e821a0fc22d', '8e534655-a843-4d60-b9e8-2e1f2ee0ab54', 4),
('40e73d81-6290-4042-a01e-d7116a7908cb', '866a86a3-271f-414d-ba2f-c6ec23b434ef', '8e534655-a843-4d60-b9e8-2e1f2ee0ab54', 136),
('e8b8632f-6042-4523-9b84-ac27e6c0005d', 'e41d0798-db07-4064-bcc7-75c4674501e4', '0f92532e-02ff-4860-83c3-fbde999ff0bf', 54),
('b5ec61e7-c97a-45d9-8e55-f1094979ca99', '2a89749e-ba9f-4146-944e-4c1f2f339ea9', '0f92532e-02ff-4860-83c3-fbde999ff0bf', 17),
('47193be9-8085-4692-bf75-1ffe4d853a4f', '746ccbd0-8947-42ec-9a99-741768ffcb23', '0f92532e-02ff-4860-83c3-fbde999ff0bf', 9);

INSERT INTO `blog_translatables` (`id`, `parent_id`, `language_id`, `title`, `content`) VALUES
('618cd943-93d1-4cf8-8bb6-f43af150f590', 'f9bd3e81-88d4-46fc-b7cf-ffd32e39387a', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Blogbeitrag über Lorem Ispum', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque magna dolor, hendrerit ut venenatis in, iaculis pharetra tellus. Maecenas sit amet auctor nulla. Proin arcu lacus, congue at felis sed, tincidunt accumsan justo. Maecenas fringilla mattis tempor. Proin a metus nec metus vulputate vehicula eu vel quam. Aliquam nec dolor ac odio placerat eleifend. Suspendisse vel felis et mauris porta lacinia. In turpis risus, cursus et nisi et, gravida interdum purus. Nunc ultricies rutrum felis a interdum. Aliquam erat volutpat. Suspendisse mollis libero vel porttitor laoreet. Fusce ullamcorper, neque dignissim tempor consequat, felis tellus consectetur ligula, ut vestibulum neque orci vitae nisl. Aenean tortor sapien, eleifend at lobortis vitae, convallis non magna'),
('2c0eede5-06bd-452d-96dc-f7e071612770', '4086ee98-e9f5-4ed2-b15f-840705f3f2ee', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Ein anderer Beitrag zu Lorem Ipsum', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque magna dolor, hendrerit ut venenatis in, iaculis pharetra tellus. Maecenas sit amet auctor nulla. Proin arcu lacus, congue at felis sed, tincidunt accumsan justo. Maecenas fringilla mattis tempor. Proin a metus nec metus vulputate vehicula eu vel quam. Aliquam nec dolor ac odio placerat eleifend. Suspendisse vel felis et mauris porta lacinia. In turpis risus, cursus et nisi et, gravida interdum purus. Nunc ultricies rutrum felis a interdum. Aliquam erat volutpat. Suspendisse mollis libero vel porttitor laoreet. Fusce ullamcorper, neque dignissim tempor consequat, felis tellus consectetur ligula, ut vestibulum neque orci vitae nisl. Aenean tortor sapien, eleifend at lobortis vitae, convallis non magna'),
('def890e3-36f7-4cb1-9c96-05ea1b1ea5a6', 'efb8aefb-8d84-45fb-9626-d70d22b5730c', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Blogging Blog', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque magna dolor, hendrerit ut venenatis in, iaculis pharetra tellus. Maecenas sit amet auctor nulla. Proin arcu lacus, congue at felis sed, tincidunt accumsan justo. Maecenas fringilla mattis tempor. Proin a metus nec metus vulputate vehicula eu vel quam. Aliquam nec dolor ac odio placerat eleifend. Suspendisse vel felis et mauris porta lacinia. In turpis risus, cursus et nisi et, gravida interdum purus. Nunc ultricies rutrum felis a interdum. Aliquam erat volutpat. Suspendisse mollis libero vel porttitor laoreet. Fusce ullamcorper, neque dignissim tempor consequat, felis tellus consectetur ligula, ut vestibulum neque orci vitae nisl. Aenean tortor sapien, eleifend at lobortis vitae, convallis non magna'),
('bd8cb0ff-db43-46c9-92d9-b64e07b102c1', '40e73d81-6290-4042-a01e-d7116a7908cb', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Ich bin ein Blogtitel', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque magna dolor, hendrerit ut venenatis in, iaculis pharetra tellus. Maecenas sit amet auctor nulla. Proin arcu lacus, congue at felis sed, tincidunt accumsan justo. Maecenas fringilla mattis tempor. Proin a metus nec metus vulputate vehicula eu vel quam. Aliquam nec dolor ac odio placerat eleifend. Suspendisse vel felis et mauris porta lacinia. In turpis risus, cursus et nisi et, gravida interdum purus. Nunc ultricies rutrum felis a interdum. Aliquam erat volutpat. Suspendisse mollis libero vel porttitor laoreet. Fusce ullamcorper, neque dignissim tempor consequat, felis tellus consectetur ligula, ut vestibulum neque orci vitae nisl. Aenean tortor sapien, eleifend at lobortis vitae, convallis non magna'),
('3691e6e1-5512-4e2e-be62-5a6454eacccb', 'e8b8632f-6042-4523-9b84-ac27e6c0005d', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Titel eines Blogs', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque magna dolor, hendrerit ut venenatis in, iaculis pharetra tellus. Maecenas sit amet auctor nulla. Proin arcu lacus, congue at felis sed, tincidunt accumsan justo. Maecenas fringilla mattis tempor. Proin a metus nec metus vulputate vehicula eu vel quam. Aliquam nec dolor ac odio placerat eleifend. Suspendisse vel felis et mauris porta lacinia. In turpis risus, cursus et nisi et, gravida interdum purus. Nunc ultricies rutrum felis a interdum. Aliquam erat volutpat. Suspendisse mollis libero vel porttitor laoreet. Fusce ullamcorper, neque dignissim tempor consequat, felis tellus consectetur ligula, ut vestibulum neque orci vitae nisl. Aenean tortor sapien, eleifend at lobortis vitae, convallis non magna'),
('95eaee79-1140-431d-ba55-55eb144a338a', 'b5ec61e7-c97a-45d9-8e55-f1094979ca99', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Noch ein Blog', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque magna dolor, hendrerit ut venenatis in, iaculis pharetra tellus. Maecenas sit amet auctor nulla. Proin arcu lacus, congue at felis sed, tincidunt accumsan justo. Maecenas fringilla mattis tempor. Proin a metus nec metus vulputate vehicula eu vel quam. Aliquam nec dolor ac odio placerat eleifend. Suspendisse vel felis et mauris porta lacinia. In turpis risus, cursus et nisi et, gravida interdum purus. Nunc ultricies rutrum felis a interdum. Aliquam erat volutpat. Suspendisse mollis libero vel porttitor laoreet. Fusce ullamcorper, neque dignissim tempor consequat, felis tellus consectetur ligula, ut vestibulum neque orci vitae nisl. Aenean tortor sapien, eleifend at lobortis vitae, convallis non magna'),
('89657621-e45a-496e-a14a-a3e56fa9e9d3', '47193be9-8085-4692-bf75-1ffe4d853a4f', 'b6baaee8-cb61-4921-b432-798b69525fde', 'Der letzte Blog', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque magna dolor, hendrerit ut venenatis in, iaculis pharetra tellus. Maecenas sit amet auctor nulla. Proin arcu lacus, congue at felis sed, tincidunt accumsan justo. Maecenas fringilla mattis tempor. Proin a metus nec metus vulputate vehicula eu vel quam. Aliquam nec dolor ac odio placerat eleifend. Suspendisse vel felis et mauris porta lacinia. In turpis risus, cursus et nisi et, gravida interdum purus. Nunc ultricies rutrum felis a interdum. Aliquam erat volutpat. Suspendisse mollis libero vel porttitor laoreet. Fusce ullamcorper, neque dignissim tempor consequat, felis tellus consectetur ligula, ut vestibulum neque orci vitae nisl. Aenean tortor sapien, eleifend at lobortis vitae, convallis non magna');
