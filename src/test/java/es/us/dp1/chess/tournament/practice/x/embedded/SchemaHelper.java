package es.us.dp1.chess.tournament.practice.x.embedded;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

final class SchemaHelper {

    private SchemaHelper() {
    }

    static boolean hasColumn(DataSource ds, String table, String column) {
        Integer n = new JdbcTemplate(ds).queryForObject("SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE UPPER(TABLE_NAME) = ? AND UPPER(COLUMN_NAME) = ?", Integer.class,
                table.toUpperCase(), column.toUpperCase());
        return n != null && n > 0;
    }

    static boolean hasTable(DataSource ds, String table) {
        Integer n = new JdbcTemplate(ds).queryForObject("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES "
                + "WHERE UPPER(TABLE_NAME) = ?", Integer.class, table.toUpperCase());
        return n != null && n > 0;
    }

}
