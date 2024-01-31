package com.zou.corejava.webflux1;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;


/**
 * @author leonard
 * @date 2022/12/17
 * @Description TODO
 */
public class PSVM {

    public static void main(String[] args) {
        //rxTest1();
        rxTest2();
    }

    /**
     * rxJava入门详解
     * https://blog.csdn.net/jaynm/article/details/106101981
     */
    public static void rxTest2(){
        Observable<String> observable = Observable.just("kawhi", "Leonard");

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("action1 call2343" + s);
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable t) {
                System.err.println("action1 Throwable" + t.getMessage());
            }
        };

        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                System.out.println("completed");
            }
        };

        //observable.subscribe(onNextAction);
        //observable.subscribe(onNextAction,onErrorAction);
        observable.subscribe(onNextAction,onErrorAction,onCompletedAction);

    }

    public static void rxTest1() {
        // 1. 创建被观察者 Observable 对象
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Leonard");
                subscriber.onNext("Kawhi");
                subscriber.onCompleted();
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("error:"+throwable.getMessage());
            }

            @Override
            public void onNext(String s) {
                System.out.println("next:"+s);
            }
        };

        new Subscriber<String>(){

            @Override
            public void onStart() {
                System.out.println("onStart..");
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(String s) {

            }
        };

        observable.subscribe(observer);


    }


    public static void main1(String[] args) {
        //createFluxObjectByStaticWay();
        //createFluxObjectByGenerator();
        //createMono();
        //operator1();
        //errorOperator1();
        //threadModel();
    }

    /**
     * 使用 publishOn 指定了流发布的调度器，subscribeOn 则指定的是流订阅的调度器。 首先是 parallel 调度器进行流数据的生成，
     * 接着使用一个 single 单线程调度器进行发布，此时经过 第一个 map 转换为另一个 Flux 流
     * 其中的消息叠加了当前线程的名称。最后进入的是一个 elastic 弹性调度器，再次进行一次同样的 map 转换
     */
    private static void threadModel() {
        Flux.create(sink -> {
            sink.next(Thread.currentThread().getName());
            sink.complete();
        }).publishOn(Schedulers.single())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .publishOn(Schedulers.elastic())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .subscribeOn(Schedulers.parallel()).toStream().forEach(System.out::println);
    }


    public static void createFluxObjectByStaticWay() {
        Flux.just("hello", "world").subscribe(System.out::println);
        Flux.fromArray(new Integer[]{1, 2, 3}).subscribe(a -> System.out.println(a));
        Flux.range(1, 10).subscribe(System.out::println);
        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);
    }

    public static void createFluxObjectByGenerator() {

        Flux.create(t -> {
            for (int i = 0; i < 10; i++) {
                t.next(i);
            }
            t.complete();
        }).subscribe(System.out::println);

        Flux.generate(t -> {
            t.next("hello");
            t.complete();
        }).subscribe(System.out::println);

    }


    public static void createMono() {
        Mono.just("mono").subscribe(System.out::print);

        Mono.error(new Exception("some error")).subscribe(System.out::print,
                System.err::print,
                () -> System.out.println("completed"));

        //Flux.just(1, 2, 3, 4, 5, 6)仅仅声明了这个数据流，此时数据元素并未 发出，只有 subscribe()方法调用的时候才会触发数据流
        //订阅前什么都不会发生
        Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("Completed!"));
    }

    private static void operator1() {
        //Flux.range(1, 5).filter(t -> t % 2 == 0).map(t -> t * 2).subscribe(System.out::println);


        //Flux.just("Hello", "World").flatMap(item -> Flux.fromArray(item.split("\\s*"))).subscribe(System.out::println);

        //doOnNext 方法是“偷窥式”的方法，不会消 费数据流
        //Flux.just("Hello", "World").flatMap(item -> Flux.fromArray(item.split("\\s*"))).doOnNext(System.out::println).subscribe(System.out::println);


        //zip 1对1合并
        Flux.zip(Flux.range(1, 10), Flux.range(100, 10)).concatMap(item -> {
            System.out.println(item);
            Optional<Object> reduce = item.toList().stream().reduce((a, b) -> Integer.parseInt(a.toString()) + Integer.parseInt(b.toString()));
            return Flux.just(reduce.get());
        }).subscribe(System.out::println, System.err::println);

    }

    private static void errorOperator1() {
        //Flux.just(1, 2).concatWith(Mono.error(new IllegalStateException())).subscribe(System.out::println, System.err::println);
        Flux.just(1, 2).concatWith(Mono.error(new IllegalArgumentException())).onErrorResume(e -> {
            if (e instanceof IllegalStateException) {
                return Mono.just(0);
            } else if (e instanceof IllegalArgumentException) {
                return Mono.just(-1);
            }
            return Mono.empty();
        }).subscribe(System.out::println);
    }


}
