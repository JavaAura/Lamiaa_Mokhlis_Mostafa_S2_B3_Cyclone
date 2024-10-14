package com.cyclone.Util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JPAUtil {

    private static final EntityManagerFactory entityManagerFactory;
    private static final Logger logger = LoggerFactory.getLogger(JPAUtil.class);

    static {
        try {
            logger.info("Attempting to create EntityManagerFactory...");
            entityManagerFactory = Persistence.createEntityManagerFactory("blogJPA");
            logger.info("EntityManagerFactory created successfully.");
        } catch (Exception e) {
           logger.error("Error creating EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError("EntityManagerFactory initialization failed.");
        }
    }

    // Provide an EntityManager instance
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    // Close the EntityManagerFactory
    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
