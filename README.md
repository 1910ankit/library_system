# library_system
Book Controller
Controller for Book management

Endpoint: /api/books/register 
Method: GET
Description: Register any new book.
Request Parameters: no 
RequestBody Schema : {
  "isbn": "string",
  "title": "string",
  "author": "string"
}

Borrower-controller
controller for Borrower management

Endpoint: /api/borrowers/registerBorrower
Method: POST
Description: Register new borrower.
Request Parameters: None
Requestbody schema :{
  "name": "string",
  "email": "202kumarankit@gmail.com"
}

Endpoint: /api/borrowers/{bookId}/borrow/{borrowerId}
Method: PUT
Description: Borrow an available book.
Request Parameters: bookid , borrowerid

EndPoint: /api/borrowers/{bookId}/return
Method:PUT
Description:return a borrowed book
Request parameters: bookid

