package com.repinsky.copywise;

import org.springframework.boot.SpringApplication;

public class TestCopyWiseApplication {

    public static void main(String[] args) {
        SpringApplication.from(CopyWiseApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
