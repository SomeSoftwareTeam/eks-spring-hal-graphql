# https://dev.mysql.com/doc/refman/8.0/en/create-table.html
create table property
(
    created timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    location point srid 0,
    name varchar(255) not null,
    owner varchar(255) not null,
    attributes json not null,
    updated timestamp not null default current_timestamp,
    index property_owner_index (owner)
);

create table verification
(
    created timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) not null,
    attributes json not null,
    owner varchar(255) not null,
    property_id bigint unsigned,
    updated timestamp not null default current_timestamp,
    FOREIGN KEY (property_id) REFERENCES property(id),
    index property_owner_index (owner)
);

create table fixture
(
    created timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) not null,
    owner varchar(255) not null,
    attributes json not null,
    property_id bigint unsigned,
    updated timestamp not null default current_timestamp,
    FOREIGN KEY (property_id) REFERENCES property(id),
    index property_owner_index (owner)
);

create table document
(
    created timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    attributes json not null,
    owner varchar(255) not null,
    property_id bigint unsigned,
    updated timestamp not null default current_timestamp,
    url varchar(255) not null,
    FOREIGN KEY (property_id) REFERENCES property(id)
);