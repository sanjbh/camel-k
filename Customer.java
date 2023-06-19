// camel-k: language=java
// camel-k: dependency=mvn:io.quarkus:quarkus-jdbc-mysql
// camel-k: build-property=quarkus.datasource.camel.db-kind=mysql
// camel-k: config=secret:my-datasource


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class Customer extends RouteBuilder {
  @Override
  public void configure() throws Exception {

      rest()
        .get()
          .path("/customers/{name}").to("direct:get")
          .path("/customers").to("direct:getAll")
        .post()
          .path("/customers").to("direct:post")
        .delete()
          .path("/customers/{name}").to("direct:delete");
        


      from("direct:getAll")
        .routeId("getall")
        .setBody(simple("select * from customers"))
        .to("log:info")
        .to("jdbc:camel")
        .setBody(simple("${bodyAs(String)}"));

      from("direct:get")
        .routeId("get")
        .setBody(simple("select * from customers where name = '${header.name}'"))
        .to("log:info")
        .to("jdbc:camel")
        .setBody(simple("${bodyAs(String)}"));

      from("direct:post")
        .routeId("post")
        .unmarshal()
        .json(JsonLibrary.Jackson)
        .to("log:info")
        .setBody(simple("insert into customers (name, city) values ('${body[name]}', '${body[city]}')"))
        .to("jdbc:camel")
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
        .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
        .setBody(constant("REQUEST OK"));


      from("direct:delete")
        .routeId("delete")
        .setBody(simple("delete from customers where name = '${header.name}'"))
        .to("log:info")
        .to("jdbc:camel")
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(204));


      

  }
}
