insert into user_table (user_seq, user_id,username, password, email, email_verified_yn, provider_type, role_type, job  )values
    ( 1,'testId1', 'testUser1','password','projectMasterTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP' ),
    ( 2,'testId2', 'testUser2','password','teamMemberTest@naver.com','Y','GOOGLE','USER','PRODUCER_CP' ),
    ( 3,'testId3', 'testUser3','password','user1@naver.com','Y','GOOGLE','USER','PRODUCER_CP' ),
    ( 4,'testId4', 'testUser4','password','user2@naver.com','Y','GOOGLE','USER','PRODUCER_CP' ),
    ( 5,'testId5', 'testUser5','password','user3@naver.com','Y','GOOGLE','USER','PRODUCER_CP' ),
    ( 6,'testId6', 'testUser6','password','user4@naver.com','Y','GOOGLE','USER','PRODUCER_CP' ),
    ( 7,'testId7', 'testUser7','password','user5@naver.com','Y','GOOGLE','USER','PRODUCER_CP' ),
    ( 8,'testId8', 'testUser8','password','user6@naver.com','Y','GOOGLE','USER','PRODUCER_CP' ),
    ( 9,'testId9', 'testUser9','password','user7@naver.com','Y','GOOGLE','USER','PRODUCER_CP' ),
    ( 10,'testId10', 'testUser10','password','user2222@naver.com','Y','GOOGLE','USER','PRODUCER_CP' );

insert into project_master
(project_id, created_date, last_modified_date, created_by, last_modified_by, project_edt, project_name, project_objective, progress, project_master_token, project_sdt, project_type) values
(99999, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', '2022-12-14', '프로젝트 1 멀티 프로젝트', '프로젝트 objective 1', 0.0, 'mst_Kiwqnp1Nq6lb4256', '2022-12-07', 'SINGLE'),
(99998, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', '2022-12-14', '팀 맴버 테스트용 프로젝트', '프로젝트 objective 2', 0.0, 'mst_Kiwqnp1Nq6lbTNn0', '2022-12-07', 'SINGLE'),
(99997, '2000-12-12', '2023-12-12', 'testUser1', 'testUser1', '2023-12-14', '프로젝트 2 멀티 프로젝트', '프로젝트 objective 3', 0.0, 'mst_Kiwqnp1Nq6lb6421', '2000-12-12', 'TEAM')
;


insert into team_member
(created_date, last_modified_date, created_by, last_modified_by, is_new, project_role_type, project_id, user_seq) values
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99998, 2),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99998, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99998, 4);