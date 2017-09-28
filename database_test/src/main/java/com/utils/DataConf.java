package com.utils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DataConf {
    private static String optkey;
    private static String dateString;
    
    private static String DbType;  
    /*
    public static HashSet dbtype = new HashSet();

    static {
        dbtype.add("mysql");
        dbtype.add("oracle");
        dbtype.add("postgresql");
        dbtype.add("sqlserver");
        dbtype.add("db2");
        dbtype.add("derby");
    }
*/
    public static HashMap getSqlMap(Connection connection) {
        LinkedHashMap<String, String> sqlMap = new LinkedHashMap<String, String>();

        sqlMap.put("delete", "delete from test_user where id=1");
        sqlMap.put("insert", "insert into test_user (id,name,birthday) values (1, '" + optkey + "_insert'," + dateString + ")");
        sqlMap.put("update", "update test_user set name='" + optkey + "_update' where name = '" + optkey + "_insert'");
        sqlMap.put("call", "{call proc_update('"+optkey+"')}");
        sqlMap.put("select", "select id,name,birthday from test_user where name like '%" + optkey + "%'");
        if (DbType == "db2"){
            sqlMap.put("truncate", "truncate table test_user IMMEDIATE");
        }else{
        	sqlMap.put("truncate", "truncate table test_user");
        }
        if(connection!=null){
        	boolean flag=validateTableNameExist(connection,"test_tableA");
            if(flag){
            	 sqlMap.put("drop", "drop table test_tableA");
           	}
           	sqlMap.put("create", "create table test_tableA (id integer not null,nowtime varchar(255), longcontent varchar(255) , age integer, primary key ( id ))");
            sqlMap.put("alter", "alter table test_tableA drop column age");
        }
        return sqlMap;
            /*
             *
			DROP PROCEDURE IF EXISTS `proc_count`;
			 DELIMITER $$
				CREATE PROCEDURE `proc_count`(IN param_id INT,OUT resultCount INT)
					BEGIN
						SELECT COUNT(*) INTO resultCount FROM test_user WHERE id =param_id;
					END
						$$
			DELIMITER ;
				
					
			DROP PROCEDURE IF EXISTS `proc_sourceTypeCount`;
			DELIMITER $$
			CREATE PROCEDURE `proc_sourceTypeCount`(IN sourcetype VARCHAR(30))
			BEGIN
			 SET @sqlstr=CONCAT("SELECT count(*) FROM test_user WHERE NAME LIKE '%",sourcetype,"%'");
			 PREPARE stmt FROM @sqlstr;
			 EXECUTE stmt;
			END
			$$
			DELIMITER ;
			
						
			DROP PROCEDURE IF EXISTS `proc_TypeUpdate`;
			DELIMITER $$
			CREATE PROCEDURE `proc_Update`(IN sourcetype VARCHAR(30))
			BEGIN
			 SET @sqlstr=CONCAT("update test_user set name = 'proc_",sourcetype,"' WHERE NAME LIKE '%",sourcetype,"%'");
			 PREPARE stmt FROM @sqlstr;
			 EXECUTE stmt;
			END
			$$
DELIMITER ;
			*/
    }

    public static DataSource getDataSource(String dataType, String sourceType) {
    	String driver=PropertyUtil.getProperty(dataType+".driverClassName");
    	String url=PropertyUtil.getProperty(dataType+".url");
    	String username=PropertyUtil.getProperty(dataType+".username");
    	String password=PropertyUtil.getProperty(dataType+".password");
        if (dataType.equalsIgnoreCase("mysql")||dataType.equalsIgnoreCase("dm")) {
            dateString = "now()";
        }else if (dataType.equalsIgnoreCase("dm")) {
        	dateString = "now()";
        }
        else if (dataType.equalsIgnoreCase("oracle")) {
            dateString = "to_char(sysdate,'YYYY-MM-DD HH24:MI:SS')";
        } else if (dataType.equalsIgnoreCase("postgresql") || dataType.equalsIgnoreCase("db2") || dataType.equalsIgnoreCase("derby")) {
        	dateString = "current_timestamp";
        } else if (dataType.equalsIgnoreCase("sqlserver")) {
            dateString = "getdate()";
        } else {
            dateString = "'2020-1-1'";
        }
        DataSource dataSource = null;
        if (sourceType.equalsIgnoreCase("Jdbc")) {
        	 dataSource = new JdbcDatasource(driver,url,username,password);
//        	 dataSource = new JdbcDatasource(getDataConfFieldVal(dataType + "_driver"), getDataConfFieldVal(dataType + "_connectUrl"), getDataConfFieldVal(dataType + "_name"), getDataConfFieldVal(dataType + "_password"));
        }
//        else if (sourceType.equalsIgnoreCase("c3p0")) {
//            dataSource = new C3p0DataSource(getDataConfFieldVal(dataType + "_driver"), getDataConfFieldVal(dataType + "_connectUrl"), getDataConfFieldVal(dataType + "_name"), getDataConfFieldVal(dataType + "_password"));
//        } else if (sourceType.equalsIgnoreCase("dbcp")) {
//            dataSource = new DbcpDataSource(getDataConfFieldVal(dataType + "_driver"), getDataConfFieldVal(dataType + "_connectUrl"), getDataConfFieldVal(dataType + "_name"), getDataConfFieldVal(dataType + "_password"));
//        } else if (sourceType.equalsIgnoreCase("proxool")) {
//            try {
//                JAXPConfigurator.configure(DataConf.class.getClassLoader().getResource(getDataConfFieldVal(dataType + "_Proxool")).getFile(), false);
//            } catch (ProxoolException e) {
//                System.out.println("#########################   JAXPConfigurator.configure 执行错误");
//                e.printStackTrace();
//            }
//            dataSource = new ProxoolDataSource(dataType);}
        else {
            System.out.println("DataSourec未成功初始化。。。");
        }
        // optkey = dataType + "_" + sourceType;
        DbType=dataType;
        optkey = sourceType;
        return dataSource;
    }
   
    private static String getDataConfFieldVal(String fieldName) {
        String fieldVal = "";
        try {
            Class<DataConf> cc = DataConf.class;
            Field fieldObj = cc.getDeclaredField(fieldName);
            fieldVal = fieldObj.get(null).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("#########################  未读取到配置信息  " + fieldName);
        }
        return fieldVal;
    }
    public static boolean validateTableNameExist(Connection connection,String tableName){  
        ResultSet rs;
	    try {
		   rs = connection.getMetaData().getTables(null, null, tableName, null);
		   if (rs.next()) {  
	           return true;  
	       }else {  
	           return false;  
	       }
	    } catch (Exception e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	    }
		return false;          
   }  

}