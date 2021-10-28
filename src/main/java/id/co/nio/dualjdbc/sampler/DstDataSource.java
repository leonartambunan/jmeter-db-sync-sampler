package id.co.nio.dualjdbc.sampler;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log.Logger;
import java.sql.Connection;
import java.sql.SQLException;

public class DstDataSource {
    private static DstDataSource INSTANCE;
    private HikariDataSource hikariDataSource = null;

    private DstDataSource(Logger log, String driver, String url, String username, String password) throws Exception {
        try {

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setDriverClassName(driver);

            int maxPool = 500;
            config.setMaximumPoolSize(maxPool);
            config.setMinimumIdle(3);
            config.setAutoCommit(true);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            hikariDataSource = new HikariDataSource(config);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static DstDataSource getInstance(Logger log,String driver, String url, String username, String password) throws Exception {
        if (INSTANCE == null) {
            INSTANCE = new DstDataSource(log,driver,url,username,password);
        }

        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        if (hikariDataSource==null) {
            throw new SQLException("hikariDataSource is null");
        }
        return hikariDataSource.getConnection();
    }
}
