package com.cyclone.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPAUtil {

    private static EntityManagerFactory entityManagerFactory = null;
    private static final Logger logger = LoggerFactory.getLogger(JPAUtil.class);

    private JPAUtil() {}

    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            try {
                Properties props = new Properties();
                props.load(JPAUtil.class.getClassLoader().getResourceAsStream("config.properties"));


                Map<String, String> properties = new HashMap<>();
                properties.put("javax.persistence.jdbc.url", props.getProperty("db.url"));
                properties.put("javax.persistence.jdbc.user", props.getProperty("db.user"));
                properties.put("javax.persistence.jdbc.password", props.getProperty("db.password"));

                entityManagerFactory = Persistence.createEntityManagerFactory("cycloneJPA", properties);
            } catch (IOException e) {
                throw new RuntimeException("Error loading database configuration", e);
            }
        }
        return entityManagerFactory;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            entityManagerFactory = null; 
        }
    }
}
