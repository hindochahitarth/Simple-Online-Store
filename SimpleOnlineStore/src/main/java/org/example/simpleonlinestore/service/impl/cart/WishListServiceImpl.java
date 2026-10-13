package org.example.simpleonlinestore.service.impl.cart;

import jakarta.transaction.Transactional;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.entity.ProductReview;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.entity.WishList;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.repository.UserRepository;
import org.example.simpleonlinestore.repository.WishListRepository;
import org.example.simpleonlinestore.service.interfaces.WishListService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class WishListServiceImpl implements WishListService {

    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    public WishListServiceImpl(UserRepository userRepository, WishListRepository wishListRepository, ProductRepository productRepository){
        this.userRepository=userRepository;
        this.wishListRepository=wishListRepository;
        this.productRepository = productRepository;
    }
    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    @Transactional
    public WishList getWishList(){
        User loggedinUser=getLoggedInUser();

        return wishListRepository.findByUser(loggedinUser)
                .orElseGet(() -> {
                    WishList wishList=new WishList();
                    wishList.setUser(loggedinUser);
                   return  wishListRepository.save(wishList);
                });
    }

    @Override
    public WishList addProductToWishList(Long productId) {
        WishList wishList=getWishList();
        Product product=productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product with given id not found "));
        wishList.addProduct(product);
        return wishListRepository.save(wishList);
    }
}
