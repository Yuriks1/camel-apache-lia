
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
                Thread.sleep(60000);
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
            from("file:C:\\Users\\juris\\lia").convertBodyTo(String.class).process(exchange -> {
             System.out.println("Exchange id:"+ exchange.getExchangeId());
                System.out.println("Headers :"+ exchange.getMessage().getHeaders());
                System.out.println(exchange.getIn().getBody());
                exchange.getMessage().setHeader("CamelFileName",exchange.getMessage().getHeader("CamelFileName") + ".out");
            })
                    .to("file:C:\\Users\\juris\\out")
                    .log("File Name: ${header.CamelFileName}, Body:${body} ")
                    .to("test-jms:queue:foo.bar");
        }
    }
}