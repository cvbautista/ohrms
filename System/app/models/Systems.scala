package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import anorm.RowParser._
import anorm.ResultSetParser._
import java.util.Date



case class Book(book_id: Pk[Int], book_title: String, 
    book_author: String,publisher: String, qty_shelf: Int, cost: String)
    
case class Borrow_book(borrowbook_id: Pk[Int], user_name: String, book_title: String, book_id: Int,
     qty_shelf:Int, issue_date: Date, due_date: Date)
     
case class Borrow_books(borrowbook_id: Pk[Int], user_name: String, book_id: Int, 
    issue_date: Date, due_date: Date)

case class Users(user_name: String, p_word: String)

object Systems {
  

   val book = {
    get[Pk[Int]]("Book.book_id") ~
    get[String]("Book.book_title") ~
    get[String]("Book.book_author") ~
    get[String]("Book.publisher") ~
    get[Int]("Book.qty_shelf")~
    get[String]("Book.cost") map {
      case book_id~book_title~book_author~publisher~qty_shelf~cost => 
        Book(book_id, book_title, book_author, publisher,qty_shelf, cost)
    }
  }
   
   
  val user = {
    get[String]("Users.user_name") ~
    get[String]("Users.p_word")  map {
      case user_name~p_word=> 
        Users(user_name, p_word)
    }
  }
 

  val borrow_book = {
    get[Pk[Int]]("borrow_book.borrowbook_id") ~
    get[String]("borrow_book.user_name") ~
    get[String]("book.book_title") ~
    get[Int]("borrow_book.book_id") ~
    get[Int]("book.qty_shelf") ~
    get[Date]("borrow_book.issue_date")~
    get[Date]("borrow_book.due_date") map {
      case borrowbook_id~user_name~book_title~book_id~qty_shelf~issue_date~due_date => 
        Borrow_book(borrowbook_id, user_name,book_title, book_id,qty_shelf, issue_date, due_date)
    }
  }
  
    val borrow_books = {
    get[Pk[Int]]("borrow_book.borrowbook_id") ~
    get[String]("borrow_book.user_name") ~
    get[Int]("borrow_book.book_id") ~
    get[Date]("borrow_book.issue_date")~
    get[Date]("borrow_book.due_date") map {
      case borrowbook_id~user_name~book_id~issue_date~due_date => 
        Borrow_books(borrowbook_id, user_name,book_id, issue_date, due_date)
    }
  }
    

    def findByEmail(user_name:String): Option[Users] = {
    DB.withConnection { implicit connection =>
      SQL("select * from users where user_name = {user_name}").on(
        'user_name -> user_name
      ).as(Systems.user.singleOpt)
    }
  }
    
   
   def authenticate(user_name: String,p_word: String): Option[Users] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
         select * from users where 
         user_name = {user_name} and p_word = {p_word}
        """
      ).on(
        'user_name -> user_name,
        'p_word -> p_word
      ).as(Systems.user.singleOpt)
    }
  }
     
     
     
    def showAll(): List[Book] = {
    DB.withConnection { implicit connection =>
        SQL("select * from book").as(book *)
    }
    }
    
    
   /* def find(filter: String = "%"): List[Book] = {
    DB.withConnection { implicit connection =>
      SQL("select * from book where book.book_title like {filter}"
       ).on ('filter -> filter).as(book *)
    }
    }*/
       
    def showBorrow(): List[Borrow_book] = {
	    DB.withConnection { implicit connection =>
	       SQL("select bb.borrowbook_id, bb.user_name, bb.book_id, bb.issue_date, bb.due_date, " +
	       		"b.qty_shelf, b.book_title from borrow_book as bb join book as b on bb.book_id = b.book_id ").
	       		as(borrow_book *)
	       
	    }
    }
    

 
   
   def insert(borrow_book: Borrow_books) = {
	    DB.withConnection { implicit connection =>
	      SQL(
	        """
	          insert into borrow_book values (
	          {borrowbook_id}, {user_name}, {book_id}, {issue_date}, {due_date}
	          )
	        """
	      ).on(
	        'borrowbook_id -> borrow_book.borrowbook_id,
	        'user_name -> borrow_book.user_name,
	        'book_id -> borrow_book.book_id,
	        'issue_date -> borrow_book.issue_date,
	        'due_date -> borrow_book.due_date
	      ).executeUpdate()
	    }
	  }
   
   
   def selectReturnBook(returnbook_id: Int): List[Borrow_books] = {
     DB.withConnection { implicit connection =>
      SQL(" select * from borrow_book where borrowbook_id = {returnbook_id}")
      .on('returnbook_id -> returnbook_id).as(borrow_books *)
	    }
    }
   
   def check(book_id: Int, qty_shelf: Int): List[Book]= {
   DB.withConnection { implicit connection =>
     SQL(
         "select * from book as b where b.book_id = {book_id}"
         ).on('book_id -> book_id,
             'qty_shelf->qty_shelf).
         as(book *)
	    }
   }
   

   def minus(book_id: Int)= {
   DB.withConnection { implicit connection =>
     SQL(
         "update book as b " +
         "set qty_shelf=qty_shelf - 1 where b.book_id = {book_id}"
         ).on('book_id -> book_id
         ).executeUpdate()
	    }
   }

     
   def add(book_id: Int) {
   DB.withConnection { implicit connection =>
     SQL(
         "update book as b set qty_shelf= qty_shelf + 1 where b.book_id = {book_id}"
         ).on('book_id -> book_id
         ).executeUpdate()
	    }
   }
     
   def delete(returnbook_id: Int) = {
    DB.withConnection { implicit connection =>
      SQL("delete from borrow_book where borrowbook_id = {returnbook_id}")
      .on('returnbook_id -> returnbook_id).executeUpdate()
    }
  }
   
}


