package com.gd.orh.userMgt.controller;

import com.gd.orh.dto.UserDTO;
import com.gd.orh.entity.ResultCode;
import com.gd.orh.entity.User;
import com.gd.orh.userMgt.service.UserService;
import com.gd.orh.utils.ImageUploadUtil;
import com.gd.orh.utils.RestResultFactory;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {

        String userImage = userDTO.getUserImage();

        // 保存用户头像
        if (ImageUploadUtil.isImageNotEmpty(userImage)) {

            String imageContent = ImageUploadUtil.getImageContent(userImage);

            if (imageContent == null) {
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("The upload file is not a image!"));
            }

            InputStream in = ImageUploadUtil.getInputStreamFromImageContent(imageContent);

            boolean isUploadFailed = !ImageUploadUtil
                    .uploadImageToRootPath(
                            "images/user/",
                            id + ".jpg",
                            in,
                            "jpg");

            if (isUploadFailed) {
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("Upload user's image failed!"));
            }
        }

        // Make sure the user id is assigned.
        userDTO.setUserId(id);

        User user = userDTO.convertTo();

        // Update user.
        User updatedUser = userService.update(user);

        UserDTO updatedUserDTO = new UserDTO().convertFor(updatedUser);

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

        users.forEach(each -> userDTOs.add(new UserDTO().convertFor(each)));

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(userDTOs));
    }
}
