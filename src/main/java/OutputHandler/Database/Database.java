package OutputHandler.Database;

import java.sql.*;

public class Database {
    private final String user;
    private final String host;
    private final int port;
    private final String db;
    private final Connection connection;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;

    public Database(String user, String password, String host, int port, String db) {
        this.user = user;
        this.host = host;
        this.port = port;
        this.db = db;

        try {
            this.connection = DriverManager.getConnection(this.getUrl(), user, password);
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e.getMessage() + "\n" + e.getSQLState() + "\n" + e.getErrorCode());
        }
    }

    public ResultSet queryStatement(String query) throws SQLException {
        this.statement = this.connection.createStatement();
        this.resultSet = this.statement.executeQuery(query);
        return this.resultSet;
    }


    /**
     * Data Manipulation Language (DML) statements are ran via this command
     * Eg. create, drop, insert, update, delete
     * @param dmlString
     * @return
     * @throws SQLException
     */
    public int dmlStatement(String dmlString) throws SQLException {
        this.statement = this.connection.createStatement();
        return statement.executeUpdate(dmlString);
    }

    public Statement getLastStatement(){
        return this.statement;
    }

    public ResultSet getLastResultSet(){
        return this.resultSet;
    }

    public PreparedStatement getLastPreparedStatement(){
        return this.preparedStatement;
    }

    public String getUser() {
        return user;
    }

    public String getUrl(){
        return "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.db;
    }
}
