//package com.zzf.rpc;
//
//import com.zzf.annotation.RemoteInvoke;
//import com.zzf.bean.User;
//import com.zzf.remote.UserRemoteTest;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.ArrayList;
//
///**
// * @author zzf
// * @date 2022/7/31 5:33 下午
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = RemoteInvokeTest.class)
//@ComponentScan("com.zzf ")
//public class RemoteInvokeTest {
//
//    @RemoteInvoke
//    private UserRemoteTest userRemote;
//
//    @Test
//    public void testSaveUser() {
//        User user = new User();
//        user.setId(1);
//        user.setName("zzf");
//         userRemote.saveUser(user);
//    }
//
//    @Test
//    public void testSaveUsers() {
//        ArrayList<User> users = new ArrayList<>();
//        User user = new User();
//        user.setId(1);
//        user.setName("zzf");
//        users.add(user);
//        User user2 = new User();
//        user2.setId(2);
//        user2.setName("zzf2");
//        users.add(user2);
//        userRemote.saveUsers(users);
//    }
//}
