-- not collected not returned not collect overdue not return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at)
values (1,2,'2b-1c-abc','2020-9-25 19:16:11','2021-10-25 19:16:11',null,null);

-- not collected not returned collect overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by)
values (1,4,'4b-1c-abc','2020-9-25 19:16:11','2021-10-25 19:16:11',null,null,'2020-9-27 19:16:11');

-- collected not returned not return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at)
values (1,3,'3b-1c-abc','2020-9-25 19:16:11','2021-10-25 19:16:11','2020-9-26 19:16:11',null);

insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at)
values (2,2,'2b-1c-def','2020-9-25 19:16:11','2021-10-25 19:16:11','2020-9-26 19:16:11',null);

-- collected not returned return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at)
values (1,1,'1b-1c-def','2020-9-25 19:16:11','2020-10-15 19:16:11','2020-9-26 19:16:11',null);

-- collected not returned return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at)
values (2,3,'3b-1c-def','2020-9-25 19:16:11','2020-10-15 19:16:11','2020-9-26 19:16:11',null);