
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;


public final class Application {

    private Application() {
    }

    public static void main(String[] args) {
        try (CamelContext context = new DefaultCamelContext()) {

            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            context.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));


            try {
                context.addRoutes(new MyRouteBuilder());
                context.start();
                Thread.sleep(1000);
                context.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    static class MyRouteBuilder extends RouteBuilder {

        @Override
        public void configure() {
            from("file:C:\\Users\\juris\\lia").process(new Processor() {
                        public void process(Exchange exchange) {
                            System.out.println(exchange.getIn().getBody());
                        }
                    })
                    .to("test-jms:queue:foo.bar");
        }
    }
}