package com.example.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.UserRequest;
import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired 
	private UserRepository userRepository;
	
	public String generateOTP() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }
	
	
	public User registerUser(UserRequest newUser) {
		
		User user = new User();
		
		user.setName(newUser.getName());
		user.setMobileNumber(newUser.getMobileNumber());
		user.setFingerprint(newUser.getFingerprint());
		user.setVerified(false);
		
		return userRepository.save(user);
	}
	
	
	public void generateOtpForLogin(String mobileNumber) {
        Optional<User> userOpt = userRepository.findByMobileNumber(mobileNumber);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String otp = generateOTP();
            user.setOtp(otp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
            userRepository.save(user);

        } else {
            throw new RuntimeException("User not registered with this mobile number");
        }
    }
	
	
	public void resendOtp(String mobileNumber) {
	    Optional<User> userOpt = userRepository.findByMobileNumber(mobileNumber);

	    if (userOpt.isPresent()) {
	        User user = userOpt.get();
	        String newOtp = generateOTP();
	        user.setOtp(newOtp);
	        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5)); // Reset OTP expiry time
	        userRepository.save(user);

	    } else {
	        throw new RuntimeException("User not found for this mobile number");
	    }
	}

	
	
	public boolean validateOtp(String mobileNumber, String otp) {
        Optional<User> userOpt = userRepository.findByMobileNumber(mobileNumber);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (user.getOtp().equals(otp) && !user.getOtpExpiry().isBefore(LocalDateTime.now())) {

                user.setVerified(true);
                userRepository.save(user);
                System.out.println("User verified: " + user.getMobileNumber());
                return true;
            }
        }
        return false;
    }


	public boolean existsByPhoneNo(String mobileNumber) {
		Optional<User> userOpt = userRepository.findByMobileNumber(mobileNumber);
		if(userOpt.isPresent()) {
			return true;
		}else {
			return false;
		}
		
	}
	
	

}
