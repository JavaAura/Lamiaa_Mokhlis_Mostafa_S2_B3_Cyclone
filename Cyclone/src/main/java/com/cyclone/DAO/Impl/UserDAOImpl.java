package com.cyclone.DAO.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.cyclone.DAO.Interface.UserDAO;
import com.cyclone.Model.Admin;
import com.cyclone.Model.Client;
import com.cyclone.Model.User;
import com.cyclone.Util.JPAUtil;

public class UserDAOImpl implements UserDAO {

	private final EntityManager entityManager;

	public UserDAOImpl() {
		this.entityManager = JPAUtil.getEntityManager();
	}

	@Override
	public boolean saveUser(User user) {
		EntityTransaction transaction = null;
		try {
			transaction = entityManager.getTransaction();
			transaction.begin();
			entityManager.persist(user);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public User getUserById(int id) {
		try {
			return entityManager.find(User.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User getUserByEmail(String email) {
	    try {
	        TypedQuery<Admin> adminQuery = entityManager.createQuery("SELECT a FROM Admin a WHERE a.email = :email", Admin.class);
	        adminQuery.setParameter("email", email);
	        List<Admin> adminResults = adminQuery.getResultList();

	        if (!adminResults.isEmpty()) {
	            return adminResults.get(0);
	        } else {
	            TypedQuery<Client> clientQuery = entityManager.createQuery("SELECT c FROM Client c WHERE c.email = :email", Client.class);
	            clientQuery.setParameter("email", email);
	            List<Client> clientResults = clientQuery.getResultList();

	            if (!clientResults.isEmpty()) {
	                return clientResults.get(0);
	            }
	        }
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public List<User> getAllUsers() {
		try {
			// get all Admin users
			TypedQuery<Admin> adminQuery = entityManager.createQuery("SELECT a FROM Admin a", Admin.class);
			List<Admin> admins = adminQuery.getResultList();

			// get all Client users
			TypedQuery<Client> clientQuery = entityManager.createQuery("SELECT c FROM Client c", Client.class);
			List<Client> clients = clientQuery.getResultList();

			// Combine both lists
			List<User> allUsers = new ArrayList<>();
			allUsers.addAll(admins);
			allUsers.addAll(clients);

			return allUsers;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateUser(User user) {
		EntityTransaction transaction = null;
		try {
			transaction = entityManager.getTransaction();
			transaction.begin();
			entityManager.merge(user);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteUser(int id) {
		EntityTransaction transaction = null;
		try {
			transaction = entityManager.getTransaction();
			transaction.begin();
			User user = entityManager.find(User.class, id);
			if (user != null) {
				entityManager.remove(user);
				transaction.commit();
				return true;
			} else {
				transaction.rollback();
				return false;
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public List<User> searchUsersByName(String name) {
	    try {
	        TypedQuery<Admin> adminQuery = entityManager.createQuery(
	            "SELECT a FROM Admin a WHERE a.firstName LIKE :keyword OR a.lastName LIKE :keyword", Admin.class);
	        adminQuery.setParameter("keyword", "%" + name + "%");
	        List<Admin> adminResults = adminQuery.getResultList();

	        TypedQuery<Client> clientQuery = entityManager.createQuery(
	            "SELECT c FROM Client c WHERE c.firstName LIKE :keyword OR c.lastName LIKE :keyword", Client.class);
	        clientQuery.setParameter("keyword", "%" + name + "%");
	        List<Client> clientResults = clientQuery.getResultList();

	        List<User> allUsers = new ArrayList<>();
	        allUsers.addAll(adminResults);
	        allUsers.addAll(clientResults);

	        return allUsers;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

}
