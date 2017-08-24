package de.zalando.steering.nakadi;

import java.io.IOException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;

import java.util.List;
import java.util.function.Function;

import org.reactivestreams.Publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Service;

import org.zalando.fahrschein.StreamParameters;

import org.zalando.fashionmovers.fahrschein.ListenerHandler;
import org.zalando.fashionmovers.fahrschein.ManualSubscriber;
import org.zalando.fashionmovers.fahrschein.NakadiListener;
import org.zalando.fashionmovers.fahrschein.Subscribe2Nakadi;
import org.zalando.fashionmovers.fahrschein.SubscriptionId;

import de.zalando.steering.SteeringProperties;
import de.zalando.steering.nakadi.Event.MasterdataEvent;
import de.zalando.steering.service.SteeringService;

import reactor.core.Exceptions;

import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

/**
 * https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html.
 */
@Service
@Subscribe2Nakadi
public class EventListener implements ManualSubscriber, NakadiListener<MasterdataEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EventListener.class);

    private final SteeringProperties props;
    private final SteeringService steeringService;

    @Autowired
    public EventListener(final SteeringProperties props, final SteeringService steeringService) {
        this.props = props;
        this.steeringService = steeringService;
    }

    @Override
    public void subscribe(final ListenerHandler listenerHandler) throws IOException {
        listenerHandler.register(this,
            new SubscriptionId(props.getApplicationName(), props.getConsumerGroup(), props.getEventName()));
    }

    @Override
    public Class<MasterdataEvent> getEventType() {
        return MasterdataEvent.class;
    }

    @Override
    public void accept(final MasterdataEvent masterdataEvent) { }

    @Override
    public void accept(final List<MasterdataEvent> events) {

        LOG.info("Number of events received: {}", events.size());

        final Function<Flux<Throwable>, Publisher<?>> retryFunction = companion ->
                companion.doOnNext(throwable -> LOG.error("{} at {}", throwable, LocalTime.now()))    // log the time of  errors
                         .zipWith(Flux.range(1, 4),
                             (throwable, index) -> {
                                 if (index < 4) {
                                     return index;
                                 } else {
                                     throw Exceptions.propagate(throwable);
                                 }
                             })                                                                       // use the retryWhen + zipWith trick to propagate the error after 3 retries
                         .flatMap(index -> Mono.delay(Duration.ofMillis(index * 100)))                // cause a delay that depends on the attempt’s index (exponential backoff)
                         .doOnNext(s -> LOG.info("retried at {}", LocalTime.now()));                  // log the time at which the retry effectively occurs

        final Instant[] start = new Instant[1];
        final int[] eventCount = {0};

        Flux<GroupedFlux<String, MasterdataEvent>> flux =
            Flux.fromIterable(events) //

                // For each Subscriber, tracks this Flux values that have been seen and filters out duplicates given
                // the extracted key.
                // .distinct(masterdataEvent -> masterdataEvent.getData().getCode())         //

                // Transform the items emitted by this Flux into Publishers, then flatten the emissions from those by
                // merging them into a single Flux, so that they may interleave. The concurrency argument allows to
                // control how many merged Publisher can happen in parallel.
                // .flatMap(event ->
                // Flux.just(steeringService.processEvent(event)).subscribeOn(Schedulers.parallel()),
                // props.getFluxConcurrency())
                .groupBy(masterdataEvent -> masterdataEvent.getData().getCode()) // Re-route this sequence into
                                                                                 // dynamically created Flux for each
                                                                                 // unique key evaluated by the given
                                                                                 // key mapper.
                .retryWhen(retryFunction)                                        // added retry (see

                // retryFunction)
                .doOnSubscribe(subscription -> {
                    start[0] = Instant.now();
                    subscription.request(9223372036854775807L);
                })                                                                              // start measurement
                                                                                                // (number of items to
                                                                                                // request taken from
                                                                                                // BaseSubscriber.class)
                .doOnEach(masterdataEventSignal -> eventCount[0]++)                             // count number of
                                                                                                // elements. Triggers
                                                                                                // side-effects when the
                                                                                                // Flux emits an item,
                                                                                                // fails with an error
                                                                                                // or completes
                                                                                                // successfully. All
                                                                                                // these events are
                                                                                                // represented as a
                                                                                                // Signal that is passed
                                                                                                // to the side-effect
                                                                                                // callback. Note that
                                                                                                // this is an advanced
                                                                                                // operator, typically
                                                                                                // used for monitoring
                                                                                                // of a Flux.
                .doOnComplete(() ->
                        LOG.info("processing completed in {} ms for {} events",
                            (Duration.between(start[0], Instant.now())).toMillis(), eventCount[0])) // stop measurement
                .doOnError(throwable ->
                        LOG.error(throwable.getMessage(), throwable))                           // log message in case
                                                                                                // of an error
            ;

        flux.subscribe(groupedFlux -> groupedFlux.collectList().subscribe(steeringService::processEvents)); // subscribe and start processing (grouped) events
    }

    @Bean
    public StreamParameters getStreamParameters() {
        return
            new StreamParameters().withStreamTimeout(props.getNakadiStreamTimeout()) //
                                  .withBatchLimit(props.getNakadiBatchLimit())       //
                                  .withMaxUncommittedEvents(props.getNakadiMaxUncommittedEvents());
    }

}
