-- not collected not returned not collect overdue not return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by,book_isbn,book_name)
values (2,2,'2b-1c-2',DATEADD('DAY',-6, CURRENT_DATE),DATEADD('MONTH',1, CURRENT_DATE),null,null,DATEADD('DAY',3, CURRENT_DATE),'978-844-89-8416-8','Java');

-- not collected not returned collect overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by,book_isbn,book_name)
values (2,4,'4b-2c-2',DATEADD('DAY',-7, CURRENT_DATE),DATEADD('DAY',25, CURRENT_DATE),null,null,DATEADD('DAY',-3, CURRENT_DATE),'979-199-640-416-3','Harry Potter 2');

-- collected not returned not return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by,book_isbn,book_name)
values (2,3,'3b-2c-2',DATEADD('DAY',-5, CURRENT_DATE),DATEADD('DAY',25, CURRENT_DATE),DATEADD('DAY',-3, CURRENT_DATE),null,DATEADD('DAY',-2, CURRENT_DATE),'978-70-17269-98-9','Harry Potter 1');

-- collected not returned return overdue
insert into book_order(user_id,book_id,book_reference_id,ordered_at,return_by,collected_at,returned_at,collect_by,book_isbn,book_name)
values (2,1,'1b-1c-2',DATEADD('DAY',-10, CURRENT_DATE),DATEADD('DAY',-2, CURRENT_DATE),DATEADD('DAY',-4, CURRENT_DATE),null,DATEADD('DAY',-3, CURRENT_DATE),'979-7690-7873-6-6','Data Structure and Algorithm');
