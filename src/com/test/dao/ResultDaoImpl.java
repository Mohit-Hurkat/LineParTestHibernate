package com.test.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import com.test.bean.Result;

public class ResultDaoImpl implements ResultDao {

	private Configuration cfg;
	private SessionFactory factory;
	private Session session;

	public ResultDaoImpl() {
		cfg = new AnnotationConfiguration();
		cfg.configure("hibernate.cfg.xml");
		factory = cfg.buildSessionFactory();

	}

	public List<Result> show(String username) throws ClassNotFoundException, SQLException, IOException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM RESULT WHERE USERNAME=:user");
			query.setString("user", username);
			List<Result> resList = query.list();
			System.out.println(resList);
			tx.commit();
			return resList;
		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	public boolean set(Result result) throws ClassNotFoundException, SQLException, IOException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(result);
			tx.commit();
			return true;

		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return false;
	}

}
