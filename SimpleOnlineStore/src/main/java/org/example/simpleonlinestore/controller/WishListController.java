package org.example.simpleonlinestore.controller;

import org.example.simpleonlinestore.entity.WishList;
import org.example.simpleonlinestore.service.impl.cart.WishListServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {
    private final WishListServiceImpl wishListService;

    public WishListController(WishListServiceImpl wishListService) {
        this.wishListService = wishListService;
    }
    @GetMapping
    public ResponseEntity<WishList> getWishList() {
        WishList wishList = wishListService.getWishList();
        return ResponseEntity.ok(wishList);
    }
    @PostMapping("/add/{productId}")
    public ResponseEntity<WishList> addToWishList(@PathVariable Long productId){
        WishList updatedList=wishListService.addProductToWishList(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedList);
    }

}
