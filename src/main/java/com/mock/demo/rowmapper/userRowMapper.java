package com.mock.demo.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mock.demo.entities.users;

public class userRowMapper implements RowMapper<users> {

	@Override
	public users mapRow(ResultSet rs, int rowNum) throws SQLException {
		users cm = new users();
		cm.setName(rs.getString(1));
		cm.setNumber(rs.getLong(2));
		cm.setEmail(rs.getString(3));
		cm.setPassword(rs.getString(4));
		cm.setId(rs.getInt(5));

		return cm;
	}

}