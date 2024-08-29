package com.fatichdev.ecommerce.exception;

import lombok.EqualsAndHashCode;
import lombok.Data;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException{
    private final String msg;
}
