insert into category(id,name) values (null,'TECHNOLOGY');
insert into category(id,name) values (null,'FICTION');

insert into book(id,isbn,name,author,publication,category_id,pages,summary,added_at)
  values (null,'979-7690-7873-6-6','Data Structure and Algorithm','DT','DTP',1,100,'Summary for Data Structure & Algorithm',current_timestamp());
insert into book(id,isbn,name,author,publication,category_id,pages,summary,added_at)
  values (null,'978-70-17269-98-9','Harry Potter 1','JK','WB',2,300,'Summary for Harry Potter 1',current_timestamp());
insert into book(id,isbn,name,author,publication,category_id,pages,summary,added_at)
  values (null,'979-199-640-416-3','Harry Potter 2','JK','WB',2,300,'Summary for Harry Potter 2',current_timestamp());
