package com.mock.demo.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Email;
import javax.validation.constraints.Null;

import org.springframework.web.bind.annotation.GetMapping;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.mock.demo.Config.jwt;
import com.mock.demo.dao.usersDao;
import com.mock.demo.entities.users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
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
			int res = dao.signUpUsers(user.getName(), user.getNumber(), user.getEmail(), user.getPassword());

			if (res == 0) {
				return "Invalid Data";
			} else {
				ObjectMapper mapper = new ObjectMapper();
				String jsonInString = mapper.writeValueAsString("Data Inserted");
				return jsonInString;
			}
		} catch (Exception e) {
			return "Invalid Data";
		}

	}

	@SuppressWarnings("unchecked")
	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, String> loginCheck(@RequestBody users user1, HttpServletResponse response,
			HttpServletRequest req, Model model) {

		Map result = new HashMap<>();
		if (user1.getEmail() == null || user1.getEmail().isEmpty() || user1.getPassword() == null
				|| user1.getPassword().isEmpty()) {

			Map result7 = new HashMap<>();
			result7.put("IncorrectCredentials", "11");
			return result7;

		} else {

			users user = dao.loginCheck(user1.getEmail(), user1.getPassword());
			try {

				if (user.getEmail() != null && user.getPassword() != null) {

					ObjectMapper mapper = new ObjectMapper();
					String jsonInString = mapper.writeValueAsString(user);
					jwt tok = new jwt();
					String token = tok.token(user.getId(), user.getName(), user.getEmail());
					System.out.println("Access Token:::::::::>" + token);
					String refreshToken = tok.generateRefreshToken(user.getId(), user.getName(), user.getEmail());
					System.out.println("Refresh Token:::::::::>" + refreshToken);

					result.put("AccessToken", token);
					result.put("RefreshToken", refreshToken);

					return result;

				} else {

					Map result1 = new HashMap<>();
					result1.put("IncorrectCredentials", "11");
					return result1;

				}
			} catch (Exception e) {

				e.printStackTrace();
				Map result2 = new HashMap<>();
				result2.put("IncorrectCredentials", "11");
				return result2;

			}
		}

	}

	@SuppressWarnings("unchecked")
	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(value = "/userData", method = { RequestMethod.POST, RequestMethod.GET })
	public String userData(@RequestHeader("Authorization") String bearerToken, HttpServletRequest req)
			throws NoSuchAlgorithmException {

		String token, token1;

		if (bearerToken != null) {

			if (bearerToken.contains("Bearer ")) {

				token = bearerToken.substring(7);

			} else {

				token1 = "Bearer " + bearerToken;
				token = token1.substring(7);

			}

			try {

				jwt tok = new jwt();
				Jws<Claims> decodedToken = tok.parseJwt(token);

				int id = (int) decodedToken.getBody().get("id");

				if (token != null) {

					users user = dao.getUserDataById(id);
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
				return "Token Expired";

			}
		} else {

			return "Token Not Valid";

		}

	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(value = "/refreshtoken", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String refreshtoken(@RequestHeader("refresh") String refreshToken, HttpServletRequest req)
			throws Exception {

		jwt tok = new jwt();

		Jws<Claims> refreshTokenn = tok.validateRefreshToken(refreshToken);

		if (refreshTokenn != null) {

			int id = (int) refreshTokenn.getBody().get("id");
			String name = (String) refreshTokenn.getBody().get("name");
			String email = (String) refreshTokenn.getBody().get("email");

			String token = tok.token(id, name, email);

			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(token);

			return jsonInString;

		} else {

			return "Refresh Token Invalid";

		}

	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(value = "/validateAccessToken", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String validateAccessToken(@RequestHeader("Authorization") String AccessToken,
			@RequestHeader("refresh") String RefreshToken, HttpServletRequest req)
			throws Exception {

		System.out.println("Accesss:::::::::::::::::::>" + AccessToken);
		System.out.println("Refresh:::::::::::::::::::>" + RefreshToken);

		String token, token1;

		if (AccessToken.contains("Bearer ")) {

			token = AccessToken.substring(7);

		} else {

			token1 = "Bearer " + AccessToken;
			token = token1.substring(7);

		}

		try {
			jwt tok = new jwt();
			Jws<Claims> decodedToken = tok.parseJwt(token);
			System.out.println("Decode Token:::::::::>" + decodedToken);

			ObjectMapper mapper = new ObjectMapper();
			String sttr = mapper.writeValueAsString("Sameer");

			return sttr;
		} catch (Exception e) {
			System.out.println("Exception::::::::::::::::::::::::::::::>");
			// e.printStackTrace();
			jwt tok = new jwt();

			Jws<Claims> refreshTokenn = tok.validateRefreshToken(RefreshToken);

			if (refreshTokenn != null) {

				int id = (int) refreshTokenn.getBody().get("id");
				String name = (String) refreshTokenn.getBody().get("name");
				String email = (String) refreshTokenn.getBody().get("email");

				System.out.println("Id:" + id);
				System.out.println("Name:" + name);
				System.out.println("Email:" + email);

				String tokenn = tok.token(id, name, email);

				ObjectMapper mapper = new ObjectMapper();
				String jsonInString = mapper.writeValueAsString(tokenn);

				return jsonInString;

			} else {

				return "Refresh Token Invalid";

			}
		}

	}

	@RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String logout(@RequestHeader("Authorization") String refreshToken, HttpServletRequest req)
			throws Exception {

		return "logout";

	}

}
