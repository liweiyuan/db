package com.dm.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CloseUtil {
	public static void  CloseAll(Statement stmt,Connection con) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	

}
