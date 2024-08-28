package com.toy2.shop29.cart.service;

public interface CartMergeService {
    void updateGuestCartToUser(String loginUser, String guestId) throws Exception;
}
