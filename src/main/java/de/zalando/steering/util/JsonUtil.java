package de.zalando.steering.util;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = //
        new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
                          .setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

    public static String toJson(final Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(String.format("serializing to json failed : %s", object), e);
        }
    }

}
