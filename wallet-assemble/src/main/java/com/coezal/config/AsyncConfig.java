package com.coezal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Version 1.0
 * Created by lll on 2019-09-06.
 * Description
 *
 * <pre>
 *   线程池配置
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */


@Configuration
@EnableAsync
public class AsyncConfig {

  @Value("${corePoolSize}")
  private int corePoolSize;

  @Value("${maxPoolSize}")
  private int maxPoolSize;

  @Value("${queueCapacity}")
  private int queueCapacity;


  @Bean
  public Executor taskExecutor(){
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setQueueCapacity(queueCapacity);
    executor.initialize();
    return executor;
  }


}
