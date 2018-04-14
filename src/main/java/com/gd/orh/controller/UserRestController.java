package com.gd.orh.controller;

import com.gd.orh.entity.User;
import com.gd.orh.security.repository.UserRepository;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
//    @Value("${jwt.header}")
//    private String tokenHeader;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    @Qualifier("jwtUserDetailsService")
//    private UserDetailsService userDetailsService;

//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping
//    public List<User> getUsers() {
//        return repository.findAll();
//    }
//
//    @PostAuthorize("returnObject.username == principal.username or hasRole('ADMIN')")
//    @GetMapping("/{id}")
//    public User getUserById(@PathVariable("id") Long id) {
//        return repository.getOne(id);
//    }
//
//    @PreAuthorize("hasRole('PASSENGER') && #username == principal.username or hasRole('ADMIN')")
//    @GetMapping("/search/findByUsername/{username}")
//    public User getUserByUsername(@PathVariable("username") String username) {
//        return repository.findByUsername(username);
//    }
//
//    @GetMapping("/auth")
//    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
//        String token = request.getHeader(tokenHeader).substring(7);
//        String username = jwtTokenUtil.getUsernameFromToken(token);
//        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
//        return user;
//    }

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @ModelAttribute User updatedUser) {
        MultipartFile userImage = updatedUser.getUserImage();

        if (userImage != null && !userImage.isEmpty()) {
            // 获取根目录
            File rootDirectory = null;
            try {
                rootDirectory = new File(ResourceUtils.getURL("classpath:").getPath());
                if (!rootDirectory.exists()) rootDirectory = new File("");
            } catch (FileNotFoundException e) { }
            // 上传目录为/static/images/user
            File uploadDirectory = new File(rootDirectory.getAbsolutePath(),"static/images/user/");
            if (!uploadDirectory.exists()) uploadDirectory.mkdirs();

            try {
                userImage.transferTo(new File(uploadDirectory, id + ".jpg"));
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(RestResultFactory.getFailResult("User Image saving failed!"));
            }
        }

        User user = userRepository.getOne(id);
        user.setNickname(updatedUser.getNickname());
        user.setGender(updatedUser.getGender());
        user.setAge(updatedUser.getAge());

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(userRepository.save(user)));
    }
}
