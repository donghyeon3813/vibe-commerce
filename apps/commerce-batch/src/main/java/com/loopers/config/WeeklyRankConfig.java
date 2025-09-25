package com.loopers.config;

import com.loopers.domain.MvProductRankWeekly;
import com.loopers.infrastructure.MvProductRankRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class WeeklyRankConfig {
    private final MvProductRankRepository mvProductRankRepository;

    @Bean
    public Job weeklyRankJob(JobRepository jobRepository, Step weeklyRankingStep) {
        return new JobBuilder("weeklyRankingJob", jobRepository)
                .start(weeklyRankingStep)
                .build();
    }

    @Bean
    public Step weeklyRankingStep(JobRepository jobRepository,
                                  PlatformTransactionManager tx,
                                  JpaPagingItemReader<MvProductRankWeekly> reader,
                                  ItemWriter<MvProductRankWeekly> writer) {
        return new StepBuilder("weeklyRankingStep", jobRepository)
                .<MvProductRankWeekly, MvProductRankWeekly>chunk(100, tx)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<MvProductRankWeekly> weeklyReader(EntityManagerFactory emf) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        String jpql = """
        SELECT new com.loopers.domain.MvProductRankWeekly(
            p.productId,
            :targetDate,
            SUM(p.likeCount * 0.2 + p.viewCount * 0.1 + p.saleCount * 0.7)
        )
        FROM ProductMetrics p
        WHERE p.metricsDate BETWEEN :start AND :end
        GROUP BY p.productId
    """;

        return new JpaPagingItemReaderBuilder<MvProductRankWeekly>()
                .name("weeklyReader")
                .entityManagerFactory(emf)
                .pageSize(200)
                .queryString(jpql)
                .parameterValues(Map.of(
                        "start", startDate,
                        "end", endDate,
                        "targetDate", endDate
                ))
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<MvProductRankWeekly> weeklyWriter() {
        return mvProductRankRepository::saveAll;
    }

}
