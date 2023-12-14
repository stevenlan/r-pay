package com.rpay.controller.vo;

import lombok.Data;

/**
 * @author steven
 */
@Data
public class Password {
    private String email ;
    private String phone ;
    private String checkCode ;
    private String oldPass ;
    private String newPass ;
}
