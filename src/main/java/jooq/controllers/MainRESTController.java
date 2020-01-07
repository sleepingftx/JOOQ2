package jooq.controllers;

import com.sun.source.tree.Tree;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jooq.models.*;
import jooq.tables.Employees;
import jooq.tables.daos.*;
import jooq.tables.pojos.EmployeesEmployees;
import jooq.tables.pojos.EmployeesOrganizations;
import jooq.tables.pojos.Organizations;
import jooq.tables.pojos.OrganizationsOrganizations;
import jooq.tables.records.EmployeesRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.bind.DatatypeConverter.parseString;
import static jooq.Tables.*;
import static org.jooq.impl.DSL.*;

@RestController
public class MainRESTController{

    @Configuration
    @ComponentScan("jooq")
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

//    private AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
//    private EmployeesDao dao = ctx.getBean(EmployeesDao.class);
//    private EmployeesOrganizationsDao dao2 = ctx.getBean(EmployeesOrganizationsDao.class);
//    private EmployeesEmployeesDao dao3 = ctx.getBean(EmployeesEmployeesDao.class);
//    private DSLContext create = ctx.getBean(DSLContext.class);
//    private OrganizationsDao dao4 = ctx.getBean(OrganizationsDao.class);

    @GetMapping("/employees")
    public List<Employeee> getEmployees() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
       // EmployeesDao dao = ctx.getBean(EmployeesDao.class);
        DSLContext create = ctx.getBean(DSLContext.class);
        ArrayList<Employeee> e = new ArrayList<Employeee>();
        Employees EM = EMPLOYEES.as("EM");
        Result<Record7<Integer, String, String, String, String, Integer, Integer>> fetch = create.select(EMPLOYEES.ID, EMPLOYEES.NAME,ORGANIZATIONS.NAME,EMPLOYEES.NAME,EM.NAME,ORGANIZATIONS.ID,EMPLOYEES_EMPLOYEES.SUPERVISOR_ID)
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
            Employeee n = new Employeee(record.get(EMPLOYEES.ID), record.get(EMPLOYEES.NAME),record.get(ORGANIZATIONS.NAME),record.get(EM.NAME),record.get(ORGANIZATIONS.ID),record.get(EMPLOYEES_EMPLOYEES.SUPERVISOR_ID));
        //    Employeee n = new Employeee(record.get(), record.get(EMPLOYEES.NAME),record.get(ORGANIZATIONS.NAME),record.get(EM.NAME),record.get(ORGANIZATIONS.ID),record.get(EMPLOYEES_EMPLOYEES.SUPERVISOR_ID));
            e.add(n);
         }
        ctx.close();
        return e;
    }

    @GetMapping("/orgs")
    public List<Organizations> getOrgs() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
        OrganizationsDao dao4 = ctx.getBean(OrganizationsDao.class);
        //DSLContext create = ctx.getBean(DSLContext.class);
        List<jooq.tables.pojos.Organizations> all = dao4.findAll();
        ctx.close();
        return all;
    }

    @PostMapping("/employee")
    public Boolean addEmployee(@RequestBody EmployeeForm s) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
        EmployeesDao dao = ctx.getBean(EmployeesDao.class);
        EmployeesOrganizationsDao dao2 = ctx.getBean(EmployeesOrganizationsDao.class);
        EmployeesEmployeesDao dao3 = ctx.getBean(EmployeesEmployeesDao.class);
        //DSLContext create = ctx.getBean(DSLContext.class);

        jooq.tables.pojos.Employees employees = new jooq.tables.pojos.Employees();
        employees.setName(s.getFormName());
        dao.insert(employees);
        Integer id = dao.getId(employees);


        EmployeesOrganizations employeesOrganizations = new EmployeesOrganizations();
        employeesOrganizations.setEmployeeId(id);
        employeesOrganizations.setOrganizationId(Integer.parseInt(s.getFormOrg().replace("option-", "")));
        dao2.insert(employeesOrganizations);

        EmployeesEmployees employeesEmployees = new EmployeesEmployees();
        employeesEmployees.setEmployeeId(id);
        employeesEmployees.setSupervisorId(Integer.parseInt(s.getFormSV().replace("option-", "")));
        dao3.insert(employeesEmployees);

        ctx.close();


        System.out.println("(Service Side) Creating employee with empNo: " + s.getFormId());
        System.out.println("(Service Side) Creating employee with empNo: " + s.getFormName());
        System.out.println("(Service Side) Creating employee with empNo: " + s.getFormOrg());
        System.out.println("(Service Side) Creating employee with empNo: " + s.getFormSV());

        return true;
    }

    @PutMapping("/employee")
    public Boolean editEmployee(@RequestBody EmployeeForm s) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
//        EmployeesDao dao = ctx.getBean(EmployeesDao.class);
//        EmployeesOrganizationsDao dao2 = ctx.getBean(EmployeesOrganizationsDao.class);
//        EmployeesEmployeesDao dao3 = ctx.getBean(EmployeesEmployeesDao.class);
        DSLContext create = ctx.getBean(DSLContext.class);

        create.update(EMPLOYEES)
            .set(EMPLOYEES.NAME, s.getFormName())
            .where(EMPLOYEES.ID.eq(s.getFormId()))
            .execute();

        create.update(EMPLOYEES_ORGANIZATIONS)
            .set(EMPLOYEES_ORGANIZATIONS.ORGANIZATION_ID, Integer.parseInt(s.getFormOrg().replace("option-", "")))
            .where(EMPLOYEES_ORGANIZATIONS.EMPLOYEE_ID.eq(s.getFormId()))
            .execute();
        create.update(EMPLOYEES_EMPLOYEES)
            .set(EMPLOYEES_EMPLOYEES.SUPERVISOR_ID, Integer.parseInt(s.getFormSV().replace("option-", "")))
            .where(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID.eq(s.getFormId()))
            .execute();

        ctx.close();

        return true;
    }


    @DeleteMapping("/employee/{Id}")
    public void deleteEmployee(@PathVariable("Id") Integer Id) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
//        EmployeesDao dao = ctx.getBean(EmployeesDao.class);
//        EmployeesOrganizationsDao dao2 = ctx.getBean(EmployeesOrganizationsDao.class);
//        EmployeesEmployeesDao dao3 = ctx.getBean(EmployeesEmployeesDao.class);
        DSLContext create = ctx.getBean(DSLContext.class);



        create.delete(EMPLOYEES)
            .where(EMPLOYEES.ID.eq(Id))
            .execute();

        create.delete(EMPLOYEES_ORGANIZATIONS)
            .where(EMPLOYEES_ORGANIZATIONS.EMPLOYEE_ID.eq(Id))
            .execute();
        create.delete(EMPLOYEES_EMPLOYEES)
            .where(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID.eq(Id))
            .execute();

        ctx.close();
    }


    @GetMapping("/tree1_ref")
    public List<Tree1> tree1_ref() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
        // EmployeesDao dao = ctx.getBean(EmployeesDao.class);
        DSLContext create = ctx.getBean(DSLContext.class);
        ArrayList<Tree1> e = new ArrayList<Tree1>();
        jooq.tables.EmployeesEmployees EE = EMPLOYEES_EMPLOYEES.as("EE");
        Result<Record3<Integer, String, Integer>> fetch = create.select(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID,EMPLOYEES.NAME, EE.SUPERVISOR_ID)
            .from(EMPLOYEES_EMPLOYEES)
            .join(EMPLOYEES)
            .on(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID.equal(EMPLOYEES.ID))
            .leftJoin(EE)
            .on(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID.equal(EE.SUPERVISOR_ID))
            .where(EMPLOYEES_EMPLOYEES.SUPERVISOR_ID.equal(0))
            .groupBy(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID,EMPLOYEES.NAME,EE.SUPERVISOR_ID)
            .orderBy(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID)
            .fetch();
        for (Record record:fetch){
            Tree1 t = new Tree1(record.get(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID), record.get(EMPLOYEES.NAME), (record.get(EE.SUPERVISOR_ID)== null)?false:true,new int[0]);
            //    Employeee n = new Employeee(record.get(), record.get(EMPLOYEES.NAME),record.get(ORGANIZATIONS.NAME),record.get(EM.NAME),record.get(ORGANIZATIONS.ID),record.get(EMPLOYEES_EMPLOYEES.SUPERVISOR_ID));
            e.add(t);
        }
        ctx.close();
        return e;
    }

    @GetMapping("/tree1_ref/{Id}")
    public List<Tree1> tree1_ref(@PathVariable("Id") Integer Id) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
        // EmployeesDao dao = ctx.getBean(EmployeesDao.class);
        DSLContext create = ctx.getBean(DSLContext.class);
        ArrayList<Tree1> e = new ArrayList<Tree1>();
        jooq.tables.EmployeesEmployees EE = EMPLOYEES_EMPLOYEES.as("EE");
        Result<Record3<Integer, String, Integer>> fetch = create.select(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID,EMPLOYEES.NAME, EE.SUPERVISOR_ID)
            .from(EMPLOYEES_EMPLOYEES)
            .join(EMPLOYEES)
            .on(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID.equal(EMPLOYEES.ID))
            .leftJoin(EE)
            .on(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID.equal(EE.SUPERVISOR_ID))
            .where(EMPLOYEES_EMPLOYEES.SUPERVISOR_ID.equal(Id))
            .groupBy(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID,EMPLOYEES.NAME,EE.SUPERVISOR_ID)
            .orderBy(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID)
            .fetch();
        for (Record record:fetch){
            Tree1 t = new Tree1(record.get(EMPLOYEES_EMPLOYEES.EMPLOYEE_ID), record.get(EMPLOYEES.NAME), (record.get(EE.SUPERVISOR_ID)== null)?false:true,new int[0]);
            //    Employeee n = new Employeee(record.get(), record.get(EMPLOYEES.NAME),record.get(ORGANIZATIONS.NAME),record.get(EM.NAME),record.get(ORGANIZATIONS.ID),record.get(EMPLOYEES_EMPLOYEES.SUPERVISOR_ID));
            e.add(t);
        }
        ctx.close();
        return e;
    }


    @GetMapping("/org")
    public List<Orgs> getOrg() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
        // EmployeesDao dao = ctx.getBean(EmployeesDao.class);
        DSLContext create = ctx.getBean(DSLContext.class);
        ArrayList<Orgs> e = new ArrayList<Orgs>();
        jooq.tables.Organizations ORG = ORGANIZATIONS.as("ORG");
        Result<Record5<Integer, String, String, Integer, Integer>> fetch = create.select(ORGANIZATIONS.ID, ORGANIZATIONS.NAME,ORG.NAME,ORGANIZATIONS_ORGANIZATIONS.PARENT_ID,count(EMPLOYEES_ORGANIZATIONS.ORGANIZATION_ID))
            .from(ORGANIZATIONS)
            .join(ORGANIZATIONS_ORGANIZATIONS)
            .on(ORGANIZATIONS.ID.equal(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID))
            .leftJoin(ORG)
            .on(ORGANIZATIONS_ORGANIZATIONS.PARENT_ID.equal(ORG.ID))
            .leftJoin(EMPLOYEES_ORGANIZATIONS)
            .on(ORGANIZATIONS.ID.equal(EMPLOYEES_ORGANIZATIONS.ORGANIZATION_ID))
            .groupBy(ORGANIZATIONS.ID, ORGANIZATIONS.NAME,ORG.NAME,ORGANIZATIONS_ORGANIZATIONS.PARENT_ID)
            .orderBy(ORGANIZATIONS.ID)
            .fetch();
        for (Record record:fetch){
            Orgs o = new Orgs(record.get(ORGANIZATIONS.ID), record.get(ORGANIZATIONS.NAME),record.get(ORG.NAME),record.get(ORGANIZATIONS_ORGANIZATIONS.PARENT_ID),record.get(count()));
            //    Employeee n = new Employeee(record.get(), record.get(EMPLOYEES.NAME),record.get(ORGANIZATIONS.NAME),record.get(EM.NAME),record.get(ORGANIZATIONS.ID),record.get(EMPLOYEES_EMPLOYEES.SUPERVISOR_ID));
            e.add(o);
        }
        ctx.close();
        return e;
    }



    @PostMapping("/org")
    public Boolean addOrg(@RequestBody EmployeeForm2 s2) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
        OrganizationsDao dao1 = ctx.getBean(OrganizationsDao.class);
        OrganizationsOrganizationsDao dao2 = ctx.getBean(OrganizationsOrganizationsDao.class);

        jooq.tables.pojos.Organizations organizations = new jooq.tables.pojos.Organizations();
        organizations.setName(s2.getFormName());
        dao1.insert(organizations);
        Integer id = dao1.getId(organizations);

        OrganizationsOrganizations organizationsOrganizations = new OrganizationsOrganizations();
        organizationsOrganizations.setOrganizationId(id);
        organizationsOrganizations.setParentId(Integer.parseInt(s2.getFormParent().replace("option-", "")));
        dao2.insert(organizationsOrganizations);

        ctx.close();
        return true;
    }

    @PutMapping("/org")
    public Boolean editOrg(@RequestBody EmployeeForm2 s2) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
//        EmployeesDao dao = ctx.getBean(EmployeesDao.class);
//        EmployeesOrganizationsDao dao2 = ctx.getBean(EmployeesOrganizationsDao.class);
//        EmployeesEmployeesDao dao3 = ctx.getBean(EmployeesEmployeesDao.class);
        DSLContext create = ctx.getBean(DSLContext.class);

        create.update(ORGANIZATIONS)
            .set(ORGANIZATIONS.NAME, s2.getFormName())
            .where(ORGANIZATIONS.ID.eq(s2.getFormId()))
            .execute();

        create.update(ORGANIZATIONS_ORGANIZATIONS)
            .set(ORGANIZATIONS_ORGANIZATIONS.PARENT_ID, Integer.parseInt(s2.getFormParent().replace("option-", "")))
            .where(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID.eq(s2.getFormId()))
            .execute();

        ctx.close();

        return true;
    }


    @DeleteMapping("/org/{Id}")
    public void deleteOrg(@PathVariable("Id") Integer Id) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
//        EmployeesDao dao = ctx.getBean(EmployeesDao.class);
//        EmployeesOrganizationsDao dao2 = ctx.getBean(EmployeesOrganizationsDao.class);
//        EmployeesEmployeesDao dao3 = ctx.getBean(EmployeesEmployeesDao.class);
        DSLContext create = ctx.getBean(DSLContext.class);

        create.delete(ORGANIZATIONS)
            .where(ORGANIZATIONS.ID.eq(Id))
            .execute();

        create.delete(ORGANIZATIONS_ORGANIZATIONS)
            .where(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID.eq(Id))
            .execute();

        ctx.close();
    }



    @GetMapping("/tree2_ref")
    public List<Tree1> tree2_ref() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
        // EmployeesDao dao = ctx.getBean(EmployeesDao.class);
        DSLContext create = ctx.getBean(DSLContext.class);
        ArrayList<Tree1> e = new ArrayList<Tree1>();
        jooq.tables.OrganizationsOrganizations OO = ORGANIZATIONS_ORGANIZATIONS.as("OO");
        Result<Record3<Integer, String, Integer>> fetch = create.select(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID,ORGANIZATIONS.NAME, OO.PARENT_ID)
            .from(ORGANIZATIONS_ORGANIZATIONS)
            .join(ORGANIZATIONS)
            .on(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID.equal(ORGANIZATIONS.ID))
            .leftJoin(OO)
            .on(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID.equal(OO.PARENT_ID))
            .where(ORGANIZATIONS_ORGANIZATIONS.PARENT_ID.equal(0))
            .groupBy(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID,ORGANIZATIONS.NAME,OO.PARENT_ID)
            .orderBy(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID)
            .fetch();
        for (Record record:fetch){
            Tree1 t = new Tree1(record.get(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID), record.get(ORGANIZATIONS.NAME), (record.get(OO.PARENT_ID)== null)?false:true,new int[0]);
            //    Employeee n = new Employeee(record.get(), record.get(EMPLOYEES.NAME),record.get(ORGANIZATIONS.NAME),record.get(EM.NAME),record.get(ORGANIZATIONS.ID),record.get(EMPLOYEES_EMPLOYEES.SUPERVISOR_ID));
            e.add(t);
        }
        ctx.close();
        return e;
    }

    @GetMapping("/tree2_ref/{Id}")
    public List<Tree1> tree2_ref(@PathVariable("Id") Integer Id) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
        // EmployeesDao dao = ctx.getBean(EmployeesDao.class);
        DSLContext create = ctx.getBean(DSLContext.class);
        ArrayList<Tree1> e = new ArrayList<Tree1>();
        jooq.tables.OrganizationsOrganizations OO = ORGANIZATIONS_ORGANIZATIONS.as("OO");
        Result<Record3<Integer, String, Integer>> fetch = create.select(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID,ORGANIZATIONS.NAME, OO.PARENT_ID)
            .from(ORGANIZATIONS_ORGANIZATIONS)
            .join(ORGANIZATIONS)
            .on(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID.equal(ORGANIZATIONS.ID))
            .leftJoin(OO)
            .on(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID.equal(OO.PARENT_ID))
            .where(ORGANIZATIONS_ORGANIZATIONS.PARENT_ID.equal(Id))
            .groupBy(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID,ORGANIZATIONS.NAME,OO.PARENT_ID)
            .orderBy(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID)
            .fetch();
        for (Record record:fetch){
            Tree1 t = new Tree1(record.get(ORGANIZATIONS_ORGANIZATIONS.ORGANIZATION_ID), record.get(ORGANIZATIONS.NAME), (record.get(OO.PARENT_ID)== null)?false:true,new int[0]);
            //    Employeee n = new Employeee(record.get(), record.get(EMPLOYEES.NAME),record.get(ORGANIZATIONS.NAME),record.get(EM.NAME),record.get(ORGANIZATIONS.ID),record.get(EMPLOYEES_EMPLOYEES.SUPERVISOR_ID));
            e.add(t);
        }
        ctx.close();
        return e;
    }


}
