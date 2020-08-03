# https://dev.mysql.com/doc/refman/8.0/en/create-table.html
create table property
(
    created_at timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    location point srid 3857,
    name varchar(255) not null,
    owner varchar(255) not null,
    attributes json not null,
    updated timestamp not null default current_timestamp,
    index property_owner_index (owner)
);

create table verification
(
    created_at timestamp not null default current_timestamp,
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
    created_at timestamp not null default current_timestamp,
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

create table item
(
    created_at timestamp not null default current_timestamp,
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
    created_at timestamp not null default current_timestamp,
    deleted boolean not null default false,
    description varchar(255) not null,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    attributes json not null,
    name varchar(255) not null,
    owner varchar(255) not null,
    property_id bigint unsigned,
    updated timestamp not null default current_timestamp,
    url varchar(255) not null,
    FOREIGN KEY (property_id) REFERENCES property(id)
);