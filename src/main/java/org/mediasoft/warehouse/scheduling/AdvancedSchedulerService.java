package org.mediasoft.warehouse.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mediasoft.warehouse.annotations.MeasureExecutionTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


@Slf4j
@Component
@Profile("!local")
@ConditionalOnExpression("${scheduler.optimization} == true && ${scheduler.enabled} == false")
@RequiredArgsConstructor
public class AdvancedSchedulerService {
    private final JdbcTemplate jdbcTemplate;
    final String filePath = "src/main/resources/updatedProducts.txt";

    @Value(value = "${scheduler.pricePercentage}")
    private BigDecimal pricePercentage;
    private final String updateSqlQuery = "UPDATE products SET price = price + (price * (?/100)) RETURNING *";
    private final String lockQuery = "LOCK TABLE products IN ACCESS EXCLUSIVE MODE";
    private ResultSet resultSet;


    @SneakyThrows
    @Scheduled(fixedRateString = "${scheduler.timing}")
    @MeasureExecutionTime
    public void advancedUpdatePrice() {
        log.info("START advancedUpdatePrice");
        Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
        connection.setAutoCommit(false);
        var pstmt = connection.prepareStatement(updateSqlQuery);
        try {
            connection.prepareStatement(lockQuery).execute();
            pstmt.setBigDecimal(1, pricePercentage);
            resultSet = pstmt.executeQuery();
        } catch (SQLException sqlException) {
            connection.rollback();
            sqlException.printStackTrace();
        }
        connection.commit();

        saveToFile(resultSet, filePath);
        connection.close();

        log.info("END advancedUpdatePrice");
    }

    @SneakyThrows
    private void saveToFile(ResultSet rs, String filePath) {
        File file = new File(filePath);

        StringBuilder row = new StringBuilder();

        FileWriter writer = new FileWriter(file, true);

        while (rs.next()) {
            row.append(rs.getObject("id").toString())
                    .append(",")
                    .append(rs.getString("name"))
                    .append(",")
                    .append(rs.getString("article"))
                    .append(",")
                    .append(rs.getString("dictionary"))
                    .append(",")
                    .append(rs.getString("category"))
                    .append(",")
                    .append(rs.getBigDecimal("price").toString())
                    .append(",")
                    .append(rs.getBigDecimal("qty"))
                    .append(",")
                    .append(rs.getString("inserted_at"))
                    .append(",")
                    .append(rs.getString("last_qty_changed"))
                    .append("\n");
        }

        writer.write(String.valueOf(row));

        writer.close();
    }

}