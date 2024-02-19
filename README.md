## Author : ELMARRAKCHY Reda | II-BDCC 2

### This repository contains the 1st part

#### Create the IDao interface with a method getDate :
```java
package com.reda.tp.dao;

import java.util.Date;

public interface IDao {
    Date getDate();
}
```
#### Create an implementation of this interface :
```java
package com.reda.tp.dao;

import java.util.Date;

public class DaoImp implements IDao {
    @Override
    public Date getDate() {
        return new Date();
    }
}
```
#### Create the IMetier interface with a method called calculate :
```java
package com.reda.tp.metier;

public interface IMetier {
    void calculate();
}
```
#### Create an implementation of this interface using loose coupling :
```java
package com.reda.tp.metier;

import com.reda.tp.dao.IDao;

import java.util.Date;

public class MetierImp implements IMetier {
    private IDao dao;

    public MetierImp(IDao dao) {
        this.dao = dao;
    }

    @Override
    public void calculate() {
        Date date = dao.getDate();
        System.out.println("Date of calculation : " + date);
    }
}
```
#### Perform dependency injection through static instantiation :
```java
package com.reda.tp.presentation;

import com.reda.tp.dao.DaoImp;
import com.reda.tp.dao.IDao;
import com.reda.tp.metier.IMetier;

import com.reda.tp.metier.MetierImp;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        IDao dao = new DaoImp();
        IMetier metier = new MetierImp(dao);
        metier.calculate();
    }
}
```
![image](https://github.com/relmarrakchy/part_1/assets/79580220/80e63972-bc4c-425f-bfed-314454ea8b6a)

#### Perform dependency injection through dynamic instantiation:
```java
package com.reda.tp.presentation;

import com.reda.tp.dao.DaoImp;
import com.reda.tp.dao.IDao;
import com.reda.tp.metier.IMetier;

import com.reda.tp.metier.MetierImp;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        try {
            IDao dao = (IDao) Class.forName("com.reda.tp.dao.DaoImp").getDeclaredConstructor().newInstance();
            IMetier metier = (IMetier) Class.forName("com.reda.tp.metier.MetierImp").getDeclaredConstructor(IDao.class).newInstance(dao);
            metier.calculate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
#### Perform dependency injection using the Spring Framework with XML configuration:
##### applicationContext.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <beans>
        <bean id="dao" class="com.reda.tp.dao.DaoImp"/>
        <bean id="metier" class="com.reda.tp.metier.MetierImp">
            <constructor-arg ref="dao"/>
        </bean>
    </beans>
</beans>
```
##### app.java
```java
package com.reda.tp.presentation;

import com.reda.tp.dao.IDao;
import com.reda.tp.metier.IMetier;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        IMetier metier = (IMetier) context.getBean("metier");
        metier.calculate();
    }
}
```
![image](https://github.com/relmarrakchy/part_1/assets/79580220/b61d8831-16f9-40ff-846a-2420315db816)
#### Perform dependency injection using the Spring Framework with annotations:
```java
package com.reda.tp.presentation;

import com.reda.tp.dao.IDao;
import com.reda.tp.metier.IMetier;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.reda.tp")
class AppConfig {
    // Configuration de Spring
}
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        IMetier metier = context.getBean(IMetier.class);
        metier.calculate();
    }
}
```
![image](https://github.com/relmarrakchy/part_1/assets/79580220/4787094b-73bb-4693-b703-d0ccae9a0cd9)
