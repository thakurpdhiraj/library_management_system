-- not collected, not returned, collect overdue, not return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by,book_isbn,book_name)
values (1,2,'2b-1c-abc','2020-9-25 19:16:11','2021-10-25 19:16:11',null,null,'2020-9-27 19:16:11','978-844-89-8416-8','Java');

-- not collected, not returned, collect overdue, not return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by,book_isbn,book_name)
values (1,4,'4b-1c-abc','2020-9-25 19:16:11','2021-10-25 19:16:11',null,null,'2020-9-27 19:16:11','979-199-640-416-3','Harry Potter 2');

-- collected, not returned, not return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by,book_isbn,book_name)
values (1,3,'3b-1c-abc','2020-9-25 19:16:11','2021-10-25 19:16:11','2020-9-26 19:16:11',null,'2020-9-27 19:16:11','978-70-17269-98-9','Harry Potter 1');

-- collected, not returned, not return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by,book_isbn,book_name)
values (2,2,'2b-1c-def','2020-9-25 19:16:11','2021-10-25 19:16:11','2020-9-26 19:16:11',null,'2020-9-27 19:16:11','978-844-89-8416-8','Java');

-- collected, not returned, return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by,book_isbn,book_name)
values (1,1,'1b-1c-def','2020-9-25 19:16:11','2020-10-15 19:16:11','2020-9-26 19:16:11',null,'2020-9-27 19:16:11','979-7690-7873-6-6','Data Structure and Algorithm');

-- collected, not returned, return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by,book_isbn,book_name)
values (2,3,'3b-1c-def','2020-9-25 19:16:11','2020-10-15 19:16:11','2020-9-26 19:16:11',null,'2020-9-27 19:16:11','978-70-17269-98-9','Harry Potter 1');