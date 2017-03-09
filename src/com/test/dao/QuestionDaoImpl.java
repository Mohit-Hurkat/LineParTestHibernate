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

import com.test.bean.Question;

public class QuestionDaoImpl implements QuestionDao {
	private Configuration cfg;
	private SessionFactory factory;
	private Session session;

	public QuestionDaoImpl() {
		cfg = new AnnotationConfiguration();
		cfg.configure("hibernate.cfg.xml");
		factory = cfg.buildSessionFactory();

	}

	private Question ques = null;

	@Override
	public boolean insert(Question question) throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT MAX(QUESTION_ID) as value FROM QUESTIONS").addScalar("value",
					new IntegerType());
			int quesId = (int) query.uniqueResult();
			question.setQuestion_Id(quesId + 1);
			session.save(question);
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
	public Question search(int questionId) throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ques = (Question) session.get(Question.class, questionId);
			tx.commit();
			return ques;

		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Question> displayAll(int subjectId) throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from QUESTIONS WHERE SUBJECT_ID=:subjectId");
			query.setInteger("subjectId", subjectId);
			List<Question> quesList = query.list();
			tx.commit();
			return quesList;
		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public boolean update(int questionId, Question question) throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ques = (Question) session.get(Question.class, questionId);
			ques.setAns(question.getAns());
			ques.setAnswer(question.getAnswer());
			ques.setChoice1(question.getChoice1());
			ques.setChoice2(question.getChoice2());
			ques.setChoice3(question.getChoice3());
			ques.setChoice4(question.getChoice4());
			ques.setQuestion(question.getQuestion());
			ques.setQuestion_Id(questionId);
			ques.setSubject_Id(question.getSubject_Id());
			ques.setValue(question.getValue());
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
	public boolean delete(int questionId) throws IOException, ClassNotFoundException, SQLException {
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ques = (Question) session.get(Question.class, questionId);
			session.delete(ques);
			tx.commit();
			return true;

		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return false;
	}

	public String answer(int questionId) throws IOException, ClassNotFoundException, SQLException {

		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ques = (Question) session.get(Question.class, questionId);
			String ans = ques.getAns();
			tx.commit();
			return ans;

		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}
}
