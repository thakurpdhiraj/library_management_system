-- not collected not returned not collect overdue not return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by)
values (3,2,'2b-1c-abc',DATEADD('DAY',-7, CURRENT_DATE),DATEADD('MONTH',1, CURRENT_DATE),null,null,DATEADD('DAY',3, CURRENT_DATE));

insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by)
values (2,2,'2b-1c-def',DATEADD('DAY',-7, CURRENT_DATE),DATEADD('MONTH',1, CURRENT_DATE),null,null,DATEADD('DAY',3, CURRENT_DATE));

-- not collected not returned collect overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by)
values (3,4,'4b-2c-abc',DATEADD('DAY',-7, CURRENT_DATE),DATEADD('DAY',25, CURRENT_DATE),null,null,DATEADD('DAY',-3, CURRENT_DATE));

insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by)
values (2,4,'4b-2c-def',DATEADD('DAY',-7, CURRENT_DATE),DATEADD('DAY',25, CURRENT_DATE),null,null,DATEADD('DAY',-3, CURRENT_DATE));

-- collected not returned not return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by)
values (3,3,'3b-2c-abc',DATEADD('DAY',-7, CURRENT_DATE),DATEADD('DAY',25, CURRENT_DATE),DATEADD('DAY',-3, CURRENT_DATE),null,DATEADD('DAY',-2, CURRENT_DATE));

insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by)
values (2,3,'3b-2c-def',DATEADD('DAY',-7, CURRENT_DATE),DATEADD('DAY',25, CURRENT_DATE),DATEADD('DAY',-3, CURRENT_DATE),null,DATEADD('DAY',-2, CURRENT_DATE));

-- collected not returned return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by)
values (3,1,'1b-1c-abc',DATEADD('DAY',-7, CURRENT_DATE),DATEADD('DAY',-1, CURRENT_DATE),DATEADD('DAY',-4, CURRENT_DATE),null,DATEADD('DAY',-3, CURRENT_DATE));

insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by)
values (2,1,'1b-1c-def',DATEADD('DAY',-7, CURRENT_DATE),DATEADD('DAY',-1, CURRENT_DATE),DATEADD('DAY',-4, CURRENT_DATE),null,DATEADD('DAY',-3, CURRENT_DATE));
