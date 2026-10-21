package com.bankapp.dao;

import com.bankapp.model.Transation;
import com.bankapp.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    /** Uses the caller's connection so it joins the caller's DB transaction. */
    public static void record(Connection con, long account, String type, double amount, String desc)
            throws SQLException {
        String sql = "INSERT INTO bank_transaction(account_number, transaction_type, amount, description) "
                   + "VALUES (?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, account);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setString(4, desc);
            ps.executeUpdate();
        }
    }

    public List<Transation> findByAccount(long account) throws SQLException {
        return query("SELECT * FROM bank_transaction WHERE account_number = ? ORDER BY transaction_id DESC", account);
    }

    public List<Transation> findAll() throws SQLException {
        return query("SELECT * FROM bank_transaction ORDER BY transaction_id DESC", null);
    }

    private List<Transation> query(String sql, Long account) throws SQLException {
        List<Transation> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (account != null) ps.setLong(1, account);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                	 list.add(
                             new Transation(
                                 rs.getInt("transaction_id"),
                                 rs.getLong("account_number"),
                                 rs.getString("transaction_type"),
                                 rs.getDouble("amount"),
                                 rs.getString("description"),
                                 rs.getTimestamp("transaction_date")
                             )
                         );
                }
            }
        }
        return list;
    }
}
