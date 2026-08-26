# Simple-Online-Store

**An online store where users can view all products ,products can be added to cart , view existing cart , remove products from cart , update the cart , and order the product. Admin can add the product,update the product,delete the product.
**

Features :- 
| Module | Description |
|--------|-------------|
| Signup/Login | JWT based authentication for admin and user and bcrypt password hashing algorithm |
| Product | Admin adds the product,update the product,delete the product |
| Cart | User can  add product to cart , can view cartitems , remove products from cart ,update the products in cart  |

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 21, Spring Boot, Spring Security |


Routes:-
localhost:8080/auth/signup --> POST
localhost:8080/auth/login  --> POST

Authorized Routes:-
Admin side:-
- `localhost:8080/api/products/create-product` --> For creating Product {POST}
- `localhost:8080/api/products/update-product` --> For updating Product {PUT}
- `localhost:8080/api/products/delete-product` --> For deleting Product {DELETE}

User side:-
- `localhost:8080/api/cart/add-to-cart{userId}/items/{productId}/{quantity}`  --> For adding product items to cart {POST}
- `localhost:8080/api/cart/remove-from-cart{userId}/items/{productId}`  --> For removing  product items from  cart {DELETE}
- `localhost:8080/api/cart/get-cart/{userId}`  --> For fetching cart items stored in cart of user {GET}




