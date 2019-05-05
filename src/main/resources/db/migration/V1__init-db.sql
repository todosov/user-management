create table users (
	id              serial primary key,
	name            varchar(40) not null,
	description     varchar(255),
	key             varchar(32)
);

create unique index users_name_uindex on users (name);