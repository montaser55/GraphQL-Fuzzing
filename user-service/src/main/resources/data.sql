
create sequence users_seq start with 1 increment by 50

create table users
(age integer not null,
 is_verified boolean not null,
 created_at timestamp(6) not null,
 id bigint not null,
 updated_at timestamp(6) not null,
 address varchar(255) not null,
 email varchar(255) unique,
 gender varchar(255) not null
     check (gender in ('MALE','FEMALE','OTHER')),
 name varchar(255) not null,
 user_role varchar(255) not null
     check (user_role in ('ADMIN','USER')),
 primary key (id))