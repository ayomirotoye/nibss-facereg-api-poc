package org.vfd.face_recg_service.enums;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

public enum ResponseCodeEnum {
	SUCCESSFUL(1, "Success", "Success"), FAILED(0, "Failed", "Failed"), BAD_REQUEST(400, "Bad request", ""),
	UNAUTHORIZED(401, "Unauthorized", ""), UNSUPPORTED_MEDIA_TYPE(405, "Unsupported media type", ""),
	METHOD_NOT_ALOWED(406, "Method not allowed", ""), SOCKET_TIMEOUT(503, "I/O timeout ... Please try again later", ""),
	ERROR_OCCURRED(500, "Internal service error or unknown error", ""),
	SERVICE_UNAVAILABLE(503, "Service provider endpoint is not reachable", "");

	@Getter
	@Setter
	public Integer responseCode;
	@Getter
	@Setter
	public String responseMessage;
	@Getter
	@Setter
	public String description;

	private ResponseCodeEnum(Integer responseCode, String responseMessage, String description) {
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.description = description;
	}

	public static HashMap<Integer, Object> getEnumValues() {
		ResponseCodeEnum[] enumArr = ResponseCodeEnum.values();
		HashMap<Integer, Object> myEnumMap = new HashMap<>();
		for (int i = 0; i < enumArr.length; i++) {
			ResponseCodeEnum myCode = enumArr[i];
			myEnumMap.put(myCode.getResponseCode(), myCode.getResponseMessage());
		}
		return myEnumMap;
	}

	public static ResponseCodeEnum getEnumFromValue(Integer data) {
		ResponseCodeEnum[] enumArr = ResponseCodeEnum.values();
		for (ResponseCodeEnum en : enumArr) {
			if (en.getResponseCode() == data) {
				return en;
			}
		}
		return ResponseCodeEnum.FAILED;
	}

	public static boolean isSuccessful(ResponseCodeEnum data) {
		return data == ResponseCodeEnum.SUCCESSFUL;
	}
}
