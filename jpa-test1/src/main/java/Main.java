import jpa_test.beans.Customer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
//import com.dragon.jpa.model.Customer;
public class Main {
    public static void main(String[] args) {
        //persistence.xml文件中persistence-unit标签name属性值
        String persistenceUnitName = "JPA";
        //1.创建EntitymanagerFactory
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);

        //2.创建EntityManager
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        //3.开启事务
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        //4.进行持久化操作
        Customer customer = new Customer();
        customer.setAge(12);
        customer.setEmail("ricky@163.com");
        customer.setName("Ricky");
        entityManager.persist(customer);

        //5. 提交事务
        transaction.commit();

        //6.关闭EntityManager
        entityManager.close();

        //7.关闭EntityManager
        entityManagerFactory.close();
    }
}