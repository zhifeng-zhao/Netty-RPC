package com.zzf.remote;

import com.zzf.model.Response;
import com.zzf.model.User;

import java.util.List;

/**
 * @author zzf
 * @date 2022/7/31 5:28 下午
 */
public interface UserRemote {

    public Response saveUser(User user);

    public Response saveUsers(List<User> users);
}
