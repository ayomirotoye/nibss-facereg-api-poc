package org.vfd.face_recg_service.services;

import java.nio.charset.Charset;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.vfd.face_recg_service.configs.ApplicationProperties;
import org.vfd.face_recg_service.configs.RestConnector;
import org.vfd.face_recg_service.enums.ResponseCodeEnum;
import org.vfd.face_recg_service.models.GenericResponse;
import org.vfd.face_recg_service.models.GetBvnRequest;
import org.vfd.face_recg_service.models.VerifyBvnRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndexOpsService {

	private RestConnector restConnector;
	private ApplicationProperties appProps;
	private HttpHeaders httpHeaders;
	private ObjectMapper objectMapper;

	@Autowired
	public IndexOpsService(RestConnector restConnector, ApplicationProperties appProperties,
			ObjectMapper objectMapper) {
		this.restConnector = restConnector;
		this.appProps = appProperties;
		this.objectMapper = objectMapper;
	}

	@PostConstruct
	public void setHeaders() {
		httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(appProps.getNibssApiUsername(), appProps.getNibssApiPassword(),
				Charset.forName("US-ASCII"));
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	}

	public GenericResponse sendOtp(String bvn) {
		GenericResponse response = GenericResponse.builder().build();
		try {
			httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			httpHeaders.setBasicAuth(appProps.getNibssApiUsername(), appProps.getNibssApiPassword(),
					Charset.forName("US-ASCII"));
			String urlToCall = appProps.getNibssApiBaseUrl().concat(appProps.getSendOtpEndpt());
			GetBvnRequest request = GetBvnRequest.builder().bvn(bvn).build();
			ResponseEntity<String> getRes = restConnector.exchange(objectMapper.writeValueAsString(request), urlToCall,
					httpHeaders, HttpMethod.POST);

			if (getRes.getStatusCode().is2xxSuccessful()) {
				response = objectMapper.readValue(getRes.getBody(), GenericResponse.class);
			} else {
				if (getRes.hasBody()) {
					response = objectMapper.readValue(getRes.getBody(), GenericResponse.class);
				} else {
					response.setMessage(ResponseCodeEnum.FAILED.getResponseMessage());
					response.setStatus(ResponseCodeEnum.FAILED.getResponseCode());
				}
			}

		} catch (Exception e) {
			log.error("===========ERROR OCCURRED WHILE SENDING OTP=========");
			e.printStackTrace();
			response.setMessage(ResponseCodeEnum.ERROR_OCCURRED.getResponseMessage());
			response.setStatus(ResponseCodeEnum.ERROR_OCCURRED.getResponseCode());
		}
		return response;
	}

	public GenericResponse verifyOtp(VerifyBvnRequest verifyBvnRequest, HttpServletRequest httpServletRequest) {
		GenericResponse response = GenericResponse.builder().build();
		try {
			String xAccessToken = (String) httpServletRequest.getHeader("x-access-token");
			if (xAccessToken == null || xAccessToken.isEmpty()) {
				response.setMessage(ResponseCodeEnum.BAD_REQUEST.getResponseMessage()
						.concat(" | Some headers not set : x-access-token "));
				response.setStatus(ResponseCodeEnum.BAD_REQUEST.getResponseCode());
				return response;
			}

			httpHeaders.set("x-access-token", xAccessToken);
			String urlToCall = appProps.getNibssApiBaseUrl().concat(appProps.getVerifyOtpEndpt());
			ResponseEntity<String> getRes = restConnector.exchange(objectMapper.writeValueAsString(verifyBvnRequest),
					urlToCall, httpHeaders, HttpMethod.POST);

			if (getRes.getStatusCode().is2xxSuccessful()) {
				response = objectMapper.readValue(getRes.getBody(), GenericResponse.class);
			} else {
				if (getRes.hasBody()) {
					response = objectMapper.readValue(getRes.getBody(), GenericResponse.class);
				} else {
					response.setMessage(ResponseCodeEnum.FAILED.getResponseMessage());
					response.setStatus(ResponseCodeEnum.FAILED.getResponseCode());
				}
			}

		} catch (Exception e) {
			log.error("===========ERROR OCCURRED WHILE SENDING OTP=========");
			e.printStackTrace();
			response.setMessage(ResponseCodeEnum.ERROR_OCCURRED.getResponseMessage());
			response.setStatus(ResponseCodeEnum.ERROR_OCCURRED.getResponseCode());
		}
		return response;
	}

	public GenericResponse doFaceRecognition(MultipartFile file, String BVN,
			HttpServletRequest httpServletRequest) {
		GenericResponse response = GenericResponse.builder().build();
		try {
			String xAccessToken = (String) httpServletRequest.getHeader("x-access-token");
			if (xAccessToken == null || xAccessToken.isEmpty()) {
				response.setMessage(ResponseCodeEnum.BAD_REQUEST.getResponseMessage()
						.concat(" | Some headers not set : x-access-token "));
				response.setStatus(ResponseCodeEnum.BAD_REQUEST.getResponseCode());
				return response;
			}

			httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			httpHeaders.set("x-access-token", xAccessToken);

			LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
			params.add("Image", file.getResource());
			params.add("BVN", BVN);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);

			String urlToCall = appProps.getNibssApiBaseUrl().concat(appProps.getFaceRecgEndpt());

			ResponseEntity<String> getRes = restConnector.exchange(urlToCall, requestEntity, HttpMethod.POST);

			if (getRes.getStatusCode().is2xxSuccessful()) {
				response = objectMapper.readValue(getRes.getBody(), GenericResponse.class);
			} else {
				if (getRes.hasBody()) {
					response = objectMapper.readValue(getRes.getBody(), GenericResponse.class);
				} else {
					response.setMessage(ResponseCodeEnum.FAILED.getResponseMessage());
					response.setStatus(ResponseCodeEnum.FAILED.getResponseCode());
				}
			}

		} catch (Exception e) {
			log.error("===========ERROR OCCURRED WHILE SENDING OTP=========");
			e.printStackTrace();
			response.setMessage(ResponseCodeEnum.ERROR_OCCURRED.getResponseMessage());
			response.setStatus(ResponseCodeEnum.ERROR_OCCURRED.getResponseCode());
		}
		return response;
	}


	public Boolean isBadRequest(Integer resCode) {
		return Arrays.asList(appProps.getFailedResponseCodes().split(",")).contains(String.valueOf(resCode));
	}
}
