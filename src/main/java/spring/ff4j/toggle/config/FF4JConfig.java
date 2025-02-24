package spring.ff4j.toggle.config;

import jakarta.annotation.PostConstruct;
import java.net.URI;
import org.ff4j.FF4j;
import org.ff4j.cache.InMemoryCacheManager;
import org.ff4j.core.Feature;
import org.ff4j.dynamodb.feature.FeatureStoreDynamoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class FF4JConfig {

  @Bean
  public FF4j ff4j() {
    FF4j ff4j = new FF4j();

    // Configure DynamoDB client
    DynamoDbClient dynamoDbClient =
        DynamoDbClient.builder()
            .httpClient(UrlConnectionHttpClient.create()) // Explicitly setting HTTP client
            .endpointOverride(URI.create("http://localhost:4566"))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("test", "test") // LocalStack default credentials
                    ))
            .overrideConfiguration(
                ClientOverrideConfiguration.builder()
                    .retryPolicy(
                        RetryPolicy.none()) // Avoid automatic retries on misconfigured services
                    .build())
            .region(Region.US_EAST_1)
            .build();

    // Set up DynamoDB Feature Store
    ff4j.setFeatureStore(new FeatureStoreDynamoDB(dynamoDbClient));

    ff4j.audit(false);
    ff4j.cache(new LoggingCacheManager(3600));
    ff4j.autoCreate(false);

    return ff4j;
  }

  public static class LoggingCacheManager extends InMemoryCacheManager {

    private final Logger log = LoggerFactory.getLogger(LoggingCacheManager.class);

    public LoggingCacheManager(int timeToLive) {
      super(timeToLive);
    }

    @Override
    public Feature getFeature(String uid) {
      Feature feature = super.getFeature(uid);
      if (feature == null) {
        log.info("Cache MISS for feature: {}", uid);
      } else {
        log.info("Cache HIT for feature: {}", uid);
      }
      return feature;
    }
  }

  @PostConstruct
  public void init() {
    System.out.println("🚀 FF4JConfig initialized!");
  }
}
