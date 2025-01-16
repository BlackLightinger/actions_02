--DROP SCHEMA public CASCADE;
--CREATE SCHEMA public;


CREATE TABLE IF NOT EXISTS users
(
    id serial primary key,
    username text,
    email text,
    password_hash text,
    role text,
    registration_date date,
    last_login date
);

CREATE TABLE IF NOT EXISTS article
(
    id serial primary key,
    title text,
    body text,
    foreign key (author_id) references users (id),
    created_at date,
    updated_at date
);

CREATE TABLE IF NOT EXISTS comments
(
    id serial primary key,
    foreign key (text_id) references article (id),
    foreign key (user_id) references users (id),
    body text,
    created_at date
);

CREATE TABLE IF NOT EXISTS metadata
(
    id serial primary key,
    foreign key (text_id) references article (id),
    keys text,
    value text
);

CREATE TABLE IF NOT EXISTS revision
(
    id serial primary key,
    foreign key (text_id) references article (id),
    foreign key (author_id) references users (id),
    created_at date
);

CREATE TABLE IF NOT EXISTS searches
(
    id serial primary key,
    foreign key (user_id) references users (id),
    query text,
    created_at date
);

CREATE TABLE IF NOT EXISTS searches_result
(
    id serial primary key,
    foreign key (search_id) references searches (id),
    foreign key (text_id) references article (id),
    relevance double
);

CREATE TABLE IF NOT EXISTS tag
(
    id serial primary key,
    name text
);

CREATE TABLE IF NOT EXISTS text_tag
(
    foreign key (text_id) references article (id),
    foreign key (tag_id) references tag (id),
);