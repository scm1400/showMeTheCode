call next value for hibernate_sequence;
insert into user(id, created_at, updated_at, answer_count, eval_count, eval_total, password, role, username)
values (1, current_timestamp, current_timestamp, 0, 0, 0, 1, 'ROLE_USER', 'user1');
call next value for hibernate_sequence;
insert into user(id, created_at, updated_at, answer_count, eval_count, eval_total, password, role, username)
values (2, current_timestamp, current_timestamp, 0, 0, 0, 1, 'ROLE_USER', 'user2');
call next value for hibernate_sequence;
insert into user(id, created_at, updated_at, answer_count, eval_count, eval_total, password, role, username)
values (3, current_timestamp, current_timestamp, 0, 0, 0, 1, 'ROLE_REVIEWER', 'JavaGod');
call next value for hibernate_sequence;
insert into user(id, created_at, updated_at, answer_count, eval_count, eval_total, password, role, username)
values (4, current_timestamp, current_timestamp, 0, 0, 0, 1, 'ROLE_REVIEWER', 'PythonGod');
call next value for hibernate_sequence;

insert into language(id, created_at, updated_at, name, user_id)
values (1, current_timestamp, current_timestamp, 'JAVA', 3);
insert into language(id, created_at, updated_at, name, user_id)
values(2, current_timestamp, current_timestamp, 'PYTHON', 4);

insert into review_request(id, created_at, updated_at, content, language_name, status, title, answer_user_id,request_user_id)
values (1, current_timestamp, current_timestamp, '테스트내용', 'java', 'REQUESTED', '테스트제목', 2, 1);

insert into review_answer(id, created_at, updated_at, content, point, title, answer_user_id, review_request_id)
values (1, current_timestamp, current_timestamp, '테스트내용', 5, '테스트답변제목', 2, 1);

update review_request set review_answer_id = 1 where id = 1;

insert into review_request_comment(id, created_at, updated_at, content,  review_request_id, user_id)
values (1, current_timestamp, current_timestamp, '테스트댓글내용', 1, 1);