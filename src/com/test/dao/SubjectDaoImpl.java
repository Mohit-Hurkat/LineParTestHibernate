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
import org.hibernate.type.IntegerType;

import com.test.bean.Subject;

public class SubjectDaoImpl implements SubjectDao {

	private Configuration cfg;
	private SessionFactory factory;
	private Session session;

	public SubjectDaoImpl() {
		cfg = new AnnotationConfiguration();
		cfg.configure("hibernate.cfg.xml");
		factory = cfg.buildSessionFactory();

	}

	private Subject subject = null;

	@Override
	public boolean insert(Subject subject1) throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT MAX(SUBJECT_ID) as value FROM SUBJECT").addScalar("value",
					new IntegerType());
			int subjectId = (int) query.uniqueResult();
			subject1.setSubject_Id(subjectId + 1);
			session.save(subject1);
			tx.commit();
			return true;

		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return false;
	}

	@Override
	public Subject search(int subjectId) throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			subject = (Subject) session.get(Subject.class, subjectId);
			tx.commit();
			return subject;

		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Subject> displayAll() throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from SUBJECT");
			List<Subject> subjectList = query.list();
			tx.commit();
			return subjectList;
		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public boolean update(int subjectId, Subject subject1) throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			subject = (Subject) session.get(Subject.class, subjectId);
			subject.setSubject_Id(subjectId);
			subject.setSubject(subject1.getSubject());
			subject.setStart_(subject1.getStart_());
			subject.setEnd_(subject1.getEnd_());
			tx.commit();
			return true;
		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return false;
	}

	@Override
	public boolean delete(int subjectId) throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			subject = (Subject) session.get(Subject.class, subjectId);
			session.delete(subject);
			tx.commit();
			return true;

		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return false;
	}

	@Override
	public String subname(int subjectId) throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			subject = (Subject) session.get(Subject.class, subjectId);
			String subjectName = subject.getSubject();
			tx.commit();
			return subjectName;

		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Subject> showSubject(String username) throws IOException, ClassNotFoundException, SQLException {

		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from SUBJECT WHERE USERNAME=:user");
			query.setString("user", username);
			List<Subject> subjectList = query.list();
			tx.commit();
			return subjectList;
		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}
}
