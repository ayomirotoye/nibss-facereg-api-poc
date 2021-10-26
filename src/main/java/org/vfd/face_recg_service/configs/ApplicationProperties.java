package org.vfd.face_recg_service.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class ApplicationProperties {
	@Value("${nibss.api.base_url}")
	private String nibssApiBaseUrl;
	@Value("${nibss.api.endpoints.send_otp}")
	private String sendOtpEndpt;
	@Value("${nibss.api.endpoints.verify_otp}")
	private String verifyOtpEndpt;
	@Value("${nibss.api.endpoints.face_recognition}")
	private String faceRecgEndpt;
	@Value("${nibss.api.username}")
	private String nibssApiUsername;
	@Value("${nibss.api.password}")
	private String nibssApiPassword;
	@Value("${nibss.api.failed_response_codes}")
	private String failedResponseCodes;
}
