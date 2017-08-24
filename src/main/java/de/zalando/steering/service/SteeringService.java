package de.zalando.steering.service;

import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import de.zalando.steering.model.TestModel;
import de.zalando.steering.nakadi.Event.MasterDataType;
import de.zalando.steering.nakadi.Event.MasterdataEvent;
import de.zalando.steering.util.JsonUtil;

@Service
public class SteeringService {

    private static final Logger LOG = LoggerFactory.getLogger(SteeringService.class);

    private static final Function<MasterdataEvent, TestModel> TRANSFORM = e -> {
        TestModel model = new TestModel();
        model.setCode(e.getData().getCode());
        model.setType(e.getData().getMasterDataType().name());
        model.setEventType(e.getMetadata().getEventType());
        model.setOperation(e.getDataOp().name());
        model.setOccurredAt(e.getMetadata().getOccurredAt());
        return model;
    };

    public MasterdataEvent processEvent(final MasterdataEvent event) {
        if (event.getData().getMasterDataType() == MasterDataType.BRAND) {
            String json = JsonUtil.toJson(TRANSFORM.apply(event));
            LOG.info("event transformed int: {}", json);
            return event;
        } else {
            throw new RuntimeException("boom!!!!! -> " + event.getData());
        }
    }

    public void processEvents(final List<MasterdataEvent> events) {

        LOG.info("process grouped {} events.", events.size());

        events.forEach(event -> {
            if (event.getData().getMasterDataType() == MasterDataType.BRAND) {
                String json = JsonUtil.toJson(TRANSFORM.apply(event));
                LOG.info("event transformed int: {}", json);
            } else {
                throw new RuntimeException("boom!!!!! -> " + event.getData());
            }
        });
    }
}
