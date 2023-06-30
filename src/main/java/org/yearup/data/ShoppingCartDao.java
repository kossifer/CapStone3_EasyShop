package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    void create(ShoppingCartItem shoppingCartItem, int userId);

    void updateQuantity(ShoppingCartItem shoppingCartItem, int productId, int userId);

    void incrementQuantity(int productId, int userId);

    boolean isProductAlreadyAdded(int productId, int userId);

    void delete(int userId);
}


