package jooq;


import jooq.controllers.MainRESTController;
import jooq.tables.daos.EmployeesDao;
import jooq.tables.pojos.Employees;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainRESTController.MyConfig.class)
@Transactional
public class JooqTest {

    @Autowired
    private EmployeesDao employeesDao;

    @Test
    public void Test1(){
        Employees employees = new Employees();
        employees.setId(103);
        employees.setName("Alesx");
        employeesDao.insert(employees);

    }
//    @Test
//    public void Test2(){
//        Employees employees = new Employees();
//        employees.setId(104);
//        employees.setName("Alesx");
//        employees.setOrgId(3);
//        employees.setSupervisorId(1);
//        employeesDao.insert(employees);
//
//    }

//    @Test
//    public void Test2(){
//        Employees employees = new Employees();
//        employees.setId(9);
//        employees.setName("Alex");
//        employees.setOrgId(2);
//        employees.setSupervisorId(2);
//        employeesDao.insert(employees);
//
//    }
}
