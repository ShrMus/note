package cn.shrmus.springboot.demo20200106.exception;

import org.springframework.boot.ExitCodeGenerator;

public class CustomizingException extends Exception implements ExitCodeGenerator {
    @Override
    public int getExitCode() {
        return 43;
    }
}
