package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.data.mysql.OrderDao;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

public class OrdersController {


        private UserDao userDao;
        private OrderDao orderDao;

        private ProfileDao profileDao;

        private ShoppingCartDao shoppingCartDao;
        @Autowired
        public OrdersController(UserDao userDao, ProfileDao profileDao, OrderDao orderDao, ShoppingCartDao shoppingCartDao) {

                this.userDao = userDao;
                this.profileDao = profileDao;
                this.orderDao = orderDao;
                this.shoppingCartDao = shoppingCartDao;
        }

        @PostMapping
        @PreAuthorize("isAuthenticated()")
        public void checkOutOrder(Principal principal) {
                try {
                        String userName = principal.getName();
                        User user = userDao.getByUserName(userName);
                        int userId = user.getId();
                        ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);
                        Profile profile = profileDao.getByUserId(userId);

                        boolean orderCreated = orderDao.checkOutOrder(userId, profile, shoppingCart);
                        if (!orderCreated) {
                                throw new Exception();
                        } else {
                                shoppingCartDao.delete(userId);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
                }
        }

}
