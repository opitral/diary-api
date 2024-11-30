package com.opitral.diaryapi.common;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CommonResponse<T> {
    CommonResponseStatus status;
    T message;

    public static <T> CommonResponse<T> ok(T message) {
        return CommonResponse.<T>builder()
            .status(CommonResponseStatus.ok)
            .message(message)
            .build();
    }

    public static <T> CommonResponse<T> error(T message) {
        return CommonResponse.<T>builder()
            .status(CommonResponseStatus.error)
            .message(message)
            .build();
    }
}
