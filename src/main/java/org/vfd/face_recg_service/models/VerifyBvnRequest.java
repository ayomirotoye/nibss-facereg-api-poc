package org.vfd.face_recg_service.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyBvnRequest {
	@JsonProperty("BVN")
	private String bvn;
	@JsonProperty("OTP")
	private String otp;
}
