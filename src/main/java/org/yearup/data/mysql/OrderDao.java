package org.yearup.data.mysql;

import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;

public interface OrderDao {
    boolean checkOutOrder(int userId, Profile profile, ShoppingCart shoppingCart);
}

