insert into user (id,name,email,account_non_expired,account_non_locked,created_at,credentials_non_expired,enabled,password,updated_at,username)
 values (null,'admin-lms','admin@lms.com',1,1,current_timestamp(),1,1,'$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',current_timestamp(),'admin');

insert into user_roles(user_id,user_role) values (1,'USER');
insert into user_roles(user_id,user_role) values (1,'ADMIN');
