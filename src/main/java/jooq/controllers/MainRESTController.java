package jooq.controllers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jooq.models.Employeee;
import jooq.tables.Employees;
import jooq.tables.daos.EmployeesDao;
import org.jooq.*;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static jooq.Tables.*;

@RestController
public class MainRESTController{

    @Configuration
    @ComponentScan("jooq")
    @EnableTransactionManagement
    public static class MyConfig{

        @Bean
        public PlatformTransactionManager transactionManager(){
            return new DataSourceTransactionManager(dasource());
        }

        @Bean
        public DataSource dasource() {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
            config.setUsername("postgres");
            config.setPassword("44952649Hs");
            return  new HikariDataSource(config);
        }

        @Bean
        public DataSourceConnectionProvider dataSourceConnectionProvider(){
            return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dasource()));
        }

        @Bean
        public DefaultDSLContext defaultDSLContext(){
            return new DefaultDSLContext(configuraton());
        }

        @Bean
        public org.jooq.Configuration configuraton() {
            DefaultConfiguration defaultConfiguration = new DefaultConfiguration();
            defaultConfiguration.setConnectionProvider(dataSourceConnectionProvider());
            defaultConfiguration.setSQLDialect(SQLDialect.POSTGRES);
            return defaultConfiguration;
        }

//        @Bean
//        public TransactionProvider transactionProvider(final PlatformTransactionManager platformTransactionManager) {
//            return new TransactionProvider() {
//                @Override
//                public void begin(TransactionContext ctx) throws DataAccessException {
//                    TransactionDefinition definition = new DefaultTransactionDefinition(
//                            TransactionDefinition.PROPAGATION_NESTED);
//                    TransactionStatus status = platformTransactionManager.getTransaction(definition);
//                    ctx.transaction(new SpringTransaction(status));
//                }
//
//                @Override
//                public void commit(TransactionContext ctx) throws DataAccessException {
//                    platformTransactionManager.commit(getTransactionStatus(ctx));
//                }
//
//                @Override
//                public void rollback(TransactionContext ctx) throws DataAccessException {
//                    platformTransactionManager.rollback(getTransactionStatus(ctx));
//                }
//
//                private TransactionStatus getTransactionStatus(TransactionContext ctx) {
//                    SpringTransaction transaction = (SpringTransaction) ctx.transaction();
//                    return transaction.getTxStatus();
//                }
//            };
//        }

//        public static class SpringTransaction implements Transaction {
//
//            private final TransactionStatus transactionStatus;
//
//            SpringTransaction(TransactionStatus transactionStatus) {
//                this.transactionStatus = transactionStatus;
//            }
//
//            public TransactionStatus getTxStatus() {
//                return this.transactionStatus;
//            }
//
//        }

    }

    // http://localhost:8080/SomeContextPath/employees
    // http://localhost:8080/SomeContextPath/employees.xml
    // http://localhost:8080/SomeContextPath/employees.json
    @RequestMapping(value = "/employees", //
            method = RequestMethod.GET, //
            produces = { MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public List<Employeee> getEmployees() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
        EmployeesDao dao = ctx.getBean(EmployeesDao.class);
        DSLContext create = ctx.getBean(DSLContext.class);

        ArrayList<Employeee> e = new ArrayList<Employeee>();

        Employees EM = EMPLOYEES.as("EM");

        Result<Record5<Integer, String, String, String, String>> fetch = create.select(EMPLOYEES.ID, EMPLOYEES.NAME,ORGANIZATIONS.NAME,EMPLOYEES.NAME,EM.NAME)
                .from(EMPLOYEES)
                .join(EMPLOYEES_ORGANIZATIONS)
                .on(EMPLOYEES.ID.equal(EMPLOYEES_ORGANIZATIONS.EMPLOYEE_ID))
                .join(ORGANIZATIONS)
                .on(EMPLOYEES_ORGANIZATIONS.ORGANIZATION_ID.equal(ORGANIZATIONS.ID))
                .join(EMPLOYEES_EMPLOYEES)
                .on(EMPLOYEES.ID.equal(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID))
                .leftJoin(EM)
                .on(EMPLOYEES_EMPLOYEES.SUPERVISOR_ID.equal(EM.ID))
                .orderBy(EMPLOYEES.ID)
                .fetch();


        for (Record record:fetch){
            Employeee n = new Employeee(record.get(EMPLOYEES.ID), record.get(EMPLOYEES.NAME),record.get(ORGANIZATIONS.NAME),record.get(EM.NAME));
            e.add(n);
         }
        return e;
    }
}
