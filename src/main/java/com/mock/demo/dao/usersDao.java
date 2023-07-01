package com.mock.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mock.demo.entities.users;
import com.mock.demo.rowmapper.userRowMapper;

@Repository("userDao")
public class usersDao {

    @Autowired
	JdbcTemplate jdbcTemplate;

    public int signUpUsers(String name, Long number, String email, String password) {
        try {
               String sql = "insert into users(name,number,email,password) values(?,?,?,?)";
        int res = jdbcTemplate.update(sql, name, number, email, password);
        System.out.println("Result:::::::::>"+res);
        return res;
        } catch (Exception e) {
            return 0;
        }
     

    }

        public users loginCheck(String email, String password) {
       
            try {
                String sql = "select * from users where email = ? and password = ?";
        userRowMapper rm = new userRowMapper();
        users user = jdbcTemplate.queryForObject(sql, rm, email, password);
 
        return user;
            } catch (Exception e) {
                users user = new users();
                System.out.println("User::::>"+user);
               return user; 
            }
        

    }

            public users getUserDataById(int id) {
       
            try {
                String sql = "select * from users where id = ?";
        userRowMapper rm = new userRowMapper();
        users user = jdbcTemplate.queryForObject(sql, rm, id);
 
        return user;
            } catch (Exception e) {
                users user = new users();
                System.out.println("User::::>"+user);
               return user; 
            }
        

    }
    
}
