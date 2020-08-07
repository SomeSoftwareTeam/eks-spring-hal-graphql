# https://dev.mysql.com/doc/refman/8.0/en/create-table.html
create table property
(
    address varchar(255) not null,
    address_formatted boolean default false,
    attributes json not null,
    created_at timestamp not null default current_timestamp,
    deleted boolean not null default false,
    group_name varchar(255) not null,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    location point srid 3857,
    name varchar(255) not null,
    owner_id varchar(255) not null,
    updated timestamp not null default current_timestamp,
    index group_name_index (group_name)
);

create table verification
(
    created_at timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) not null,
    attributes json not null,
    owner_id varchar(255) not null,
    property_id bigint unsigned,
    updated timestamp not null default current_timestamp,
    FOREIGN KEY (property_id) REFERENCES property(id)
);

create table fixture
(
    attributes json not null,
    created_at timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) not null,
    owner_id varchar(255) not null,
    property_id bigint unsigned,
    updated timestamp not null default current_timestamp,
    FOREIGN KEY (property_id) REFERENCES property(id)
);

create table item
(
    attributes json not null,
    created_at timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) not null,
    owner_id varchar(255) not null,
    property_id bigint unsigned,
    updated timestamp not null default current_timestamp,
    FOREIGN KEY (property_id) REFERENCES property(id)
);

create table document
(
    created_at timestamp not null default current_timestamp,
    deleted boolean not null default false,
    description varchar(255) not null,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    attributes json not null,
    name varchar(255) not null,
    owner_id varchar(255) not null,
    property_id bigint unsigned,
    updated timestamp not null default current_timestamp,
    url varchar(255) not null,
    FOREIGN KEY (property_id) REFERENCES property(id)
);