# --- !Ups

create table book(
 book_id Int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	book_title varchar(50) NOT NULL,
	book_author varchar(30) NOT NULL,
	publisher varchar(30) NOT NULL,
	qty_shelf Int NOT NULL,
	cost varchar(10) NOT NULL);


create table users(
 user_name varchar(30) NOT NULL PRIMARY KEY,
	p_word varchar(30) NOT NULL);


create table borrow_book(
	borrowbook_id Int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_name  varchar(30) NOT NULL,
 book_id Int NOT NULL ,
	issue_date date,
 due_date date);


alter table borrow_book add constraint fk_borrowbook2 foreign key (user_name) references users(user_name);
alter table borrow_book add constraint fk_borrowbook1 foreign key (book_id) references book(book_id);

ALTER TABLE borrow_book AUTO_INCREMENT=1000;

Insert into users(user_name,p_word)
values
('cindy','cvbautista');


Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Advanced Oracle SQL Programming','Laurent Schneider','3','1360','Oracle ACE, Oracle Certified M');

Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Visual C++ Programming','Yashavant Kanetkar','5','1660','BPB Publications');

Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Foundations of WPF','Laurence Moroney','1','1240','Apress');

Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Programming for e-Learning Developers','Jeffrey Rhodes','7','1800','Platte Canyon Press');

Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Programming Windows Phone 7 Series','Charles Petzold','11','2309','Microsoft Press');

Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Programming Windows Sixth Edition','Charles Petzold','4','2099','Microsoft Press');
               
Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Programming in Python 3 (Second Edition)','Mark Summerfield ','9','1947','Prentice Hall');


Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Advanced Qt Programming','Mark Summerfield','5','1500','Prentice Hall');


Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('UNIX Shells by Example 4th Edition','Ellie Quigley','7','3000','Prentice Hall PTR');

Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Learning iPhone Programming','Alasdair Allan','2','2376','O Reilly');


Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Pro .NET 2.0 Extreme Programming','Greg Pearman, James Goodwill','4','2500','Apress');

Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Programming Microsoft ASP.NET 4','Dino Esposito','2','2300','Microsoft');


Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Essential Pascal', ' Marco Cantu', '4','1500','CreateSpace 2008');


Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Pro Java 6 3D Game Development', 'Andrew Davison', '12','1230','Apress 2007');

Insert into book(book_title,book_author,qty_shelf,cost,publisher)
values
('Objective-C 2.0 Essentials', 'Neil Smyth', '9','2120','Techotopia 2010');

Insert into borrow_book(user_name, book_id, issue_date, due_date)
values
('cindy','1','2012/12/12','2012/12/13');


# --- !Downs

DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS borrow_book;
DROP TABLE IF EXISTS users;
