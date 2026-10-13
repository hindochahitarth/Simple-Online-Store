package org.example.simpleonlinestore.service.interfaces;

import org.example.simpleonlinestore.entity.WishList;

public interface WishListService {
    WishList getWishList();
    WishList addProductToWishList(Long productId);
}
