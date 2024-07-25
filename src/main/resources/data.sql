-- CHAT_USERS
INSERT INTO CHAT_USERS (username, password, online) VALUES ('john_doe', 'password123', true);
INSERT INTO CHAT_USERS (username, password, online) VALUES ('jane_smith', 'secure_password', false);
insert into chat_users (username, password, online) values ('test', 'test', false);

-- Private Rooms
INSERT INTO private_rooms (user1, user2) VALUES ('john_doe', 'jane_smith');

-- Private Rooms Messages
INSERT INTO private_rooms_messages (room_id, sender_id, reciever_id, message_body, moment) VALUES (1, 'john_doe', 'jane_smith', 'Hey Jane, how are you?', '2022-01-01 12:00:00');
INSERT INTO private_rooms_messages (room_id, sender_id, reciever_id, message_body, moment) VALUES (1, 'john_doe', 'jane_smith', 'I am doing great, how about you?', '2022-01-01 12:15:00');
INSERT INTO private_rooms_messages (room_id, sender_id, reciever_id, message_body, moment) VALUES (1, 'jane_smith', 'john_doe', 'I am good too, thanks for asking!', '2022-01-01 12:20:00');

-- Public Rooms
INSERT INTO public_rooms (room_name) VALUES ('general_chat');

-- Public Room Messages
INSERT INTO public_room_messages (room_id, sender_id, message_body, moment) VALUES ('general_chat', 'john_doe',  'Good morning everyone!', '2022-01-01 13:00:00');
INSERT INTO public_room_messages (room_id, sender_id, message_body, moment) VALUES ('general_chat', 'jane_smith',  'Good afternoon everyone!', '2022-01-01 14:00:00');
INSERT INTO public_room_messages (room_id, sender_id, message_body, moment) VALUES ('general_chat', 'john_doe', 'Good evening Jane, how was your day?', '2022-01-01 18:00:00');
