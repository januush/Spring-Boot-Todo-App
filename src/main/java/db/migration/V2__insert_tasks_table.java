package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class V2__insert_tasks_table extends BaseJavaMigration {
    @Override
    public void migrate(final Context context) {
        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(),true))
                .execute("INSERT INTO tasks (description, done) VALUES ('Task from migration', true)");
    }
}