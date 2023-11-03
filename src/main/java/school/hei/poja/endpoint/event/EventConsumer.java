package school.hei.poja.endpoint.event;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import school.hei.poja.PojaGenerated;
import school.hei.poja.endpoint.event.gen.UuidCreated;
import school.hei.poja.endpoint.event.model.TypedEvent;
import school.hei.poja.endpoint.event.model.TypedUuidCreated;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

@PojaGenerated
@Component
@Slf4j
public class EventConsumer implements Consumer<List<EventConsumer.AcknowledgeableTypedEvent>> {
  public static final String DETAIL_TYPE_PROPERTY = "detail-type";
  private static final ObjectMapper om = new ObjectMapper();
  private static final String DETAIL_ROPERTY = "detail";
  private final EventServiceInvoker eventServiceInvoker;

  public EventConsumer(EventServiceInvoker eventServiceInvoker) {
    this.eventServiceInvoker = eventServiceInvoker;
  }

  public static List<AcknowledgeableTypedEvent> toAcknowledgeableTypedEvent(
      EventConf eventConf, SqsClient sqsClient, List<SQSEvent.SQSMessage> messages) {
    var res = new ArrayList<AcknowledgeableTypedEvent>();
    for (SQSEvent.SQSMessage message : messages) {
      TypedEvent typedEvent;
      try {
        typedEvent = toTypedEvent(message);
      } catch (Exception e) {
        log.error(e.getMessage());
        log.error("Message could not be unmarshalled, message : %s \n", message);
        continue;
      }
      AcknowledgeableTypedEvent event =
          new AcknowledgeableTypedEvent(
              typedEvent,
              () ->
                  sqsClient.deleteMessage(
                      DeleteMessageRequest.builder()
                          .queueUrl(eventConf.getSqsQueue())
                          .receiptHandle(message.getReceiptHandle())
                          .build()));
      res.add(event);
    }
    return res;
  }

  private static TypedEvent toTypedEvent(SQSEvent.SQSMessage message)
      throws JsonProcessingException {
    TypedEvent typedEvent;
    TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
    Map<String, Object> body = om.readValue(message.getBody(), typeRef);
    String typeName = body.get(DETAIL_TYPE_PROPERTY).toString();
    if (UuidCreated.class.getTypeName().equals(typeName)) {
      UuidCreated uuidCreated = om.convertValue(body.get(DETAIL_ROPERTY), UuidCreated.class);
      typedEvent = new TypedUuidCreated(uuidCreated);
    } else {
      throw new RuntimeException("Unexpected message type for message=" + message);
    }
    return typedEvent;
  }

  @Override
  public void accept(List<AcknowledgeableTypedEvent> ackTypedEvents) {
    for (AcknowledgeableTypedEvent ackTypedEvent : ackTypedEvents) {
      eventServiceInvoker.accept(ackTypedEvent.getTypedEvent());
      ackTypedEvent.ack();
    }
  }

  @AllArgsConstructor
  public static class AcknowledgeableTypedEvent {
    @Getter private final TypedEvent typedEvent;
    private final Runnable acknowledger;

    public void ack() {
      acknowledger.run();
    }
  }
}
