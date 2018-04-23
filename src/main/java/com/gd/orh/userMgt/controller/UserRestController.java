package com.gd.orh.userMgt.controller;

import com.gd.orh.dto.UserDTO;
import com.gd.orh.entity.ResultCode;
import com.gd.orh.entity.User;
import com.gd.orh.userMgt.service.UserService;
import com.gd.orh.utils.RestResultFactory;
import com.google.common.collect.Lists;
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
    public ResponseEntity<?> update(@PathVariable("id") Long id, UserDTO userDTO) {

        MultipartFile userImage = userDTO.getUserImage();

        // If the user image is exists, save it.
        if (userImage != null && !userImage.isEmpty()) {
            File rootDirectory = null;
            try {
                // Get root path if project was deployed in the Windows
                rootDirectory =
                        new File(ResourceUtils.getURL("classpath:").getPath());

                // The root directory will not exists when the project was deployed in the Linux,
                // so use this way to get root directory.
                if (!rootDirectory.exists()) rootDirectory = new File("");
            } catch (FileNotFoundException e) { }

            // Set the upload path is /static/images/user.
            File uploadDirectory =
                new File(rootDirectory.getAbsolutePath(),"static/images/user/");

            // if the upload directory is not exists, create the directory.
            if (!uploadDirectory.exists()) uploadDirectory.mkdirs();

            // Save the user Image in the /static/images/user.
            try {
                userImage.transferTo(new File(uploadDirectory, id + ".jpg"));
            } catch (IOException e) {
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("User Image saving failed!"));
            }
        }

        // Make sure the user id is assigned.
        userDTO.setUserId(id);

        User user = userDTO.convertToUser();

        // Update user.
        user = userService.update(user);

        UserDTO updatedUserDTO = new UserDTO().convertFor(user);

        // Return updated user.
        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(updatedUserDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(RestResultFactory.getFreeResult(
                        ResultCode.NOT_FOUND,
                        "Not found user with id: " + id + "!",
                        null
                    ));
        }

        UserDTO userDTO = new UserDTO().convertFor(user);

        // Return user.
        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(userDTO));
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "10") Integer rows) {
        User user = new User();
        user.setPage(page);
        user.setRows(rows);

        List<User> users = userService.findAll(user);
        List<UserDTO> userDTOs = Lists.newArrayList();

        for (User each: users) {
            userDTOs.add(new UserDTO().convertFor(each));
        }

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(userDTOs));
    }
}
