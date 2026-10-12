# Simple-Online-Store

An online store where users can view all products ,products can be added to cart , view existing cart , remove products from cart , update the cart , and order the product. Admin can add the product,update the product,delete the product.


Features :- 
| Module | Description |
|--------|-------------|
| Signup/Login | JWT based authentication for admin and user and bcrypt password hashing algorithm , Sending OTP to email Verifying OTP and then login for user |
| Product | Admin adds the product,update the product,delete the product ,Add new Stock of Product , Update the Discount Percentage of Product|
| Category | Admin adds the category , update the category , get all categories , delete the category |
| Cart | User can  add products to cart , can view cartitems , remove products from cart ,update the products in cart , fetch current cart  |
| Order | User can place the order and items in current cart will be fetched and order will be placed , Get all orders placed by user , Get invoice of order , Send invoice to email of user after placing an order , Cancel the order 
| Other Functionalities | User can add multiple  address and while placing order address is selected , User can add product review from 1-5 rating and comment , Resend the otp and Reset password 

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 21, Spring Boot, Spring Security |


Routes:-
Admin side :-
- `localhost:8080/api/products/create-product` --> For creating new Product {POST}
- `localhost:8080/api/products/update-product` --> For updating existing Product {PUT}
- `localhost:8080/api/products/delete-product` --> For deleting Product {DELETE}
- `localhost:8080/api/products/get-all-products` --> For fetching all Products {GET}
- `localhost:8080/api/categories/add-category` --> For adding category of Products {POST}
- `localhost:8080/api/categories/update-category{categoryId}` --> For updating category of Products {PUT}
- `localhost:8080/api/categories/delete-category{categoryId}` --> For deleting category of Products {DELETE}
- `localhost:8080/api/categories/get-all-categories` --> For fetching all Categories {GET}
- `localhost:8080/api/products/{productId}/stock` --> For adding new stocks to current Product {PATCH}
- `localhost:8080/api/products/{productId}/discount` --> For updating discount percentage of existing item {PATCH}

User side :-
- `localhost:8080/auth/signup`  --> For signup a new user {POST}
- `localhost:8080/auth/verify`  --> For verifying new user by entering OTP {POST}
- `localhost:8080/auth/login`   --> For login after verifying using email and password {POST}
- `localhost:8080/auth/resendOtp` --> For Resending the OTP to user email {POST}
- `localhost:8080/auth/resetPassword` --> For reset password of user {POST}
- `localhost:8080/api/cart/add-to-cart/items/{productId}/{quantity}`  --> For adding product items to cart {POST}
- `localhost:8080/api/cart/remove-from-cart/items/{productId}`  --> For removing  product items from  cart {DELETE}
- `localhost:8080/api/cart/get-cart`  --> For fetching cart items stored in cart of user {GET}
- `localhost:8080/api/cart/update-cart-item/items/{productId}` --> To update quantity after adding items in cart {PUT}
- `localhost:8080/api/products/get-all-products` --> To fetch all the products {GET}
- `localhost:8080/api/products/get-product-by-category/{categoryName}` --> To get products by category {GET}
- `localhost:8080/api/products/search` --> To search product using wildcard search {GET}
- `localhost:8080/api/orders/place-order` --> To place order and fetch items from cart {POST}
- `localhost:8080/api/orders/get-orders` --> To get all order of user {GET}
- `localhost:8080/api/orders/{orderId}/cancel` --> To cancel the order {PUT}
- `localhost:8080/api/orders/{orderId}/invoice` --> To get invoice of placed orders {GET}
- `localhost:8080/api/orders/{orderId}/send-invoice` --> To send invoice to email as notification after placing order {POST}
- `localhost:8080/api/users/addresses` --> To add address of user to determine on which address to send delivery {POST}
- `localhost:8080/api/product-review/{productId}/add-review` --> To add review of product from user {POST}
