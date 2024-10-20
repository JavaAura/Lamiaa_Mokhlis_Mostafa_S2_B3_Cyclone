package com.cyclone.Util;

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
            synchronized (JPAUtil.class) {
                if (entityManagerFactory == null) { // Double-checked locking
                    try {
                        logger.info("Attempting to create EntityManagerFactory...");
                        entityManagerFactory = Persistence.createEntityManagerFactory("cycloneJPA");
                        logger.info("EntityManagerFactory created successfully.");
                    } catch (Exception e) {
                        logger.error("Error creating EntityManagerFactory: " + e.getMessage());
                        e.printStackTrace();
                        throw new ExceptionInInitializerError("EntityManagerFactory initialization failed.");
                    }
                }
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
