package org.example.simpleonlinestore.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;
@Configuration
public class RateLimitConfig {

    @Bean
    public Bucket bucket(){
        // Define the bandwidth with a limit of 5 tokens, refilled every minute
        Bandwidth limit=Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }
}