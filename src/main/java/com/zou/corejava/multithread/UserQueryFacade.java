package com.zou.corejava.multithread;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author leonard
 * @date 2022/11/8
 * @Description https://mp.weixin.qq.com/s/N7hzj4IR-di_KEgOts5OBg
 * <p>
 * 异步线程 + CountDownLatch + Future 实现无耦合的多数据查询
 */
public class  UserQueryFacade {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        //test1();
        test2();
    }

    public static void test1() throws InterruptedException {
        long t1 = System.currentTimeMillis();
        User userData = getUserData("199987573889");
        userData.setPosts(getPosts("199987573889"));
        userData.setFollows(getFollows("199987573889"));
        System.out.println(userData);
        System.out.println("cost:" + (System.currentTimeMillis() - t1) / 1000 + "s");
    }

    public static void test2() throws InterruptedException, ExecutionException {
        long t1 = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch cl = new CountDownLatch(3);

        Future<User> userFuture = executorService.submit(() -> {
            try {
                return getUserData("199987573889");
            } finally {
                cl.countDown();

            }
        });
        Future<List<Post>> postFuture = executorService.submit(() -> {
            try {
                return getPosts("199987573889");
            } finally {
                cl.countDown();
                ;

            }
        });
        Future<List<User>> followsFuture = executorService.submit(() -> {
            try {
                return getFollows("199987573889");
            } finally {
                cl.countDown();
                ;
            }
        });

        cl.await();

        User userData = userFuture.get();
        userData.setPosts(postFuture.get());
        userData.setFollows(followsFuture.get());
        System.out.println(userData);
        System.out.println("cost:" + (System.currentTimeMillis() - t1) / 1000 + "s");
        executorService.shutdown();
    }


    public static User getUserData(String id) throws InterruptedException {
        Thread.sleep(3000);
        User user = new User();
        user.setId(id);
        user.setName("leonard");
        user.setEmail("leonard@163.com");
        return user;
    }

    public static List<Post> getPosts(String id) throws InterruptedException {

        Thread.sleep(2000);

        Post post = new Post();
        post.setTitle("DevOps");
        post.setContent("aws,k8s,pipeline");
        return Collections.singletonList(post);
    }

    public static List<User> getFollows(String id) throws InterruptedException {
        Thread.sleep(5000);
        List<User> follows = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(String.valueOf(i));
            user.setName("name" + i);
            user.setEmail("email" + i + "@fox.com");
            follows.add(user);
        }
        return follows;
    }

    @Data
    static class User{
        private String id;
        private String name;
        private String email;
        private List<Post> posts;
        private List<User> follows;
    }

    @Data
    static class Post {

        private String title;
        private String content;
    }
}
