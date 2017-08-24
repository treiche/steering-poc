package de.zalando.steering;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import org.reactivestreams.Subscription;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class ReactorSnippetsTest {

    private static List<String> words = Arrays.asList("the", "quick", "brown", "fox", "jumped", "over", "the", "lazy",
            "dog");

    @Test
    public void simpleCreation() {
        Flux<String> fewWords = Flux.just("Hello", "World");
        Flux<String> manyWords = Flux.fromIterable(words);

        fewWords.subscribe(System.out::println);
        System.out.println();
        manyWords.subscribe(System.out::println);
    }

    @Test
    public void findingMissingLetter() {
        Flux<String> manyLetters = Flux.fromIterable(words).flatMap(word ->
                                               Flux.fromArray(word.split(""))).distinct().sort().zipWith(Flux.range(1,
                                               Integer.MAX_VALUE),
                                           (string, count) ->
                                               String.format("%2d. %s", count, string));

        manyLetters.subscribe(System.out::println);
    }

    @Test
    @Ignore
    public void testFlux() {

        final Random RANDOM = new Random();

        Flux<String> flux = Flux.range(1, 10).map(item -> {
                if (item <= 3) {
                    return "item: " + item;
                } else {
                    System.out.println(">> Exception occurs on map()");

                    throw new RuntimeException();

                }
            });

        System.out.println("=== do when error ===");
        flux.doOnError(e -> System.out.println("doOnError: " + e)).subscribe(System.out::println);

        System.out.println("=== fall back to a default value ===");
        flux.onErrorReturn("onErrorReturn: Value!").subscribe(System.out::println);

        System.out.println("=== fall back to another Flux ===");
        flux.onErrorResume(e -> {
                System.out.println("-> inside onErrorResumeWith()");
                return Flux.just(1, 2).map(item -> "-> new Flux item: " + item);
            }).subscribe(System.out::println);

        System.out.println("=== retry ===");
        flux.retry(1).doOnError(System.out::println).subscribe(System.out::println);

        System.out.println("=== retry with Predicate ===");
        flux.retry(1,
                e -> {
                    boolean shouldRetry = RANDOM.nextBoolean();
                    System.out.println("shouldRetry? -> " + shouldRetry);
                    return shouldRetry;
                }).doOnError(System.out::println).subscribe(System.out::println);

        System.out.println("=== deal with backpressure Error ===");
        flux.onBackpressureError().doOnError(System.out::println).subscribe(new BaseSubscriber<String>() {

                @Override
                protected void hookOnSubscribe(final Subscription subscription) {
                    System.out.println("Subscriber > request only 1 item...");
                    request(1);
                }

                @Override
                protected void hookOnNext(final String value) {
                    System.out.println("Subscriber > process... [" + value + "]");
                }
            });

        System.out.println("===  dropping excess values ===");
        flux.onBackpressureDrop(item -> System.out.println("Drop: [" + item + "]")).doOnError(System.out::println)
            .subscribe(new BaseSubscriber<String>() {

                    @Override
                    protected void hookOnSubscribe(final Subscription subscription) {
                        System.out.println("Subscriber > request only 1 item...");
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(final String value) {
                        System.out.println("Subscriber > process... [" + value + "]");
                    }
                });

        System.out.println("===  buffer excess values ===");

        BaseSubscriber<String> subscriber = new BaseSubscriber<String>() {

            @Override
            protected void hookOnSubscribe(final Subscription subscription) {
                System.out.println("Subscriber > request only 1 item...");
                request(1);
            }

            @Override
            protected void hookOnNext(final String value) {
                System.out.println("Subscriber > process... [" + value + "]");
            }
        };

        flux.onBackpressureBuffer(2, item -> System.out.println("Buffer: [" + item + "]"))
            .doOnError(System.out::println).subscribe(subscriber);

        System.out.println("Subscriber > request more items:");
        subscriber.request(1);

        System.out.println("=== catch and rethrow ===");
        flux.map(e -> new RuntimeException("mapError")).subscribe(System.out::println);
    }

}
