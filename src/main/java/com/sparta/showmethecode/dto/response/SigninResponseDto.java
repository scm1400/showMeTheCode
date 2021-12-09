package com.sparta.showmethecode.dto.response;

import com.sparta.showmethecode.service.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SigninResponseDto {
    private Token token;
    private String authority;
    private HttpStatus httpStatus;
    private String message;
}
