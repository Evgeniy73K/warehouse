package org.mediasoft.warehouse.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mediasoft.warehouse.annotations.Stopwatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Slf4j
@Component
@Profile("!local")
@ConditionalOnProperty(value = "scheduler.optimization", havingValue = "true")
@RequiredArgsConstructor
public class AdvancedSchedulerService {
    private final JdbcTemplate jdbcTemplate;

    @Value(value = "${scheduler.pricePercentage}")
    private BigDecimal pricePercentage;


    @Scheduled(fixedRateString = "${scheduler.timing}")
    @Transactional
    @Stopwatch
    @SneakyThrows
    public void advancedUpdatePrice() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        connection.setAutoCommit(false);
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM products",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                rs.updateBigDecimal("price", rs.getBigDecimal("price")
                        .add(rs.getBigDecimal("price").divide(BigDecimal.valueOf(100)).multiply(pricePercentage))
                        .setScale(2, RoundingMode.HALF_UP));
            }
            connection.commit();
        } finally {
            connection.close();
        }
    }


}