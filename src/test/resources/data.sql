insert into user_table (user_seq, user_id,username, password, email, email_verified_yn, provider_type, role_type, job, profile_image_url )values
    ( 1,'testId1', 'testUser1','password','projectMasterTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    ( 2,'testId2', 'testUser2','password','teamMemberTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    (11,'testId11', 'testUser2','password','keyResultTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    (12,'testId12', 'testUser2','password','initiativeTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
    (13,'testId13', 'testUser2','password','projectMasterRetrieveTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
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
(99999, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', '2022-12-14', '프로젝트 1 멀티 프로젝트', '프로젝트 objective 1', 0.0, 'mst_Kiwqnp1Nq6lb4256', '2022-12-07', 'SINGLE'),
(99998, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', '2022-12-14', '팀 맴버 테스트용 프로젝트', '프로젝트 objective 2', 0.0, 'mst_Kiwqnp1Nq6lbTNn0', '2022-12-07', 'SINGLE'),
(99997, '2000-12-12', '2023-12-12', 'testUser1', 'testUser1', '2023-12-14', 'key result 테스트용 프로젝트', '프로젝트 objective 3', 0.0, 'mst_Kiwqnp1Nq6lb6421', '2000-12-12', 'TEAM'),
(99996, '2000-12-12', '2023-12-12', 'testUser1', 'testUser1', '2023-12-14', 'Initiative 테스트용 프로젝트', '프로젝트 objective 4', 0.0, 'mst_K4e8a5s7d6lb6421', '2000-12-12', 'TEAM'),
(99995, '2000-12-12', '2023-12-12', 'testUser1', 'testUser1', '2023-12-14', '프로젝트 조회 테스트용 프로젝트', '프로젝트 objective 4', 0.0, 'mst_K42334fffrgg6421', '2000-12-12', 'TEAM'),
(99994, '2001-12-12', '2023-12-12', 'testUser1', 'testUser1', '2023-12-10', '프로젝트 조회 테스트용 프로젝트(프로젝트 완료)', '프로젝트 objective 4', 100.0, 'mst_K42334fffrgg6421', '2000-12-12', 'TEAM'),
(99993, '2002-12-12', '2023-12-12', 'testUser1', 'testUser1', '2033-12-12', '프로젝트 조회 테스트용 프로젝트(프로젝트 70)', '프로젝트 objective 4', 70.0, 'mst_K42334fffrgg6421', '2000-12-12', 'TEAM'),
(99992, '2003-12-12', '2023-12-12', 'testUser1', 'testUser1', '2003-12-12', '프로젝트 조회 테스트용 프로젝트(프로젝트 60)', '프로젝트 objective 4', 60.0, 'mst_K42334fffrgg6421', '2000-12-12', 'TEAM'),
(99991, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2004-12-12', '프로젝트 조회 테스트용 프로젝트(프로젝트 완료2)', '프로젝트 objective 4', 100.0, 'mst_K42334fffrgg6421', '2000-12-12', 'TEAM')
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
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'MEMBER', 99995, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'LEADER', 99994, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99994, 2),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99994, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99993, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'MEMBER', 99992, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99992, 9),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99992, 8),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99991, 13)
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
(99991, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_3235gggrtQ25zQMs', 'testKeyResult 9', 99992)

;
