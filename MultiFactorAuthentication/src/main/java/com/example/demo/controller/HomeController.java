package com.example.demo.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.UserInfo;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.AuthRequest;

@Controller
public class HomeController {
	public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
	public static String APP_NAME = "TTCOnline";
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private static final String BAD_CREDENTIALS_FAILURE_URL = "/resetAuthPage?error";
	
	@Value("${welcome}")
	String msgValue;

	@Autowired
	private UserRepository userRepo;

	@GetMapping("/Login")
	public String loginPage(Model model) {
		AuthRequest req = new AuthRequest();
		req.setDisplayEmailField(false);
		req.setDisplayOtpLink(false);
		req.setDisplayPass(false);
		req.setShowVerficationField(false);

		model.addAttribute("authRequest", req);
		return "LoginForm.html";
	}

	@GetMapping("/homePage")
	public String homePage(Model model) {
		AuthRequest req = new AuthRequest();
		model.addAttribute("authRequest", req);
		return "Welcome.html";
	}

	@GetMapping("/login")
	public String loadIndexForm(Model model, HttpServletRequest request) {
		AuthRequest req = new AuthRequest();
		Map<String, String[]> parameters = request.getParameterMap();
		if (parameters.containsKey("error")) {
			req.setDisplayErrorMessage(true);
		} else {
			req.setDisplayErrorMessage(false);
		}
		System.out.println("Display Error Message " + req.isDisplayErrorMessage());
		req.setDisplayEmailField(true);
		model.addAttribute("authRequest", req);
		return "index.html";
	}

	@PostMapping("/checkLogin")
	public String getAuthenticated(@ModelAttribute("authRequest") AuthRequest user)
			throws UnsupportedEncodingException {
		UserInfo currentUser = userRepo.findByEmailAddress(user.getUserName());
		if (currentUser != null) {
			user.setUserName(currentUser.getEmailAddress());
			if (currentUser.isUsing2FA()) {
				if (currentUser.getSecretKey() != null) {
					user.setDisplayPass(true);
					user.setShowVerficationField(true);
					user.setDisplayOtpLink(false);
					if(user.isMobileView()) {
						Totp otp = new Totp(currentUser.getSecretKey());
						System.out.println("Current OTP number for mobile view : "+ otp.now());
						user.setOtpLabel("OTP ->");
						user.setGeneratedCode(otp.now());
					}
				} else {
					currentUser.setSecretKey(Base32.random());
					currentUser = userRepo.save(currentUser);
					user.setShowVerficationField(true);
					user.setDisplayOtpLink(true);
					user.setDisplayPass(true);
					user.setQrCodeUrl(generateQrCode(currentUser.getEmailAddress(), currentUser.getSecretKey()));
					if(user.isMobileView()) {
						user.setOtpLabel("KEY ->");
						user.setGeneratedCode(currentUser.getSecretKey());
					}
				}
			} else {
				user.setShowVerficationField(false);
				user.setDisplayOtpLink(false);
				user.setDisplayPass(true);
			}
		} else {
			user.setDisplayErrorMessage(true);
			return "index.html";
		}
		return "loginForm.html";
	}
	
	
	@GetMapping("/resetAuthPage")
	public String resetAuthPage(Model model, HttpServletRequest request) {
		AuthRequest req = new AuthRequest();
		Map<String, String[]> parameters = request.getParameterMap();
		if (parameters.containsKey("error")) {
			req.setDisplayErrorMessage(true);
		} else {
			req.setDisplayErrorMessage(false);
		}
		System.out.println("Display Error Message " + req.isDisplayErrorMessage());
		req.setDisplayEmailField(true);
		model.addAttribute("authRequest", req);
		return "ResetAuthenticator.html";
	}
	
	@PostMapping("/resetAuth")
	public String resetAuth(@ModelAttribute("authRequest") AuthRequest user, HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserInfo currentUser = userRepo.findByEmailAddress(user.getUserName());
		if(currentUser != null) {
			if(currentUser.getPassword().equalsIgnoreCase(user.getPassword())) {
				if(currentUser.getSecretKey() != null) {
					currentUser.setSecretKey(Base32.random());
					currentUser = userRepo.save(currentUser);
					user.setQrCodeUrl(generateQrCode(currentUser.getEmailAddress(), currentUser.getSecretKey()));
					user.setDisplayOtpLink(true);
					return "reset-password-template.html";
				}
				
			} else {
				user.setDisplayErrorMessage(true);
				redirectStrategy.sendRedirect(request, response, BAD_CREDENTIALS_FAILURE_URL);
			}
		}
		return "ResetAuthenticator.html";
	}
	
	public String generateQrCode(String emailAddress, String secretKey) throws UnsupportedEncodingException {
		String url = QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s",
				APP_NAME, emailAddress, secretKey, APP_NAME), "UTF-8");
		return url;
	}
	
	@GetMapping("/resetAuthEmailTemplate")
	public String resetAuthEmailTemplate(Model model) {
		AuthRequest req = new AuthRequest();
		req.setDisplayOtpLink(false);
		model.addAttribute("authRequest", req);
		return "reset-password-template.html";
	}
}
