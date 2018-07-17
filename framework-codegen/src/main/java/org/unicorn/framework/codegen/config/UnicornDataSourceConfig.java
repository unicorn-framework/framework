package org.unicorn.framework.codegen.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.unicorn.framework.codegen.convert.IUnicornTypeConvert;
import org.unicorn.framework.codegen.convert.UnicornMysqlTypeConvert;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.config.rules.DbType;

public class UnicornDataSourceConfig {
	 /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * 类型转换
     */
    private IUnicornTypeConvert typeConvert;
    /**
     * 驱动连接的URL
     */
    private String url;
    /**
     * 驱动名称
     */
    private String driverName;
    /**
     * 数据库连接用户名
     */
    private String username;
    /**
     * 数据库连接密码
     */
    private String password;

    /**
     * 判断数据库类型
     *
     * @return 类型枚举值
     */
    public DbType getDbType() {
        if (null == dbType) {
            if (driverName.contains("mysql")) {
                dbType = DbType.MYSQL;
            } else if (driverName.contains("oracle")) {
                dbType = DbType.ORACLE;
            } else if (driverName.contains("postgresql")) {
                dbType = DbType.POSTGRE_SQL;
            } else {
                throw new MybatisPlusException("Unknown type of database!");
            }
        }
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public IUnicornTypeConvert getTypeConvert() {
        if (null == typeConvert) {
            switch (getDbType()) {
                case ORACLE:
                    break;
                case SQL_SERVER:
                    break;
                case POSTGRE_SQL:
                    break;
                default:
                    typeConvert = new UnicornMysqlTypeConvert();
                    break;
            }
        }
        return typeConvert;
    }

    public void setTypeConvert(IUnicornTypeConvert typeConvert) {
        this.typeConvert = typeConvert;
    }

    /**
     * 创建数据库连接对象
     *
     * @return Connection
     */
    public Connection getConn() {
        Connection conn = null;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
