package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Product;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;
import org.yearup.models.ShoppingCart;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao {
    public MySqlProfileDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public boolean checkOutOrder(int userId, Profile profile, ShoppingCart shoppingCart) {
        return false;
    }

    @Override
    public Profile create(Profile profile) {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile getByUserId(int userId) {
        String sql = "SELECT * FROM profiles WHERE user_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet row = statement.executeQuery();

            if (row.next()) {
                return mapRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(Profile profile, int userId) {

        List<Object> paramValues = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("UPDATE profiles SET ");
        boolean appended = false;

        if (!profile.getFirstName().isEmpty()) {
            queryBuilder.append(" first_name = ? ");
            paramValues.add(profile.getFirstName());
            appended = true;
        }
        if (!profile.getLastName().isEmpty()) {
            if (!appended)
                queryBuilder.append(" last_name = ? ");
            else {
                queryBuilder.append(", last_name = ? ");
            }
            appended = true;
            paramValues.add(profile.getLastName());
        }

        if (!profile.getPhone().isEmpty()) {
            if (!appended)
                queryBuilder.append(" phone = ? ");
            else
                queryBuilder.append(", phone = ? ");
            appended = true;
            paramValues.add(profile.getPhone());
        }

        if (!profile.getEmail().isEmpty()) {
            if (!appended)
                queryBuilder.append(" email = ?");

            else
                queryBuilder.append(", email = ?");
            appended = true;
            paramValues.add(profile.getEmail());
        }


        if (!profile.getAddress().isEmpty()) {
            if (!appended)
                queryBuilder.append(" address = ? ");
            else
                queryBuilder.append(", address = ? ");
            appended = true;
            paramValues.add(profile.getCity());
        }

        if (!profile.getCity().isEmpty()) {
            if (!appended)
                queryBuilder.append(" city = ?");
            else
                queryBuilder.append(", city = ?");
            appended = true;
            paramValues.add(profile.getCity());
        }


        if (!profile.getState().isEmpty()) {
            if (!appended)
                queryBuilder.append(" state = ?");
            else
                queryBuilder.append(", state = ?");
            appended = true;
            paramValues.add(profile.getState());
        }

        if (!profile.getZip().isEmpty()) {
            if (!appended)
                queryBuilder.append(" zip = ?");
            else
                queryBuilder.append(", zip = ?");

            paramValues.add(profile.getZip());
        }

        queryBuilder.append(" WHERE user_id = ?;");
        paramValues.add(userId);

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

            for (int i = 0; i < paramValues.size(); i++) {
                statement.setString(i + 1, paramValues.get(i).toString());
            }


            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private Profile mapRow(ResultSet row) throws SQLException {
        int userId = row.getInt("user_id");
        String firstName = row.getString("first_name");
        String lastName = row.getString("last_name");
        String phone = row.getString("phone");
        String email = row.getString("email");
        String address = row.getString("address");
        String city = row.getString("city");
        String state = row.getString("state");
        String zip = row.getString("zip");

        return new Profile(userId, firstName, lastName, phone, email, address, city, state, zip);

    }
}
