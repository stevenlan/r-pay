package com.rpay.controller.vo;

import lombok.Data;

/**
 * @author steven
 */
@Data
public class Approve {
    private String options ;
    private Long userId ;
    private String esp ;
    private String minValue ;
    private String reason ;
}
