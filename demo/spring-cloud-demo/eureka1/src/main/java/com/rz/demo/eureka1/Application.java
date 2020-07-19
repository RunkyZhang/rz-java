package com.rz.demo.eureka1;

import com.rz.core.RZHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.util.PriorityQueue;

@SpringBootApplication
@EnableEurekaServer
public class Application {
    public static void main(String[] args) throws Exception {
        topK(3, new int[]{2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5, 6, 4, 3, 3, 43, 4, 5, 5, 6, 4, 3, 3, 4, 4, 4, 4, 32, 55, 3, 45, 4,});


        System.out.println(RZHelper.getIpV4());

        SpringApplication.run(Application.class, args);
    }


    static PriorityQueue<Integer> queue;
    static int maxHeapSize;

    public static void topK(int k, int[] nums) {
        queue = new PriorityQueue<Integer>(k);
        maxHeapSize = k;
        if (k < nums.length) {
            for (int i = 0; i < k; i++) {
                queue.add(nums[i]);
            }
            for (int j = k; j < nums.length; j++) {
                if (nums[j] > queue.peek()) {
                    queue.poll();
                    queue.offer(nums[j]);
                }
            }
        } else {
            for (Integer integer : nums) {
                queue.add(integer);
            }
        }
    }
}
