package com.rz.web.demo.server;

import com.rz.web.demo.server.schema.ArrayArgument;
import com.rz.web.demo.server.schema.EnumArgument;
import com.rz.web.demo.server.schema.InputSchema;
import com.rz.web.demo.server.schema.StringArgument;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

/**
 * Hello world!
 */
@SpringBootApplication
public class ServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
    }
}
