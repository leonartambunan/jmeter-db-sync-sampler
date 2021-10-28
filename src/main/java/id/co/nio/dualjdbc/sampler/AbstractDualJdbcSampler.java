package id.co.nio.dualjdbc.sampler;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDualJdbcSampler extends AbstractSampler implements TestBean {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private String jdbc1Driver = "";
    private String jdbc2Driver = "";
    private String jdbc1Url = "";
    private String jdbc2Url = "'";
    private String jdbc1Username = "";
    private String jdbc2Username = "";
    private String jdbc1Password = "";
    private String Jdbc2Password = "";

    private String upsertQuery = "";
    private String checkingQuery = "";
    private int connectionTimeout = 5000;
    
    private String failureReason = "Unknown";

    private Connection srcConnection = null;
    private Connection dstConnection = null;

    public AbstractDualJdbcSampler(String name) {
        super();
        setName(name);
    }

    public void connectToSrc() {
        try {
            failureReason = "Unknown";

            srcConnection = SrcDataSource.getInstance(log, jdbc1Driver, jdbc1Url, jdbc1Username, jdbc1Password).getConnection();

        } catch (Exception e) {
            failureReason = e.getMessage();
            try {
                srcConnection.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }

            log.error("JDBC connexion error", e);
        }
    }
    public void connectToDst() {
        try {
            failureReason = "Unknown";
            dstConnection = DstDataSource.getInstance(log,jdbc2Driver, jdbc2Url, jdbc2Username, Jdbc2Password).getConnection();
        } catch (Exception e) {
            failureReason = e.getMessage();
            try {
                dstConnection.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
            log.error("JDBC connexion error", e);
        }
    }

    public void disconnect() {
        if (srcConnection != null) {
            try {
                srcConnection.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }

        if (dstConnection != null) {
            try {
                dstConnection.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public void setUpsertQuery(String upsertQuery) {
        this.upsertQuery = upsertQuery;
    }

    public String getUpsertQuery() {
        return upsertQuery;
    }

    public String getJdbc1Driver() {
        return jdbc1Driver;
    }

    public void setJdbc1Driver(String server) {
        this.jdbc1Driver = server;
    }

    public String getJdbc2Driver() {
        return jdbc2Driver;
    }

    public void setJdbc2Driver(String jdbc2Driver) {
        this.jdbc2Driver = jdbc2Driver;
    }

    public String getJdbc1Url() {
        return jdbc1Url;
    }

    public void setJdbc1Url(String jdbc1Url) {
        this.jdbc1Url = jdbc1Url;
    }

    public String getJdbc2Url() {
        return jdbc2Url;
    }

    public void setJdbc2Url(String jdbc2Url) {
        this.jdbc2Url = jdbc2Url;
    }

    public String getJdbc1Username() {
        return jdbc1Username;
    }

    public void setJdbc1Username(String jdbc1Username) {
        this.jdbc1Username = jdbc1Username;
    }

    public String getJdbc2Username() {
        return jdbc2Username;
    }

    public void setJdbc2Username(String jdbc2Username) {
        this.jdbc2Username = jdbc2Username;
    }

    public String getJdbc1Password() {
        return jdbc1Password;
    }

    public void setJdbc1Password(String jdbc1Password) {
        this.jdbc1Password = jdbc1Password;
    }

    public String getJdbc2Password() {
        return Jdbc2Password;
    }

    public void setJdbc2Password(String jdbc2Password) {
        this.Jdbc2Password = jdbc2Password;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    protected Connection getSrcConnection() {
        return srcConnection;
    }

    protected void setSrcConnection(Connection connection) {
        this.srcConnection = connection;
    }

    public Connection getDstConnection() {
        return dstConnection;
    }

    public void setDstConnection(Connection dstConnection) {
        this.dstConnection = dstConnection;
    }

    protected String getFailureReason() {
        return failureReason;
    }

    public String getCheckingQuery() {
        return checkingQuery;
    }

    public void setCheckingQuery(String checkingQuery) {
        this.checkingQuery = checkingQuery;
    }

    @Override
    public void finalize() {
        try {
            super.finalize();
        } catch (Throwable e) {
            log.error("JDBC finalize error", e);
        } finally {
            if (srcConnection != null) {
                try {
                    srcConnection.close();
                } catch (SQLException throwable) {
                    log.error(throwable.getMessage());
                }
            }

            if (dstConnection != null) {
                try {
                    dstConnection.close();
                } catch (SQLException throwable) {
                    log.error(throwable.getMessage());
                }
            }
        }
    }
}
