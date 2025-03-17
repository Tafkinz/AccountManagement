package ee.tafkin.config;

import ee.tafkin.errorhandling.InternalException;
import org.h2.tools.TriggerAdapter;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AuditTrigger extends TriggerAdapter {
  private static final String UPDATE_AUDIT_FIELD = "MODIFIED_DTIME";
  private static final String INSERT_AUDIT_FIELD = "CREATED_DTIME";

  @Override
  public void fire(Connection connection, ResultSet oldRow, ResultSet newRow) throws SQLException {
    validateRowsExist(newRow, super.tableName);
    switch (super.type) {
      case 1 -> newRow.updateTimestamp("CREATED_DTIME", Timestamp.valueOf(LocalDateTime.now()));
      case 2 -> newRow.updateTimestamp("MODIFIED_DTIME", Timestamp.valueOf(LocalDateTime.now()));
    }
  }

  private static void validateRowsExist(ResultSet newRow, String tableName) {
    if (newRow != null) {
      List.of(UPDATE_AUDIT_FIELD, INSERT_AUDIT_FIELD).forEach(field -> {
        try {
          newRow.findColumn(field);
        }
        catch (SQLException e) {
          throw new InternalException("Table %s is missing audit rows".formatted(tableName));
        }
      });
    }
  }
}
