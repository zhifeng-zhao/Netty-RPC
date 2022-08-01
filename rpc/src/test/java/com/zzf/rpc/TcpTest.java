//package com.zzf.rpc;
//
//import com.zzf.bean.User;
//import com.zzf.client.TcpClient;
//import com.zzf.model.ClientRequest;
//import com.zzf.model.Response;
//import org.junit.Test;
//
//import java.util.ArrayList;
//
///**
// * @author zzf
// * @date 2022/7/30 6:07 下午
// */
//public class TcpTest {
//    @Test
//    public void testGetResponse() {
//        ClientRequest request = new ClientRequest();
//        request.setContent("测试长连接");
//        Response response = TcpClient.send(request);
//        System.out.println(response.getContent());
//    }
//
//    @Test
//    public void testSaveUser() {
//        ClientRequest request = new ClientRequest();
//        User user = new User();
//        user.setId(1);
//        user.setName("zzf");
//        request.setContent(user);
//        request.setCommand("com.zzf.controller.UserControllersaveUser");
//        Response response = TcpClient.send(request);
//        System.out.println(response.getContent());
//    }
//
//    @Test
//    public void testSaveUsers() {
//        ClientRequest request = new ClientRequest();
//        ArrayList<User> users = new ArrayList<>();
//        User user = new User();
//        user.setId(1);
//        user.setName("zzf");
//        users.add(user);
//        User user2 = new User();
//        user2.setId(2);
//        user2.setName("zzf2");
//        users.add(user2);
//        request.setContent(users);
//        request.setCommand("com.zzf.controller.UserControllersaveUsers");
//        Response response = TcpClient.send(request);
//        System.out.println(response.getContent());
//    }
//}
