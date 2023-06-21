// camel-k: language=java
// camel-k: dependency=camel-azure-storage-blob

import org.apache.camel.builder.RouteBuilder;

public class Personal extends RouteBuilder {

  // @BindToRegistry("creds")
  // public StorageCredentials datasoure() {
  // return new StorageCredentialsAccountAndKey("sanjay-container",
  // "sp=r&st=2023-06-21T04:24:57Z&se=2023-06-21T12:24:57Z&spr=https&sv=2022-11-02&sr=c&sig=Y11pFzqq9V3iWS8Ul9JXKFw%2BW5yu81Cqw7ITC93Mh8Q%3D");
  // }

  @Override
  public void configure() throws Exception {

    // StorageSharedKeyCredential credential = new StorageSharedKeyCredential("yourAccountName", "yourAccessKey");
    // String uri = String.format("https://%s.blob.core.windows.net", "yourAccountName");

    // BlobServiceClient client = new BlobServiceClientBuilder()
    //     .endpoint(uri)
    //     .credential(credential)
    //     .buildClient();

    // getCamelContext().getRegistry().bind("client", client);

    from("azure-storage-blob://mysasanjbh1975/sanjay-container?" +
        "blobName=hr.json&" +
        "credentialType=SHARED_ACCOUNT_KEY&" +
        "accessKey=RAW(0fPr1LvEX9/XwY8DY70jql/jgS5YOSpek7DMdyV0/IcjbNHOcSUZEwmXN/J40xqVScIryWzJ+PBZ+AStMXlDKw==)")
        .split().jsonpath("$.[*]")
        .log("${body}")
        .to("direct:eyecolor");

    from("direct:eyecolor")
        .setHeader("firstName").jsonpath("$.name.first")
        .setHeader("lastName").jsonpath("$.name.last")
        .setHeader("eyeColor").jsonpath("$.eyeColor")
        .log("${headers.firstName} ${headers.lastName} with eye color ${headers.eyeColor}")
        .split().jsonpath("$.friends[*]")
        .log("${body}")
        .to("direct:friends");

    from("direct:friends")
        .setHeader("friendName").jsonpath("$.name")
        .log("${headers.friendName}");
  }
}
