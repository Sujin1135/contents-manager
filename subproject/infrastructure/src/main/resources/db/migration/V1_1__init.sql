CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists channels
(
    id       uuid                                                            not null
        primary key default uuid_generate_v4(),
    external_id         varchar(255) not null,
    description         text not null,
    family_safety       boolean not null,
    keyword             varchar(1000),
    thumbnail           varchar(1000),
    link                varchar(1000),
    total_view_count    int not null,
    total_subscriber    int not null,
    total_video         int not null,
    joined              date not null,
    deleted  timestamp                                                       null,
    created  timestamp default CURRENT_TIMESTAMP                             not null,
    modified timestamp default CURRENT_TIMESTAMP                             not null
);
