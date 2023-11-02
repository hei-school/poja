package school.hei.poja;

import school.hei.poja.endpoint.event.EventConf;
import school.hei.poja.endpoint.event.EventConsumer;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import software.amazon.awssdk.services.sqs.SqsClient;

import static school.hei.poja.endpoint.event.EventConsumer.toAcknowledgeableTypedEvent;

@Slf4j
public class MailboxEventHandler implements RequestHandler<SQSEvent, String> {

  @Override
  public String handleRequest(SQSEvent event, Context context) {
    log.info("Following received events : {}\n", event);
    List<SQSEvent.SQSMessage> messages = event.getRecords();
    log.info("Following received messages : {}\n", messages);

    ConfigurableApplicationContext applicationContext = applicationContext();
    EventConsumer eventConsumer = applicationContext.getBean(EventConsumer.class);
    EventConf eventConf = applicationContext.getBean(EventConf.class);
    SqsClient sqsClient = applicationContext.getBean(SqsClient.class);

    eventConsumer.accept(toAcknowledgeableTypedEvent(eventConf, sqsClient, messages));

    applicationContext.close();
    return "{message: ok}";
  }

  private ConfigurableApplicationContext applicationContext(String... args) {
    return SpringApplication.run(PojaApplication.class, args);
  }
}
