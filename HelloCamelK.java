// camel-k: language=java

import org.apache.camel.builder.RouteBuilder;

public class HelloCamelK extends RouteBuilder {
  @Override
  public void configure() throws Exception {

      // Write your routes here, for example:
      from("timer:java?period=1000")
        .routeId("java")
        .setBody()
          .simple("Hello Camel K from ${routeId}. Env var is ${env:AZURE_STORAGE_CONNECTION_STRING}")
        .to("log:info");

  }
}
