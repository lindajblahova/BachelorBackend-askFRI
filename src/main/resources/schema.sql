ALTER TABLE if EXISTS answer DROP CONSTRAINT if EXISTS FK_question_id_question;
ALTER TABLE if EXISTS answered_question DROP CONSTRAINT if EXISTS FK_question_id_question;
ALTER TABLE if EXISTS answered_question DROP CONSTRAINT if EXISTS FK_user_profile_id_user;
ALTER TABLE if EXISTS liked_message DROP CONSTRAINT if EXISTS FK_message_id_message;
ALTER TABLE if EXISTS liked_message DROP CONSTRAINT if EXISTS FK_user_profile_id_user;
ALTER TABLE if EXISTS message DROP CONSTRAINT if EXISTS FK_room_id_room;
ALTER TABLE if EXISTS optional_answer DROP CONSTRAINT if EXISTS FK_question_id_question;
ALTER TABLE if EXISTS question DROP CONSTRAINT if EXISTS FK_room_id_room;
ALTER TABLE if EXISTS room DROP CONSTRAINT if EXISTS FK_user_profile_id_user;

DROP INDEX if EXISTS unique_passcode;
DROP TABLE if EXISTS answer CASCADE ;
DROP TABLE if EXISTS answered_question CASCADE;
DROP TABLE if EXISTS liked_message CASCADE;
DROP TABLE if EXISTS message CASCADE;
DROP TABLE if EXISTS optional_answer CASCADE;
DROP TABLE if EXISTS question CASCADE;
DROP TABLE if EXISTS room CASCADE;
DROP TABLE if EXISTS user_profile CASCADE;

DROP sequence if EXISTS a_id_seq;
DROP sequence if EXISTS m_id_seq;
DROP sequence if EXISTS oa_id_seq;
DROP sequence if EXISTS q_id_seq;
DROP sequence if EXISTS r_id_seq;
DROP sequence if EXISTS u_id_seq;

CREATE sequence a_id_seq start 1 increment 1;
CREATE sequence m_id_seq start 1 increment 1;
CREATE sequence oa_id_seq start 1 increment 1;
CREATE sequence q_id_seq start 1 increment 1;
CREATE sequence r_id_seq start 1 increment 1;
CREATE sequence u_id_seq start 2 increment 1;

CREATE TABLE answer (id_answer int8 NOT NULL, content TEXT NOT NULL, question_id_question int8 NOT NULL, PRIMARY KEY (id_answer));
CREATE TABLE answered_question (question_id_question int8 NOT NULL, user_profile_id_user int8 NOT NULL, PRIMARY KEY (question_id_question, user_profile_id_user));
CREATE TABLE liked_message (message_id_message int8 NOT NULL, user_profile_id_user int8 NOT NULL, PRIMARY KEY (message_id_message, user_profile_id_user));
CREATE TABLE message (id_message int8 NOT NULL, content TEXT NOT NULL, room_id_room int8 NOT NULL, PRIMARY KEY (id_message));
CREATE TABLE optional_answer (id_optional_answer int8 NOT NULL, content TEXT NOT NULL, question_id_question int8 NOT NULL, PRIMARY KEY (id_optional_answer));
CREATE TABLE question (id_question int8 NOT NULL, answers_displayed BOOLEAN NOT NULL, content TEXT NOT NULL, question_displayed BOOLEAN NOT NULL, type INT NOT NULL, room_id_room int8 NOT NULL, PRIMARY KEY (id_question));
CREATE TABLE room (id_room int8 NOT NULL, active boolean NOT NULL, room_name TEXT NOT NULL, room_passcode TEXT NOT NULL, user_profile_id_user int8 NOT NULL, PRIMARY KEY (id_room));
CREATE TABLE user_profile (id_user int8 NOT NULL, email TEXT NOT NULL, firstname TEXT NOT NULL, password VARCHAR(60) NOT NULL, role VARCHAR(10) NOT NULL, surname TEXT NOT NULL, PRIMARY KEY (id_user));

ALTER TABLE if EXISTS user_profile ADD CONSTRAINT user_email_unique UNIQUE (email);
ALTER TABLE if EXISTS answer ADD CONSTRAINT FK_question_id_question FOREIGN KEY (question_id_question) REFERENCES question ON DELETE CASCADE ;
ALTER TABLE if EXISTS answered_question ADD CONSTRAINT FK_question_id_question FOREIGN KEY (question_id_question) REFERENCES question ON DELETE CASCADE;
ALTER TABLE if EXISTS answered_question ADD CONSTRAINT FK_user_profile_id_user FOREIGN KEY (user_profile_id_user) REFERENCES user_profile ON DELETE CASCADE;
ALTER TABLE if EXISTS liked_message ADD CONSTRAINT FK_message_id_message FOREIGN KEY (message_id_message) REFERENCES message ON DELETE CASCADE;
ALTER TABLE if EXISTS liked_message ADD CONSTRAINT FK_user_profile_id_user FOREIGN KEY (user_profile_id_user) REFERENCES user_profile ON DELETE CASCADE;
ALTER TABLE if EXISTS message ADD CONSTRAINT FK_room_id_room FOREIGN KEY (room_id_room) REFERENCES room ON DELETE CASCADE;
ALTER TABLE if EXISTS optional_answer ADD CONSTRAINT FK_question_id_question FOREIGN KEY (question_id_question) REFERENCES question ON DELETE CASCADE;
ALTER TABLE if EXISTS question ADD CONSTRAINT FK_room_id_room FOREIGN KEY (room_id_room) REFERENCES room ON DELETE CASCADE;
ALTER TABLE if EXISTS room ADD CONSTRAINT FK_user_profile_id_user FOREIGN KEY (user_profile_id_user) REFERENCES user_profile ON DELETE CASCADE;

CREATE UNIQUE INDEX unique_passcode ON room (room_passcode,active) where (active);
