package ru.netology.data;

import lombok.val;

import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class DataHelperSQL {
    private static final String url = System.getProperty("url");
    private static final String user = System.getProperty("user");
    private static final String password = System.getProperty("password");

    private DataHelperSQL() {
    }

    public static String getPurchaseByDebitCard() { //покупка дебетовой картой
        var statusBD = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";

        try (
                var connection = getConnection(url, user, password);
                var payStatus = connection.createStatement()
        ) {
            try (var rs = payStatus.executeQuery(statusBD)) {
                if (rs.next()) {
                    var status = rs.getString(1);
                    return status;
                }
                return null;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String getPurchaseOnCreditCard() { //покупка в кредит
        var statusBD = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";

        try (
                var connection = getConnection(url, user, password);
                var payStatus = connection.createStatement()
        ) {
            try (var rs = payStatus.executeQuery(statusBD)) {
                if (rs.next()) {
                    var status = rs.getString(1);
                    return status;
                }
                return null;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }


    public static void cleanDataBase() { //очистить БД

        var payment = "DELETE FROM payment_entity";
        var credit = "DELETE FROM credit_request_entity";
        var order = "DELETE FROM order_entity";


        try (
                var conn = getConnection(url, user, password);
                var prepareStatCredit = conn.createStatement();
                var prepareStatOrder = conn.createStatement();
                var prepareStatPayment = conn.createStatement()
        ) {
            prepareStatCredit.executeUpdate(credit);
            prepareStatOrder.executeUpdate(order);
            prepareStatPayment.executeUpdate(payment);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
