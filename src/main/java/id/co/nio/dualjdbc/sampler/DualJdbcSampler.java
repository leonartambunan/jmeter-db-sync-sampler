package id.co.nio.dualjdbc.sampler;

import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.sql.*;

public class DualJdbcSampler extends AbstractDualJdbcSampler {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private String upsertQuery;
    private String checkingQuery;

    public DualJdbcSampler() {
        super("Dual JDBC Sampler");
    }

    public SampleResult sample(Entry e) {
        SampleResult res = new SampleResult();
        res.setSampleLabel(getName() + ":(" + getJdbc1Username() + "@" + getJdbc1Driver() + ":" + getJdbc1Url() + ")");

        // Set up sampler return types
        res.setSamplerData(upsertQuery);

        res.setDataType(SampleResult.TEXT);
        res.setContentType("text/plain");

        String response;
        if (getSrcConnection() == null) {
            connectToSrc();
        }
        if (getDstConnection() == null) {
            connectToDst();
        }

        try {
            if (getSrcConnection() == null) {
                log.error("Failed to connect to First DB Server with credentials "
                        + getJdbc1Username() + "@" + getJdbc1Driver() + ":" + getJdbc1Url()
                        + " pw=" + getJdbc1Password());
                throw new NullPointerException("Failed to connect to DB server: " + getFailureReason());
            }

            if (getDstConnection() == null) {
                log.error("Failed to connect to First DB Server with credentials "
                        + getJdbc1Username() + "@" + getJdbc1Driver() + ":" + getJdbc1Url()
                        + " pw=" + getJdbc1Password());
                throw new NullPointerException("Failed to connect to DB server: " + getFailureReason());
            }

            response = doProcess(upsertQuery, checkingQuery, res);
            res.setResponseData(response.getBytes());

            res.setSuccessful(true);
            res.setResponseMessageOK();
        } catch (SQLException sqlException) {
            res.setSuccessful(false);
            res.setResponseCode("JDBC Exception");
            res.setResponseMessage(sqlException.getMessage());
        } catch (NullPointerException e1) {
            res.setSuccessful(false);
            res.setResponseCode("JDBC Connection Failed");
            res.setResponseMessage(e1.getMessage());
        } finally {
            disconnect();
            setSrcConnection(null);
            setDstConnection(null);
        }
        return res;
    }

    private String doProcess(String upsertQuery, String checkingQuery, SampleResult res) throws SQLException {
        StringBuilder sb = new StringBuilder();

        res.sampleStart();
        Connection srcConnection  = getSrcConnection();

//        int lastId = -1;
//        try (PreparedStatement preparedStatement = srcConnection.prepareStatement(upsertQuery, Statement.RETURN_GENERATED_KEYS)) {
        sb.append(upsertQuery).append("\n");
        try (PreparedStatement preparedStatement = srcConnection.prepareStatement(upsertQuery)) {
            preparedStatement.executeUpdate();
//            ResultSet rs = preparedStatement.getGeneratedKeys();
//            if (rs.next()){
//                lastId = rs.getInt( 1 );
//            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        }


        boolean result = false;
        sb.append(checkingQuery).append("\n");

        do {
            Connection dstConnection = getDstConnection();
            try (PreparedStatement preparedStatement = dstConnection.prepareStatement(checkingQuery)) {
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    result = rs.getBoolean(1);
                }

            } catch (SQLException e) {
                log.error(e.getMessage());
                throw e;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (!result);

        res.sampleEnd();

        return sb.toString();
    }

    public String getUpsertQuery() {
        return upsertQuery;
    }

    public void setUpsertQuery(String source) {
        this.upsertQuery = source;
    }

    public String getCheckingQuery() {
        return checkingQuery;
    }

    public void setCheckingQuery(String checkingQuery) {
        this.checkingQuery = checkingQuery;
    }

}
