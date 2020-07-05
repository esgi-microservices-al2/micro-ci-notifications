package ci.microservice.notification.amqp;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class EventModel {

    private String projectId;
    private String date;
    private String test;
    private String type;
    private String build;

    public EventModel(@JsonProperty("projectId") String projectId,
                      @JsonProperty("date") String date,
                      @JsonProperty("test") String test,
                      @JsonProperty("type") String type,
                      @JsonProperty("build") String build) {
        this.projectId = projectId;
        this.date = date;
        this.test = test;
        this.type = type;
        this.build = build;
    }
}

