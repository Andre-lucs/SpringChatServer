CREATE TABLE CHAT_USERS (
  username varchar PRIMARY KEY,
  password varchar,
  online bool
);

CREATE TABLE private_rooms (
  id long auto_increment PRIMARY KEY,
  user1 varchar,
  user2 varchar
);

CREATE TABLE private_rooms_messages (
  id LONG auto_increment PRIMARY KEY,
  room_id long,
  sender_id varchar,
  reciever_id varchar,
  message_body text,
  moment datetime
);

CREATE TABLE public_rooms (
  room_name varchar PRIMARY KEY
);

CREATE TABLE public_rooms_users (
    id long auto_increment PRIMARY KEY,
    room_id varchar,
    user_id varchar,
    last_seen datetime
);

CREATE TABLE public_room_messages (
  id LONG auto_increment primary key,
  room_id varchar,
  sender_id varchar,
  message_body text,
  moment datetime
);

ALTER TABLE private_rooms ADD FOREIGN KEY (user1) REFERENCES CHAT_USERS (username);

ALTER TABLE private_rooms ADD FOREIGN KEY (user2) REFERENCES CHAT_USERS (username);

ALTER TABLE private_rooms_messages ADD FOREIGN KEY (room_id) REFERENCES private_rooms (id);

ALTER TABLE private_rooms_messages ADD FOREIGN KEY (sender_id) REFERENCES CHAT_USERS (username);

ALTER TABLE private_rooms_messages ADD FOREIGN KEY (reciever_id) REFERENCES CHAT_USERS (username);

ALTER TABLE public_room_messages ADD FOREIGN KEY (room_id) REFERENCES public_rooms (room_name);

ALTER TABLE public_room_messages ADD FOREIGN KEY (sender_id) REFERENCES CHAT_USERS (username);

ALTER TABLE public_rooms_users ADD FOREIGN KEY (room_id) REFERENCES public_rooms (room_name);

ALTER TABLE public_rooms_users ADD FOREIGN KEY (user_id) REFERENCES CHAT_USERS (username);