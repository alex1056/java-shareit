/*
-- users
INSERT INTO public.USERS ( NAME, EMAIL) VALUES ('user1', 'user1@mail.ru');
INSERT INTO public.USERS ( NAME, EMAIL) VALUES ('user2', 'user2@mail.ru');
INSERT INTO public.USERS ( NAME, EMAIL) VALUES ('user3', 'user3@mail.ru');
-- items
INSERT INTO public.ITEMS ( NAME, DESCRIPTION, AVAILABLE, OWNER_ID) VALUES ('Вещь 1', 'Описание вещи 1', true, 1);
INSERT INTO public.ITEMS ( NAME, DESCRIPTION, AVAILABLE, OWNER_ID) VALUES ('Вещь 2', 'Описание вещи 2', true, 2);
INSERT INTO public.ITEMS ( NAME, DESCRIPTION, AVAILABLE, OWNER_ID) VALUES ('Вещь 3', 'Описание вещи 3', true, 3);
INSERT INTO public.ITEMS ( NAME, DESCRIPTION, AVAILABLE, OWNER_ID) VALUES ('Вещь 4', 'Описание вещи 4', true, 3);
INSERT INTO public.ITEMS ( NAME, DESCRIPTION, AVAILABLE, OWNER_ID) VALUES ('Вещь 5', 'Описание вещи 5', true, 3);

--bookings
INSERT INTO public.BOOKINGS ( START_DATE, END_DATE, CREATION_DATE, ITEM_ID, BOOKER_ID, STATUS) VALUES (
'2023-04-05T17:12:44','2023-04-05T17:12:45','2023-04-05T17:12:45',2,1,'WAITING');
INSERT INTO public.BOOKINGS ( START_DATE, END_DATE, CREATION_DATE, ITEM_ID, BOOKER_ID, STATUS) VALUES (
'2023-04-05T17:12:44','2023-04-06T17:12:45','2023-04-06T17:12:45',3,1,'WAITING');
INSERT INTO public.BOOKINGS ( START_DATE, END_DATE, CREATION_DATE, ITEM_ID, BOOKER_ID, STATUS) VALUES (
'2023-04-05T17:12:44','2023-04-07T17:12:45','2023-04-07T17:12:45',3,1,'WAITING');
INSERT INTO public.BOOKINGS ( START_DATE, END_DATE, CREATION_DATE, ITEM_ID, BOOKER_ID, STATUS) VALUES (
'2023-04-05T17:12:44','2023-04-07T18:12:45','2023-04-07T17:12:45',3,1,'WAITING');
INSERT INTO public.BOOKINGS ( START_DATE, END_DATE, CREATION_DATE, ITEM_ID, BOOKER_ID, STATUS) VALUES (
'2023-04-05T17:12:44','2023-04-08T17:12:45','2023-04-016T17:12:45',3,1,'WAITING');
INSERT INTO public.BOOKINGS ( START_DATE, END_DATE, CREATION_DATE, ITEM_ID, BOOKER_ID, STATUS) VALUES (
'2023-04-08T17:12:44','2023-04-09T17:12:45','2023-04-016T17:12:45',3,1,'WAITING');
INSERT INTO public.BOOKINGS ( START_DATE, END_DATE, CREATION_DATE, ITEM_ID, BOOKER_ID, STATUS) VALUES (
'2023-04-09T10:00:00','2023-04-10T10:00:00','2023-04-016T17:12:45',3,1,'WAITING');
INSERT INTO public.BOOKINGS ( START_DATE, END_DATE, CREATION_DATE, ITEM_ID, BOOKER_ID, STATUS) VALUES (
'2023-04-05T17:12:44','2023-04-05T17:12:45','2023-04-07T10:12:45', 4,2,'WAITING');
INSERT INTO public.BOOKINGS ( START_DATE, END_DATE, CREATION_DATE, ITEM_ID, BOOKER_ID, STATUS) VALUES (
'2023-04-07T07:00:00','2023-04-08T08:00:00','2023-04-07T10:30:45',5,2,'WAITING');
INSERT INTO public.BOOKINGS ( START_DATE, END_DATE, CREATION_DATE, ITEM_ID, BOOKER_ID, STATUS) VALUES (
'2023-04-15T17:12:44','2023-04-16T17:12:45','2023-04-07T10:30:45',5,2,'WAITING');
INSERT INTO public.BOOKINGS ( START_DATE, END_DATE, CREATION_DATE, ITEM_ID, BOOKER_ID, STATUS) VALUES (
'2023-04-10T10:00:00','2023-04-11T11:00:00','2023-04-07T10:30:45',5,3,'WAITING');
--comments
INSERT INTO public.COMMENTS ( TEXT, ITEM_ID, AUTHOR_ID) VALUES ('Коммент для вещи 3', 3, 3);
INSERT INTO public.COMMENTS ( TEXT, ITEM_ID, AUTHOR_ID) VALUES ('Еще коммент для вещи 3', 3, 1);
*/