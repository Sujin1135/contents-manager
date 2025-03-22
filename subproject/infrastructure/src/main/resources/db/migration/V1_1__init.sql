create table if not exists channels
(
    id       uuid                                                            not null
        primary key,
    external_id         varchar(255) not null,
    description         varchar(355) not null,
    family_safety       boolean not null,
    keyword             json,
    thumbnail           varchar(255),
    link                varchar(255),
    total_view_count    int not null,
    total_subscriber    int not null,
    total_video         int not null,
    joined              timestamp not null,
    deleted  timestamp                                                       null,
    created  timestamp default CURRENT_TIMESTAMP                             not null,
    modified timestamp default CURRENT_TIMESTAMP                             not null
);
