package de.unifrankfurt.dbis.DBFit;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import dbfit.MySqlTest;
import dbfit.api.DBEnvironment;
import de.unifrankfurt.dbis.Submission.Count;
import de.unifrankfurt.dbis.config.DataSource;
import de.unifrankfurt.dbis.config.XConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class CustomMySQLTest extends MySqlTest {

    public boolean isConnected() {
        DBEnvironment a = super.environment;

        Connection c;
        try {
            c = a.getConnection();
        } catch (SQLException | IllegalArgumentException e) {
            return false;
        }
        if (c == null) return false;
        try {
            if (c.isClosed()) return false;
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * returns the count which was evaluated by DBFit
     *
     * @return a Count object
     */
    public Count getCount() {
        return new Count(
                this.counts.right,
                this.counts.wrong,
                this.counts.ignores,
                this.counts.exceptions
        );
    }

    public void connect(XConfig config) throws SQLException {
        super.connect(config.getHostname(), config.getUsername(), config.getPassword(), config.getDatabase());
    }

    public void connect(DataSource source) throws SQLException {
        super.connect(source.getHost(), source.getUser(), source.getPassword(), source.getDatabase());
    }
}