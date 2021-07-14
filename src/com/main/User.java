/**
 * 
 */
package com.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


/**
 * @author Fazeel
 *
 */

@ManagedBean(name = "user")
@RequestScoped
public class User {

	int id;
	String name;
	String email;
	String gender;
	String address;
	String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	

	public User(String name, String email, String gender, String address, String password) {
		super();
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.address = address;
		this.password = password;
	}



	public User() {
		super();
		// TODO Auto-generated constructor stub
	}



	Connection connection;

	public Connection getConnection() {
		Connection connection = null;
		try {

			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/primefaces", "root", "");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return connection;
	}

	public List<User> userList() throws SQLException {
		List<User> userlist = new ArrayList<User>();

		connection = getConnection();
		String sql = "select * from users";
		PreparedStatement myPre = connection.prepareStatement(sql);
		// Statement myStat = connection.createStatement();

		ResultSet rs = myPre.executeQuery(sql);
		while (rs.next()) {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setEmail(rs.getString("email"));
			user.setAddress(rs.getString("address"));
			user.setGender(rs.getString("gender"));
			userlist.add(user);

		}
		return userlist;
	}

	private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

	public String save() {
		int result = 0;

		try {
			connection = getConnection();
			PreparedStatement myPre = connection
					.prepareStatement("insert into users(name,email,gender,address,password) values (?,?,?,?,?)");
			myPre.setString(1, name);
			
			myPre.setString(2, email);
			myPre.setString(3, gender);
			myPre.setString(4, address);
			myPre.setString(5, password);
			result = myPre.executeUpdate();
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (result != 0)
			return "AllUser.xhtml?faces-redirect=true";
		else
			return "create.xhtml?faces-redirect=true";

	}

	public String getGender(char gender) {
		if (gender == 'M') {
			return "Male";
		} else {
			return "Female";
		}
	}

	// Used to fetch record to update
	public String edit(int id) {
		User user = null;
		System.out.println(id);
		try {
			connection = getConnection();
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select * from users where id = " + (id));
			rs.next();
			user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setEmail(rs.getString("email"));
			user.setGender(rs.getString("gender"));
			user.setAddress(rs.getString("address"));
			user.setPassword(rs.getString("password"));
			System.out.println(rs.getString("password"));
			sessionMap.put("editUser", user);
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return "/edit.xhtml?faces-redirect=true";
	}

	// Used to update user record
	public String update(User u) {
		// int result = 0;
		try {
			connection = getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("update users set name=?,email=?,password=?,gender=?,address=? where id=?");
			stmt.setString(1, u.getName());
			stmt.setString(2, u.getEmail());
			stmt.setString(3, u.getPassword());
			stmt.setString(4, u.getGender());
			stmt.setString(5, u.getAddress());
			stmt.setInt(6, u.getId());
			stmt.executeUpdate();
			connection.close();
		} catch (Exception e) {
			System.out.println();
		}
		return "/AllUser.xhtml?faces-redirect=true";
	}

	// Used to delete user record
	public void delete(int id) {
		try {
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("delete from users where id = " + id);
			stmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
