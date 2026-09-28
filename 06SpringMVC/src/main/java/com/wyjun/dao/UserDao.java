package com.wyjun.dao;

import com.wyjun.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {
    private static List<User> users = new ArrayList<>();

    //模拟数据
    static {
        User user1 = new User(10001L, "张三", "zhangsan@wyjun.com", 1);
        User user2 = new User(10002L, "李四", "lisi@wyjun.com", 1);
        User user3 = new User(10003L, "王五", "wangwu@wyjun.com", 1);
        User user4 = new User(10004L, "赵六", "zhaoliu@wyjun.com", 0);
        User user5 = new User(10005L, "钱七", "qianqi@wyjun.com", 0);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
    }

    public List<User> getAllUsers() {
        return users;
    }

    public static long generateId() {
        //找出最大值，然后再加1
        Long maxId = users.stream().map(User::getId).max(Long::compareTo).get();
        return maxId + 1;
    }

    public void addUser(User user) {
        user.setId(generateId());//设置user的id，否则id不会自增，一直为null
        users.add(user);
    }

    public User getUserById(Long id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    public void updateUserById(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (user.getId().equals(users.get(i).getId())) {
                users.set(i, user);
                break;
            }
        }
    }

    public void deleteUserById(Long id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}