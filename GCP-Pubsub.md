# GCP Spring boot integration for publishing message to a pub/sub topic:

Add dependency com.google.cloud:google-cloud-pubsub:2021.0.5

1. Add properties in properties file. These include:
projectId,
clientId
privateKey
privateKeyId
clientEmail (service account email)
topicName
gcp-type: such as oAuth, or service_account

2. Create Gcp pojo class with below fields.
clientId (@JsonProperty(client_id))
privateKey (@JsonProperty(private_key))
privateKeyId (@JsonProperty(private_key_id))
clientEmail (@JsonProperty(client_email))

3. Create GCPCOnfig class:

Instantiate all properties in this class.
private GoogleCredentials googleCredentials;

// Create init() method.
public void init() {
    Gcp gcp = new Gcp(clientId, privateKey, privateKeyId, clientEmail);
    String stringifiedGcp = objectMapper.writeValueAsString(gcp);
    googleCredentials = GoogleCredentials.fromStream(new ByteArrayInputStream(writeValueAsString.getBytes()));
}

// Create GoogleCredentials method.
public GoogleCredentials getCredentials() {
  if (googleCredentials == null) {
      init();
  }
  return googleCredentials;
}

4. PubsubMessageSender

// Read from properties file
private String topicId;

Topic topic = Topic.from(projectId, topicId);
Publisher publisher = Publisher
                              .newBuilder(topicName)
                              .setCredentialsProvider(FixedCredentialsProvider.create(gcpConfig.getGoogleCredentials()))
                              .build();

            PubsubMessage pubsubMessage =
                    PubsubMessage.newBuilder()
                            .setData(ByteString.copyFromUtf8(message))
                            .putAllAttributes(attributes)
                            .build();

            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
            String messageId = messageIdFuture.get();


---------------------------------------------------------------------------------------------------------------------------

# GCP Spring boot integration for listening from pub/sub subscription:

Dependency:
implementation platform('com.google.cloud:spring-cloud-gcp-dependencies:3.4.9')
'com.google.cloud:spring-cloud-gcp-starter-pubsub'

Properties:
spring.cloud.gcp.pubsub.project-id: 
spring.cloud.gcp.pubsub.subscriber.subscription-name:
spring.cloud.gcp.pubsub.credentials.encoded-key:
// for local-testing:
spring.cloud.gcp.pubsub.project-id:
spring.cloud.gcp.pubsub.emulator-host: localhost:8081


// Create bean of Pusub input channel adapter
@Bean
  public PubSubInboundChannelAdapter messageChannelAdapter(@Qualifier("pubsubInputChannel") MessageChannel inputChannel, PubSubTemplate pubSubTemplate) {
      PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, subscriptionName);
      adapter.setOutputChannel(inputChannel);
      adapter.setAckMode(AckMode.MANUAL);

      return adapter;
  }

// Create Bean of MessageChannel
@Bean
public DirectChannel pubsubInputChannel() {
    return new DirectChannel();
}

// Read message
@Bean
@ServiceActivator(inputChannel = "pubsubInputChannel")
public MessageHandler messageReceiver() {
    return message -> {

        BasicAcknowledgeablePubsubMessage originalMessage = message.getHeaders().get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class);
        originalMessage.ack();
        PubsubMessage pubsubMessage = Objects.requireNonNull(originalMessage.getPubsubMessage());
        Map<String, String> attributes = pubsubMessage.getAttributesMap();

        String payloadString = pubsubMessage.getData().toString(StandardCharsets.UTF_8);
}
