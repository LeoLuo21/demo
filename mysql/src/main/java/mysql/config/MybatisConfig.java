package mysql.config;

import common.domain.Product;
import lombok.extern.slf4j.Slf4j;
import mysql.mapper.ProductMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author leo
 * @date 20220627 16:03:07
 */
@Slf4j
public class MybatisConfig {
    private static DataSource getDataSource() {
        Properties properties = new Properties();
        try (var is = MybatisConfig.class.getClassLoader().getResourceAsStream("jdbc.properties")){
            properties.load(is);
            String driver = properties.getProperty("jdbc.driver");
            String url = properties.getProperty("jdbc.url");
            String username = properties.getProperty("jdbc.user");
            String password = properties.getProperty("jdbc.password");
            return new PooledDataSource(driver,url,username,password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        Environment environment = new Environment("development", new JdbcTransactionFactory(), getDataSource());
        Configuration configuration = new Configuration(environment);
        configuration.addMappers("mysql.mapper");
        configuration.getTypeAliasRegistry().registerAliases("common.domain");
        return builder.build(configuration);
    }

    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        try (var session = sqlSessionFactory.openSession()){
            ProductMapper mapper = session.getMapper(ProductMapper.class);
            Product one = mapper.getOne();
            log.info("{}",one);
        }
    }

}
