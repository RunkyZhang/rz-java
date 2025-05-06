package com.rz.web.demo.server.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContactDto implements Serializable {
    private String name;
    private String code;
}
