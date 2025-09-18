package com.loopers.config;

import com.loopers.domain.MvProductRankMonthly;
import com.loopers.infrastructure.MvProductRankMonthlyRepository;
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
public class MonthlyRankConfig {

    private final MvProductRankMonthlyRepository mvProductRankMonthlyRepository;

    @Bean
    public Job monthlyRankJob(JobRepository jobRepository, Step monthlyRankingStep) {
        return new JobBuilder("monthlyRankingJob", jobRepository)
                .start(monthlyRankingStep)
                .build();
    }

    @Bean
    public Step monthlyRankingStep(JobRepository jobRepository,
                                   PlatformTransactionManager tx,
                                   JpaPagingItemReader<MvProductRankMonthly> reader,
                                   ItemWriter<MvProductRankMonthly> writer) {
        return new StepBuilder("monthlyRankingStep", jobRepository)
                .<MvProductRankMonthly, MvProductRankMonthly>chunk(100, tx)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<MvProductRankMonthly> monthlyReader(EntityManagerFactory emf) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29); // 최근 30일 집계

        String jpql = """
        SELECT new com.loopers.domain.MvProductRankMonthly(
            p.productId,
            :targetDate,
            SUM(p.likeCount * 0.2 + p.viewCount * 0.1 + p.saleCount * 0.7)
        )
        FROM ProductMetrics p
        WHERE p.metricsDate BETWEEN :start AND :end
        GROUP BY p.productId
    """;

        return new JpaPagingItemReaderBuilder<MvProductRankMonthly>()
                .name("monthlyReader")
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
    public ItemWriter<MvProductRankMonthly> monthlyWriter() {
        return mvProductRankMonthlyRepository::saveAll;
    }
}
