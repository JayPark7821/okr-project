insert into user_table (user_seq, user_id,username, password, email, email_verified_yn, provider_type, role_type, job, profile_image_url )values
    ( 1,'testId1', 'testUser1','password','projectMasterTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    ( 2,'testId2', 'testUser2','password','teamMemberTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    (11,'testId11', 'testUser2','password','keyResultTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    (12,'testId12', 'testUser2','password','initiativeTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    (13,'testId13', 'testUser2','password','projectMasterRetrieveTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    (14,'testId14', 'testUser2','password','projectCalendarTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    (15,'testId15', 'testUser2','password','initiativeRetrieveTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    (16,'testId16', 'testUser2','password','notificationTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    (17,'testId17', 'testUser2','password','feedbackTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    ( 3,'testId3', 'testUser3','password','user1@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    ( 4,'testId4', 'testUser4','password','user2@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    ( 5,'testId5', 'testUser5','password','user3@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    ( 6,'testId6', 'testUser6','password','user4@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    ( 7,'testId7', 'testUser7','password','user5@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    ( 8,'testId8', 'testUser8','password','user6@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    ( 9,'testId9', 'testUser9','password','user7@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    ( 10,'testId10', 'testUser10','password','user2222@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' );

insert into project_master
(project_id, created_date, last_modified_date, created_by, last_modified_by, project_edt, project_name, project_objective, progress, project_master_token, project_sdt, project_type) values
(99999, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', '2022-12-14', '???????????? 1 ?????? ????????????', '???????????? objective 1', 0.0, 'mst_Kiwqnp1Nq6lb4256', '2022-12-07', 'SINGLE'),
(99998, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', '2022-12-14', '??? ?????? ???????????? ????????????', '???????????? objective 2', 0.0, 'mst_Kiwqnp1Nq6lbTNn0', '2022-12-07', 'SINGLE'),
(99997, '2000-12-12', '2023-12-12', 'testUser1', 'testUser1', '2023-12-14', 'key result ???????????? ????????????', '???????????? objective 3', 0.0, 'mst_Kiwqnp1Nq6lb6421', '2000-12-12', 'TEAM'),
(99996, '2000-12-12', '2023-12-12', 'testUser1', 'testUser1', '2023-12-14', 'Initiative ???????????? ????????????', '???????????? objective 4', 11.0, 'mst_K4e8a5s7d6lb6421', '2000-12-12', 'TEAM'),
(99995, '2000-12-12', '2023-12-12', 'testUser1', 'testUser1', '2023-12-14', '???????????? ?????? ???????????? ????????????', '???????????? objective 4', 0.0, 'mst_K4232g4g5rgg6421', '2000-12-12', 'TEAM'),
(99994, '2001-12-12', '2023-12-12', 'testUser1', 'testUser1', '2023-12-10', '???????????? ?????? ???????????? ????????????(???????????? ??????)', '???????????? objective 4', 100.0, 'mst_as3fg34tgg6421', '2000-12-12', 'TEAM'),
(99993, '2002-12-12', '2023-12-12', 'testUser1', 'testUser1', '2033-12-12', '???????????? ?????? ???????????? ????????????(???????????? 70)', '???????????? objective 4', 70.0, 'mst_3gbyy554frgg6421', '2000-12-12', 'TEAM'),
(99992, '2003-12-12', '2023-12-12', 'testUser1', 'testUser1', '2003-12-12', '???????????? ?????? ???????????? ????????????(???????????? 60)', '???????????? objective 4', 60.0, 'mst_K4g4tfdaergg6421', '2000-12-12', 'TEAM'),
(99991, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2004-12-12', '???????????? ?????? ???????????? ????????????(???????????? ??????2)', '???????????? objective 4', 100.0, 'mst_K42334fffrgg6421', '2000-12-12', 'TEAM'),
(99990, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-10-01', '???????????? for ?????? 1', '???????????? objective ?????? 1', 100.0, 'mst_K42334fffrgg6421', '2022-01-12', 'TEAM'),
(99989, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-11-12', '???????????? for ?????? 2', '???????????? objective ?????? 2', 100.0, 'mst_K42h45dfd4gg6421', '2022-01-01', 'TEAM'),
(99988, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-09-12', '???????????? for ?????? 3', '???????????? objective ?????? 3', 100.0, 'mst_ff4g34fffrgg6421', '2022-01-12', 'TEAM'),
(99987, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-12-12', '???????????? for ?????? 4', '???????????? objective ?????? 4', 100.0, 'mst_aa344tg5dfgg6421', '2022-01-12', 'TEAM'),
(99986, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '???????????? for ?????? 5', '???????????? objective ?????? 5', 100.0, 'mst_qq2f4gbfeefe1gg6421', '2022-01-12', 'TEAM'),
(88888, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '???????????? for initiative 1', '???????????? objective initiative ', 100.0, 'mst_qq2f4gbffrgg6421', '2022-01-12', 'TEAM'),
(88887, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '???????????? for initiative 2', '???????????? objective initiative ', 100.0, 'mst_qsdzcxbffrgg6421', '2022-01-12', 'TEAM'),
(88886, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '???????????? for initiative 3', '???????????? objective initiative ', 100.0, 'mst_qq2aaaaaaagg6421', '2022-01-12', 'TEAM'),
(88885, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '???????????? for initiative 4', '???????????? objective initiative ', 100.0, 'mst_qq2f4gbfffffe421', '2022-01-12', 'TEAM'),
(88884, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '???????????? for initiative 5', '???????????? objective initiative ', 100.0, 'mst_qq2f4gaawdeg6421', '2022-01-12', 'TEAM')
;


insert into team_member
(created_date, last_modified_date, created_by, last_modified_by, is_new, project_role_type, project_id, user_seq) values
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99998, 2),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99998, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99998, 4),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99997, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99997, 11),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99996, 12),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99996, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99996, 17),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'MEMBER', 99995, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'LEADER', 99994, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99994, 2),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99994, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99993, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'MEMBER', 99992, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99992, 9),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99992, 8),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99991, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99990, 14),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99989, 14),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99988, 14),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99987, 14),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99986, 14),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88888, 15),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88888, 9),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88888, 16),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88887, 15),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88886, 15),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88885, 15),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88884, 15),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88884, 7),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88888, 17)
;


insert into key_result
(key_result_id, created_date, last_modified_date, created_by, last_modified_by, key_result_token, key_result_name, project_id) values
(99999, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6MX15WQ3DTzQMs', 'testKeyResult 1', 99999),
(99998, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6f45vWQ3DTzQMs', 'testKeyResult 2', 99999),
(99997, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6h12vWQ3DTzQMs', 'testKeyResult 3', 99999),
(99996, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_2236f4vWQ3DTzQMs', 'testKeyResult 4', 99998),
(99995, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_3f46MX55WQ25zQMs', 'testKeyResult 5', 99998),
(99994, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_325fdggrtQ25zQMs', 'testKeyResult 6', 99996),
(99993, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_32hhuggrtQ25zQMs', 'testKeyResult 7', 99992),
(99992, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_325fd23ftQ25zQMs', 'testKeyResult 8', 99992),
(99991, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_3235gggrtQ25zQMs', 'testKeyResult 9', 99991),
(99990, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_aaaaaaaaeeQ25zQMs', 'testKeyResult 10', 99990),
(99989, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_fghjbuiiitQ25zQMs', 'testKeyResult 11', 99989),
(99988, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_guyopuyjytQ25zQMs', 'testKeyResult 12', 99988),
(99987, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_243tgasw3tQ25zQMs', 'testKeyResult 13', 99987),
(99986, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_t433t4g24tgffedsg', 'testKeyResult 14', 99986),
(88888, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_t433t43fdQ25fzQMs', 'testKeyResult 14', 88888),
(88887, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_t43awefd2asazfzQMs', 'testKeyResult 14', 88888),
(88885, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_t43awef23rfdccQMs', 'testKeyResult 14', 88887),
(88884, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_t433t43fg5r43yfzQMs', 'testKeyResult 14', 88886),
(88883, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_t433t4345yfggfxxs', 'testKeyResult 14', 88885),
(88882, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_t433t43fdQxxxxxMs', 'testKeyResult 14', 88884)
;

insert into initiative
(initiative_id, created_date, last_modified_date, created_by, last_modified_by, initiative_detail, initiative_done, initiative_edt, initiative_token, key_result_id, initiative_name, initiative_sdt, project_id, user_seq) values
(99999, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-14', 'ini_ixYjj5nODqtb3AH8', 88888, 'ini name', '2000-12-12', 88888, 15),
(99998, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail2', true, '2023-12-14', 'ini_ix324gfODqtb3AH8', 88888, 'ini name', '2000-12-12', 88888, 9),
(99997, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail3', true, '2023-12-14', 'ini_g43gfd4ODqtb3AH8', 88887, 'ini name', '2000-12-12', 88888, 15),
(99996, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail4', false, '2023-12-14', 'ini_ixYjj5F43frfdAH8', 88885, 'ini name', '2000-12-12', 88887, 15),
(99995, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail5', false, '2023-12-14', 'ini_f43qerfdgbffhAH8', 88884, 'ini name', '2000-12-12', 88886, 15),
(99994, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail6', false, '2027-11-14', 'ini_ixYjj5nOF432wrH8', 88883, 'ini name', '2000-12-12', 88885, 15),
(99993, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail7', false, '2026-12-14', 'ini_ixYjj5nODqgrg431', 88882, 'ini name', '2000-12-12', 88884, 15),
(99992, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail7', true, '2026-12-14', 'ini_ixYjj5nOD22222221', 99994, 'ini name', '2000-12-12', 99996, 7),
(99991, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative test', true, '2026-12-14', 'ini_ixYjj5nOD2233333331', 99994, 'ini name', '2000-12-12', 99996, 17)
;


insert into notification (notification_id, created_date, last_modified_date, checked, message, notification_token, type, user_seq) values
(9999, '2022-12-17', '2022-12-16', 'NEW', '<>?????? <> ????????? ???????????????!????????', 'noti_111fey1SERx', 'NEW_TEAM_MATE', 16),
(9998, '2022-12-16', '2022-12-16', 'DELETED', '<>?????? <> ????????? ???????????????!????????', 'noti_e2222y1SERx', 'NEW_TEAM_MATE', 16),
(9997, '2022-12-18', '2022-12-16', 'NEW', '<>?????? <> ????????? ???????????????!????????', 'noti_aaaaaMoZey1SERx', 'NEW_TEAM_MATE', 16),
(9996, '2022-12-19', '2022-12-16', 'DELETED', '<>?????? <> ????????? ???????????????!????????', 'noti_e3eeddoZey1SERx', 'NEW_TEAM_MATE', 16),
(9995, '2022-12-20', '2022-12-16', 'CHECKED', '<>?????? <> ????????? ???????????????!????????', 'noti_e144441Zey1SERx', 'NEW_TEAM_MATE', 16),
(9994, '2022-12-21', '2022-12-16', 'NEW', '<>?????? <> ????????? ???????????????!????????', 'noti_eK5555535ey1SERx', 'NEW_TEAM_MATE', 16),
(9993, '2022-12-22', '2022-12-16', 'NEW', '<>?????? <> ????????? ???????????????!????????', 'noti_6666yhger5tx', 'NEW_TEAM_MATE', 16);


insert into feedback (feedback_id, created_date, last_modified_date, created_by, last_modified_by, feedback_token, grade_mark, project_initiative_id, checked, opinion, project_id, user_seq) values
(9999, '2022-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_el6awefwaeyWx39', 'BEST_RESULT', 99999, false, '?????? ?????????!!', 88888, 17),
(9998, '2022-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_el61234f23fWx39', 'BEST_RESULT', 99999, false, '?????? ?????????!!', 88888, 17),
(9997, '2022-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_el612awefdx3239', 'BEST_RESULT', 99999, false, '?????? ?????????!!', 88888, 17),
(9996, '2022-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_el6adsff3fffff9', 'BEST_RESULT', 99999, false, '?????? ?????????!!', 88888, 17),
(9995, '2022-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_el6awe4gx39rrrr', 'BEST_RESULT', 99999, false, '?????? ?????????!!', 88888, 17),
(9994, '2022-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_el612grerfcvds9', 'BEST_RESULT', 99999, false, '?????? ?????????!!', 88888, 17),
(9993, '2022-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_el612XNk3rSyWx9', 'BEST_RESULT', 99999, false, '?????? ?????????!!', 88888, 17),
(9992, '2022-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_el6q34zazzSyWx9', 'BEST_RESULT', 99998, false, '?????? ?????????!!', 88888, 17),
(9991, '2022-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_aaaaaaaaaaayWx9', 'BEST_RESULT', 99998, false, '?????? ?????????!!', 88888, 16),
(9990, '2022-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_aaa333333aayWx9', 'BEST_RESULT', 99997, false, '?????? ?????????!!', 88888, 15)
;


