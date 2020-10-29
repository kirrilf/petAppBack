delete from user_roles;
delete from users;


insert into users(  id,
                    status,
                    email,
                    first_name,
                    last_name,
                    password,
                    username)
                    values
                    (1,
                    'ACTIVE',
                    'test@test.com',
                    'test',
                    'test',
                    '$2a$10$EhLB2zd2Bz4qcPoh1t96Xeauo/0R715bbEFaSAto7V3KkcchtBZ16',
                    'test'
                    );

insert into user_roles (user_id, role_id) values (1, 1), (1, 2);

