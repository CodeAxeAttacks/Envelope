package com.envelope.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class PatchUserDto {
    @Size(max = 64, message = "Field's \"firstname\" length must be lower than 65 symbols")
    private final String firstName;

    @Size(max = 64, message = "Field's \"lastname\" length must be lower than 65 symbols")
    private final String lastName;

    @Size(max = 12, message = "Field's \"phone\" length must be lower than 13 symbols")
    private final String phone;

}
