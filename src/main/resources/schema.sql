
DROP TABLE IF EXISTS PUBLIC.USERS cascade;
DROP TABLE IF EXISTS PUBLIC.ITEMS cascade;
DROP TABLE IF EXISTS PUBLIC.BOOKINGS cascade;
DROP TABLE IF EXISTS PUBLIC.COMMENTS cascade;

CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
                                id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                name VARCHAR(45) NOT NULL,
                                email VARCHAR(512) NOT NULL,
                                CONSTRAINT users_pk_user PRIMARY KEY (id),
                                CONSTRAINT uq_user_email UNIQUE (email)
                                );

CREATE TABLE IF NOT EXISTS PUBLIC.ITEMS (
                                id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                               	name CHARACTER(45) NOT NULL,
                               	description CHARACTER(255),
                               	available BOOLEAN,
                                owner_id BIGINT,
                                request_id BIGINT,
                                CONSTRAINT items_pk_item PRIMARY KEY (id),
                                CONSTRAINT items_fk_owner_id FOREIGN KEY (owner_id) REFERENCES PUBLIC.USERS(id) ON DELETE SET NULL
                               );

CREATE TABLE IF NOT EXISTS PUBLIC.BOOKINGS (
                                id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                start_date TIMESTAMP,
                                end_date TIMESTAMP,
                                creation_date TIMESTAMP,
                                item_id BIGINT,
                                booker_id BIGINT,
                                status CHARACTER(10),
                                state CHARACTER(10),
                                CONSTRAINT bookings_pk_item PRIMARY KEY (id),
                                CONSTRAINT bookings_fk_item_id FOREIGN KEY (item_id) REFERENCES PUBLIC.ITEMS(id) ON DELETE SET NULL,
                                CONSTRAINT bookings_fk_booker_id FOREIGN KEY (booker_id) REFERENCES PUBLIC.USERS(id) ON DELETE SET NULL
                               );

CREATE TABLE IF NOT EXISTS PUBLIC.COMMENTS (
                                id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                               	text CHARACTER(255) NOT NULL,
                               	item_id BIGINT,
                                author_id BIGINT,
                                created TIMESTAMP,
                                CONSTRAINT comments_pk_comment PRIMARY KEY (id),
                                CONSTRAINT comments_fk_item_id FOREIGN KEY (item_id) REFERENCES PUBLIC.ITEMS(id) ON DELETE CASCADE,
                                CONSTRAINT comments_fk_author_id FOREIGN KEY (author_id) REFERENCES PUBLIC.USERS(id) ON DELETE SET NULL
                               );
