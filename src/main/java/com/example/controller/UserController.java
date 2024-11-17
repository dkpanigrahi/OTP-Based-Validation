package com.example.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.UserRequest;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest user) {
		Map<String, String> response = new HashMap<>(); 
        
        try {
                     
            if (userService.existsByPhoneNo(user.getMobileNumber())) {
                response.put("error", "Phone number is already taken");
                return ResponseEntity.badRequest().body(response);
            }
            
            userService.registerUser(user);

            response.put("message", "User registered successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateOtp(@RequestParam String mobileNumber) {
        userService.generateOtpForLogin(mobileNumber);
        return new ResponseEntity<>("OTP Successfully sent to the Registered Mobile Number",HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String mobileNumber, @RequestParam String otp) {
        if (userService.validateOtp(mobileNumber, otp)) {
            
                return new ResponseEntity<>("Login Successful.", HttpStatus.OK);
            
        }
        return new ResponseEntity<>("Invalid OTP.", HttpStatus.BAD_REQUEST);
    }

    
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String mobileNumber) {
        userService.resendOtp(mobileNumber);
        return new ResponseEntity<>("A new OTP has been sent to" + mobileNumber,HttpStatus.OK);
    }
    
    @GetMapping("/getUser/{userId}")
	public ResponseEntity<?> getCurrentUser(@PathVariable int userId) {
	    Optional<User> optionalUser = userRepository.findById(userId);
	    
	    if (optionalUser.isPresent()) {
	        User user = optionalUser.get();
	        if (user.isVerified()) {
	            return new ResponseEntity<>(user, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Please Login to Access this URL...", HttpStatus.BAD_REQUEST);
	        }
	    } else {
	        return new ResponseEntity<>("User Not Found..", HttpStatus.NOT_FOUND);
	    }
	}
    
    @PostMapping("/logout/{userId}")
    public ResponseEntity<?> logout(@PathVariable int userId){
    	Optional<User> optionalUser = userRepository.findById(userId);
	    
	    if (optionalUser.isPresent()) {
	        User user = optionalUser.get();
	        if (user.isVerified()) {
	            user.setVerified(false);
	            user.setOtp(null);
	            user.setOtpExpiry(null);
	            userRepository.save(user);
	            return new ResponseEntity<>("LogOut Successful", HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Please Login to Access this URL...", HttpStatus.BAD_REQUEST);
	        }
	    } else {
	        return new ResponseEntity<>("User Not Found..", HttpStatus.NOT_FOUND);
	    }
    }
    
    

	
}
