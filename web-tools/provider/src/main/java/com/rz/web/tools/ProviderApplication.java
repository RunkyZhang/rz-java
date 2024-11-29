package com.rz.web.tools;

import com.rz.core.utils.StreamUtility;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MapperScan(basePackages = "com.rz.web.tools.mapper")
@SpringBootApplication
public class ProviderApplication {
    public static void main(String[] args) throws Exception {
        readInvokeLog();

        SpringApplication.run(ProviderApplication.class, args);
    }

    private static void readInvokeLog() throws IOException {
        Map<String, Integer> map = new HashMap<>();
        StreamUtility.readFileProcessLine("/Users/runkyzhang/Downloads/8080.log", StandardCharsets.UTF_8, line -> {
            if (null == line) {
                return false;
            }


            List<String> httpMethods = Arrays.asList("HTTP: GET ", "HTTP: POST ", "HTTP: OPTIONS ", "HTTP: HEAD ", "HTTP: TRACE ", "HTTP: PUT ", "HTTP: DELETE ", "HTTP: PATCH ", "HTTP: CONNECT ");

            for (String httpMethod : httpMethods) {
                if (line.contains(httpMethod) && !line.contains("/nacos")) {
                    String[] lineSplit = line.split(httpMethod);

                    String tracedayLine = "/sfa/gaode/traceday/";
                    String invokePath = httpMethod;
                    if (lineSplit[1].startsWith(tracedayLine)) {
                        invokePath += tracedayLine;
                    } else {
                        invokePath += lineSplit[1];
                    }

                    if (map.containsKey(invokePath)) {
                        int count = map.get(invokePath);
                        map.put(invokePath, count + 1);
                    } else {
                        map.put(invokePath, 1);
                    }
                }
            }
            return true;
        });

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "=====" + entry.getValue());
        }
    }
}
