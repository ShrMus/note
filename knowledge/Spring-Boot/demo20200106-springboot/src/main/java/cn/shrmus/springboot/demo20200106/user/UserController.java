package cn.shrmus.springboot.demo20200106.user;

import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }
}
