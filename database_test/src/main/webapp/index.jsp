<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String contextPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<head>
    <title>Spring DB</title>
</head>
<body>
<h2>test_database</h2>
<h3>操作类型：insert update select delete call </h3>
<p>ojdbc6 11.2.0.4.0  ：
    <%--<a href="<%=contextPath%>/oracleSpringJdbc">SpringJdbc</a>--%>
    <a href="<%=contextPath%>/oracleJdbcTemplate">JdbcTemplate</a>
    <a href="<%=contextPath%>/oracleDruid">Druid</a>
    <a href="<%=contextPath%>/oracleC3p0">C3p0</a>
    <a href="<%=contextPath%>/oracleDbcp">Dbcp</a>
    <a href="<%=contextPath%>/oracleProxool">Proxool</a>
    <a href="<%=contextPath%>/oracleHibernate">Hibernate</a>
    <a href="<%=contextPath%>/oracleMybatis">Mybatis</a>
    <a href="<%=contextPath%>/oracleJdbc">Jdbc Statement</a>
    <a href="<%=contextPath%>/oracleJdbcPs">Jdbc PreparedStatement</a>  
    <a href="<%=contextPath%>/oracleJndi">Jndi</a>    
</p>
<p></p>
<p>db2jcc4 10.1 :
    <%--<a href="<%=contextPath%>/db2SpringJdbc">SpringJdbc</a>--%>
    <a href="<%=contextPath%>/db2JdbcTemplate">JdbcTemplate</a>
    <a href="<%=contextPath%>/db2Druid">Druid</a>
    <a href="<%=contextPath%>/db2C3p0">C3p0</a>
    <a href="<%=contextPath%>/db2Dbcp">Dbcp</a>
    <a href="<%=contextPath%>/db2Proxool">Proxool</a>
    <a href="<%=contextPath%>/db2Hibernate">Hibernate</a>
    <a href="<%=contextPath%>/db2Mybatis">Mybatis</a> 
    <a href="<%=contextPath%>/db2Jdbc">Jdbc Statement</a>
    <a href="<%=contextPath%>/db2JdbcPs">Jdbc PreparedStatement</a> 
    <a href="<%=contextPath%>/db2Jndi">Jndi</a> 
</p>
<p></p>
<p>postgresql 9.3.1104-jdbc4 :
   <%-- <a href="<%=contextPath%>/postgresqlSpringJdbc">SpringJdbc</a>--%>
    <a href="<%=contextPath%>/postgresqlJdbcTemplate">JdbcTemplate</a>
    <a href="<%=contextPath%>/postgresqlDruid">Druid</a>
    <a href="<%=contextPath%>/postgresqlC3p0">C3p0</a>
    <a href="<%=contextPath%>/postgresqlDbcp">Dbcp</a>
    <a href="<%=contextPath%>/postgresqlProxool">Proxool</a>
    <a href="<%=contextPath%>/postgresqlHibernate">Hibernate</a>
    <a href="<%=contextPath%>/postgresqlMybatis">Mybatis</a>
    <a href="<%=contextPath%>/postgresqlJdbc">Jdbc Statement</a>
    <a href="<%=contextPath%>/postgresqlJdbcPs">Jdbc PreparedStatement</a>  
    <a href="<%=contextPath%>/postgresqlJndi">Jndi</a> 
</p>
<p></p>
<p>mysql-connector-java 5.1.39 :
    <%--<a href="<%=contextPath%>/mysqlSpringJdbc">SpringJdbc</a>--%>
    <a href="<%=contextPath%>/mysqlJdbcTemplate">JdbcTemplate</a>
    <a href="<%=contextPath%>/mysqlDruid">Druid</a>
    <a href="<%=contextPath%>/mysqlC3p0">C3p0</a>
    <a href="<%=contextPath%>/mysqlDbcp">Dbcp</a>
    <a href="<%=contextPath%>/mysqlProxool">Proxool</a>
    <a href="<%=contextPath%>/mysqlHibernate">Hibernate</a>
    <a href="<%=contextPath%>/mysqlMybatis">Mybatis</a> 
    <a href="<%=contextPath%>/mysqlJdbc">Jdbc Statement</a>
    <a href="<%=contextPath%>/mysqlJdbcPs">Jdbc PreparedStatement</a> 
    <a href="<%=contextPath%>/mysqlJndi">Jndi</a>  
</p>
<p>sqlserver  jtds 1.3.1 :
    <%--<a href="<%=contextPath%>/sqlserverSpringJdbc">SpringJdbc</a>--%>
    <a href="<%=contextPath%>/sqlserverJdbcTemplate">JdbcTemplate</a>
    <a href="<%=contextPath%>/sqlserverDruid">Druid</a>
    <a href="<%=contextPath%>/sqlserverC3p0">C3p0</a>
    <a href="<%=contextPath%>/sqlserverDbcp">Dbcp</a>
    <a href="<%=contextPath%>/sqlserverProxool">Proxool</a>
    <a href="<%=contextPath%>/sqlserverHibernate">Hibernate</a>
    <a href="<%=contextPath%>/sqlserverMybatis">Mybatis</a>
    <a href="<%=contextPath%>/sqlserverJdbc">Jdbc Statement</a>
    <a href="<%=contextPath%>/sqlserverJdbcPs">Jdbc PreparedStatement</a>
    <a href="<%=contextPath%>/sqlserverJndi">Jndi</a>  
</p>
<p>derby 10.9.1.0 :
    <a href="<%=contextPath%>/derbyJdbc">Jdbc Statement</a> 
    <a href="<%=contextPath%>/derbyJdbcPs">Jdbc PreparedStatement</a> 
</p>
<p>dm :
    <a href="<%=contextPath%>/dmJDBC">Jdbc Statement</a>
</p>
<%= application.getServerInfo() %>
</body>
</html>
