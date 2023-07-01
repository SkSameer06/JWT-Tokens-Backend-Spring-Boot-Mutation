package com.mock.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mock.demo.config.jwt;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mock.demo.dao.usersDao;
import com.mock.demo.entities.users;

import ch.qos.logback.core.model.Model;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
public class controller {

    @Autowired
	usersDao dao;



    @GetMapping("/")
	public String index() {
		return "index1";
		
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/signUp")
	@ResponseBody
	public String signUpUser(@RequestBody users user) {
		try {
			int res = dao.signUpUsers(user.getName(),user.getNumber(),user.getEmail(),user.getPassword());
		
			if(res == 0) {
				return "Invalid Data";
			}
			return "Data Inserted";
		} catch (Exception e) {
			return "Invalid Data";
		}
		
		
	}



	@SuppressWarnings("unchecked")
	@CrossOrigin(origins = "http://localhost:3000")
 	@RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
		public Map<String , String> loginCheck(@RequestBody users user1, HttpServletResponse response, 
		HttpServletRequest req, Model model) {
			java.util.Map result = new HashMap<>();
        
		
			users user = dao.loginCheck(user1.getEmail(), user1.getPassword());
		 	try {
				if(user.getEmail().equals(user1.getEmail()) &&  user.getPassword().equals(user1.getPassword())) {
				 

					HttpSession session = req.getSession();
					session.setAttribute("user", user);
					session.setAttribute("email", user.getEmail());
					session.setAttribute("password", user.getPassword());
					session.setAttribute("name", user.getName());
					ObjectMapper mapper = new ObjectMapper();
					String jsonInString = mapper.writeValueAsString(user);

					jwt tok = new jwt();
					String token = tok.token(user.getName(),user.getEmail());
					System.out.println("Token::>"+token);
					String refreshToken = tok.generateRefreshToken(user.getName(),user.getEmail());
					System.out.println("Refresh Token:::::::::>"+ refreshToken);

					result.put("AccessToken", token);
					result.put("RefreshToken", refreshToken);

				return result;
				}else{
					java.util.Map result1 = new HashMap<>();
					result1.put("Incorrect Credentials", "");
					return result1;
				}
			} catch (Exception e) {
				e.printStackTrace();
					java.util.Map result2 = new HashMap<>();
				result2.put("Incorrect Credentials", "");
				return result2;
			}
		}
		
	
	@SuppressWarnings("unchecked")
	@CrossOrigin(origins = "http://localhost:3000")
 	@RequestMapping(value = "/userData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String userData(@RequestHeader("Authorization") String bearerToken, HttpServletRequest req) {
System.out.println("Bearer Token:::::::::>"+bearerToken);
			String token, token1;
			//String token1 = extractBearerToken(req.getHeader(HttpHeaders.AUTHORIZATION));
			if(bearerToken.contains("Bearer ")){
				token = bearerToken.substring(7);
			}else{
				token1 = "Bearer "+ bearerToken;
				token = token1.substring(7);
			}
			
			HttpSession session = req.getSession();
		
			String email = (String) session.getAttribute("email");
			String password = (String) session.getAttribute("password");
			jwt tok = new jwt();
		
			try {

				Jws<Claims> decodedToken = tok.parseJwt(token);
System.out.println("Decode Toekn:::::::::"+decodedToken);
System.out.println("Decode Toekn:::::::::"+decodedToken.getBody().get("email"));
 System.out.println("Decode Toekn:::::::::"+email);
				if(token!=null && decodedToken.getBody().get("email").equals(email)) {
					System.out.println("Inside If Clock");
					users user = dao.loginCheck(email, password);
					ObjectMapper mapper = new ObjectMapper();
						String jsonInString;
						try {
							jsonInString = mapper.writeValueAsString(user);
							return jsonInString;
						} catch (JsonProcessingException e) {
							
							e.printStackTrace();
						}
			
				}
				return "Token Not Valid";
			} catch (Exception e) {
				e.printStackTrace();
				return "Token Not Valid";
			}
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(value = "/refreshtoken", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String refreshtoken(@RequestHeader("Authorization")  String refreshToken, HttpServletRequest req) throws Exception {
		jwt tok = new jwt();

		Boolean refreshTokenn = tok.validateRefreshToken(refreshToken);

		if(refreshTokenn == true){
			System.out.println("True");
			HttpSession session = req.getSession();
			String email = (String) session.getAttribute("email");
			String name = (String) session.getAttribute("name");
			//String password = (String) session.getAttribute("password");
   				
			String token = tok.token(name, email);
			System.out.println("Token::>"+token);
			return token;
		}else{
			System.out.println("False");
			return "Refresh Token Invalid";
		}
		
	}

}
