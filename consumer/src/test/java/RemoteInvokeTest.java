
import com.zzf.client.annotation.RemoteInvoke;
import com.zzf.client.parm.Response;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.zzf.user.bean.User;
import com.zzf.remote.UserRemote;

import java.util.ArrayList;

/**
 * @author zzf
 * @date 2022/7/31 5:33 下午
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RemoteInvokeTest.class)
@ComponentScan("com.zzf")
public class RemoteInvokeTest {

    @RemoteInvoke
    private UserRemote userRemote;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setId(1);
        user.setName("zzf");
        Response response = userRemote.saveUser(user);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void testSaveUsers() {
        ArrayList<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1);
        user.setName("zzf");
        users.add(user);
        User user2 = new User();
        user2.setId(2);
        user2.setName("zzf2");
        users.add(user2);
        Response response = userRemote.saveUsers(users);
        System.out.println(JSONObject.toJSONString(response));
    }
}
