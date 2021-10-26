package org.vfd.face_recg_service.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vfd.face_recg_service.models.GenericResponse;
import org.vfd.face_recg_service.models.VerifyBvnRequest;
import org.vfd.face_recg_service.services.IndexOpsService;

@RestController
@RequestMapping("face-recg-api")
@Validated
public class IndexController {
	private IndexOpsService indexOpsService;

	public IndexController(IndexOpsService indexOpsService) {
		this.indexOpsService = indexOpsService;
	}

	@PostMapping("/send-otp/{bvn}")
	public ResponseEntity<GenericResponse> sendOtp(@PathVariable String bvn) {
		GenericResponse response = indexOpsService.sendOtp(bvn);

		if (response.getStatus() != null && response.getStatus() == 1) {
			return ResponseEntity.ok(response);
		} else if (indexOpsService.isBadRequest(response.getStatus())) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.internalServerError().body(response);
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<GenericResponse> sendOtp(@RequestBody VerifyBvnRequest verifyBvnRequest,
			HttpServletRequest servletRequest) {
		GenericResponse response = indexOpsService.verifyOtp(verifyBvnRequest, servletRequest);

		if (response.getStatus() != null && response.getStatus() == 1) {
			return ResponseEntity.ok(response);
		} else if (indexOpsService.isBadRequest(response.getStatus())) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.internalServerError().body(response);
	}

	@PostMapping("/face-recognition")
	public ResponseEntity<GenericResponse> doFaceRecognition(@RequestParam MultipartFile Image,
			@RequestParam String BVN, HttpServletRequest servletRequest) {
		GenericResponse response = indexOpsService.doFaceRecognition(Image, BVN, servletRequest);

		if (response.getStatus() != null && response.getStatus() == 1) {
			return ResponseEntity.ok(response);
		} else if (indexOpsService.isBadRequest(response.getStatus())) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.internalServerError().body(response);
	}

	// VERIFY OTP
	// DO FAVE VALIDATION
}
