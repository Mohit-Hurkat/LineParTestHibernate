package com.test.dao;

import java.io.IOException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import java.sql.SQLException;
import java.util.List;
import com.test.bean.Student;

public class StudentDaoImpl implements StudentDao {

	private Configuration cfg;
	private SessionFactory factory;
	private Session session;

	public StudentDaoImpl() {
		cfg = new AnnotationConfiguration();
		cfg.configure("hibernate.cfg.xml");
		factory = cfg.buildSessionFactory();

	}

	private Student stud = null;

	@Override
	public boolean insert(Student student) throws IOException, ClassNotFoundException, SQLException {
		if (student.getUsername().equals("admin")) {
			System.out.println("admin : Username Not Allowed");
			return false;
		}
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(student);
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
	public Student search(String username) throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			stud = (Student) session.get(Student.class, username);
			tx.commit();
			return stud;

		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Student> displayAll() throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from STUDENT");
			List<Student> stuList = query.list();

			for (Student emp : stuList) {
				System.out.println(emp);
			}
			tx.commit();
			return stuList;
		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public boolean update(String username, Student student) throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			stud = (Student) session.get(Student.class, username);
			stud.setUsername(username);
			stud.setPassword(student.getPassword());
			stud.setName(student.getName());
			stud.setEmail(student.getEmail());
			stud.setPhone(student.getPhone());
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
	public boolean delete(String username) throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			stud = (Student) session.get(Student.class, username);
			session.delete(stud);
			tx.commit();
			return true;

		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return false;
	}

	public boolean check(String username, String password) throws IOException, ClassNotFoundException, SQLException {
		Student checkStudent = search(username);
		if (checkStudent.getUsername().equals(username) && checkStudent.getPassword().equals(password)) {
			return true;
		}
		return false;
	}

}
