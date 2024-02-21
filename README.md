## Author : ELMARRAKCHY Reda | II-BDCC 2

### Part 1 

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

### Part 2

#### The IOC
````java
package com.reda.part2.presentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD})
@interface Autowired {
}

public class IoCContainer {
    private Map<String, Object> beans = new HashMap<>();

    public void registerBean(String beanName, Object bean) {
        beans.put(beanName, bean);
    }

    public Object getBean(String beanName) {
        return beans.get(beanName);
    }

    public void doConstructorInjection() {
        for (Object bean : beans.values()) {
            Class<?> beanClass = bean.getClass();
            for (Constructor<?> constructor : beanClass.getConstructors()) {
                if (constructor.isAnnotationPresent(Autowired.class)) {
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    Object[] parameters = new Object[parameterTypes.length];
                    for (int i = 0; i < parameterTypes.length; i++) {
                        parameters[i] = beans.get(parameterTypes[i].getSimpleName());
                    }
                    try {
                        constructor.newInstance(parameters);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void doSetterInjection() {
        for (Object bean : beans.values()) {
            Class<?> beanClass = bean.getClass();
            for (Method method : beanClass.getMethods()) {
                if (method.getName().startsWith("set") && method.isAnnotationPresent(Autowired.class)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1) {
                        continue; // Ignorer les méthodes setter avec plus d'un paramètre
                    }
                    Object dependency = beans.get(parameterTypes[0].getSimpleName());
                    try {
                        method.invoke(bean, dependency);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void doFieldInjection() {
        for (Object bean : beans.values()) {
            Class<?> beanClass = bean.getClass();
            for (Field field : beanClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Object dependency = beans.get(field.getType().getSimpleName());
                    field.setAccessible(true);
                    try {
                        field.set(bean, dependency);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
````
#### The implementation of the framework
````java
package com.reda.part2.presentation;

import com.reda.tp.dao.DaoImp;
import com.reda.tp.metier.IMetier;
import com.reda.tp.metier.MetierImp;

public class App {
    public static void main(String[] args) {
        IoCContainer container = new IoCContainer();
        
        //Saving the beans in a container
        container.registerBean("dao", new DaoImp());
        container.registerBean("metier", new MetierImp(new DaoImp()));

        // Injection of dependencies
        container.doConstructorInjection();
        container.doSetterInjection();
        container.doFieldInjection();

        //Using our beans
        IMetier metier = (IMetier) container.getBean("metier");
        metier.calculate();
    }
}
````
