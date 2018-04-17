package com.gd.orh.userMgt.controller;

import com.gd.orh.entity.ResultCode;
import com.gd.orh.entity.User;
import com.gd.orh.userMgt.service.UserService;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

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
    private UserService userService;

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @ModelAttribute User updatedUser) {
        MultipartFile userImage = updatedUser.getUserImage();

        // If the user image is existed, save it.
        if (userImage != null && !userImage.isEmpty()) {
            File rootDirectory = null;
            try {
                // Get root path
                // If the project was deployed in the Windows, the first way is in effect,
                // otherwise, the second way will in effect when the project was deployed in the Linux.
                rootDirectory =
                    new File(ResourceUtils.getURL("classpath:").getPath());
                if (!rootDirectory.exists()) rootDirectory = new File("");
            } catch (FileNotFoundException e) { }

            // Set the upload path is /static/images/user.
            File uploadDirectory =
                new File(rootDirectory.getAbsolutePath(),"static/images/user/");
            if (!uploadDirectory.exists()) uploadDirectory.mkdirs();

            // Save the user Image in the /static/images/user.
            try {
                userImage.transferTo(new File(uploadDirectory, id + ".jpg"));
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(
                        RestResultFactory.getFailResult("User Image saving failed!"));
            }
        }

        // Make sure the user id is assigned.
        updatedUser.setId(id);
        // Update user.
        User user = userService.update(updatedUser);
        // Return updated user.
        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    RestResultFactory.getFreeResult(
                            ResultCode.NOT_FOUND, "Not found user with" + id, null));
        }

        // Return user.
        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(user));
    }

    @GetMapping
    public ResponseEntity<?> findAll(User user) {
        List<User> users = userService.findAll(user);
        return ResponseEntity.ok(
                RestResultFactory.getSuccessResult().setData(users));
    }
}
