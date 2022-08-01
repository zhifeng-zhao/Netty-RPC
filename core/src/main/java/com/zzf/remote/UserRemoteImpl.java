package com.zzf.remote;

import com.zzf.annotation.Remote;
import com.zzf.model.User;
import com.zzf.model.Response;
import com.zzf.service.UserService;
import com.zzf.utils.ResponseUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zzf
 * @date 2022/7/31 5:28 下午
 */
@Remote
public class UserRemoteImpl implements UserRemote {

    @Resource
    private UserService userService;

    @Override
    public Response saveUser(User user) {
        userService.save(user);
        return ResponseUtil.createSuccessResult(user);
    }

    @Override
    public Response saveUsers(List<User> users) {
        userService. saveList(users);
        return ResponseUtil.createSuccessResult(users);
    }
}
