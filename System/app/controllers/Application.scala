package controllers


import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import anorm._
import views._
import models._
import models.Systems
import models.Book
import models.Borrow_book
import models.Borrow_books
import models.Users
import java.util.Date



object Application extends Controller{
  val Borrow = Redirect(routes.Application.borrow())
  val Available = Redirect(routes.Application.available())
  val Error = Redirect(routes.Application.error())

  val bookForm = Form(
	mapping(
	    "book_id" -> ignored(NotAssigned:Pk[Int]),
		"book_title" -> nonEmptyText,
		"book_author" -> nonEmptyText,
		"publisher" -> nonEmptyText,
		"qty_shelf" -> number,
		"cost" -> nonEmptyText
	)(Book.apply)(Book.unapply) 
  ) 

  
  
  val borrowForm = Form(
	mapping(
	    "borrowbook_id" -> ignored(NotAssigned:Pk[Int]),
	    "user_name" -> nonEmptyText,
		"book_id" -> number,
		"issue_date" -> date,
		"due_date" -> date
	)(Borrow_books.apply)(Borrow_books.unapply)
	)


  
	val loginForm = Form(
    tuple(
      "user_name" -> text,
      "p_word" -> text
    ) verifying ("Invalid username or password", result => result match {
      case (user_name, p_word) => Systems.authenticate(user_name, p_word).isDefined
    })
  )

 
  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }
   def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => Redirect(routes.Application.homes()).withSession("user_name" -> user._1)
    )
  }
   
   
   private def username(request: RequestHeader) = request.session.get("user_name")
   private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)
   
   
   def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
    Action(request => f(user)(request))
  }


  
  def homes = Action {
    Ok(views.html.homes())
  }
  


  def available = Action {
	  Ok(views.html.available(Systems.showAll()))  
  }
 

  def borrow = Action { implicit request =>
    Ok(views.html.borrow(Systems.showBorrow(),borrowForm))
  }
 
  
  def report = Action {
    Ok(views.html.report())
  }
  
  def error = Action {
    Ok(views.html.error())
  }
   
  def signout = Action {
    Ok(views.html.signout())
  }
  

   
  def selectReturnBook(bookreturn_id:Int) = Action { implicit request =>
	Ok(views.html.returnbook(bookreturn_id,Systems.selectReturnBook(bookreturn_id)))
  }
  
  
  def save(book_id:Int, qty_shelf:Int) = Action { implicit request =>
    Systems.check(book_id, qty_shelf)
    	if (qty_shelf > 0){
    	borrowForm.bindFromRequest.fold(
    	formWithErrors => BadRequest(views.html.addcart(formWithErrors,book_id, qty_shelf)),
    	Borrow_books => {
    		Systems.insert(Borrow_books)
	     }
    	)
    	
    	Systems.minus(book_id)
    	Borrow.flashing()
    }
    else {
      println("No more Books in shelf.")
      Error.flashing()

    }
 
  }


 def delete(book_id: Int) = Action {
	 Systems.delete(book_id)
	 Systems.add(book_id)
	 Borrow.flashing()
 }
  
 
   
  def booktitle(book_id: Int,qty_shelf: Int) = Action {
    Ok(views.html.addcart(borrowForm, book_id, qty_shelf))
  }
  
}