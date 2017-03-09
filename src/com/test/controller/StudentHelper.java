package com.test.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.bean.Question;
import com.test.bean.Result;
import com.test.bean.Student;
import com.test.bean.Subject;
import com.test.bl.ResultLogic;
import com.test.bl.StudentLogic;
import com.test.bl.SubjectLogic;
import com.test.bl.TestLogic;

public class StudentHelper extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudentLogic lc = new StudentLogic();
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("display") != null) {
			HttpSession session = request.getSession(false);
			SubjectLogic lc = new SubjectLogic();
			try {
				List<Subject> sub = lc.displayAll();
				if (sub != null) {
					request.setAttribute("subjectDisplay", sub);// use this
																// attribute to
																// display data
					RequestDispatcher dispatch = request.getRequestDispatcher("./Student/studentGiveTest.jsp");
					dispatch.forward(request, response);
				} else {
					session.setAttribute("message", "");// use this attribute to
														// abstract info
					session.setAttribute("message1", "");// use this attribute
															// to abstract info
					response.sendRedirect("./lost.jsp");
				}
			} catch (ClassNotFoundException | SQLException | NullPointerException e) {
				session.setAttribute("message", "Server Error!!!");
				session.setAttribute("message1", "Please Contact The Administrator.");
				response.sendRedirect("./lost.jsp");
			}
		}

		else if (request.getParameter("update") != null) {
			HttpSession session = request.getSession(false);
			try {
				String suname = (String) session.getAttribute("sessionUsername");
				String spass = request.getParameter("updatePassword");
				String sname = request.getParameter("updateName");
				String sphone = request.getParameter("updatePhone");
				String semail = request.getParameter("updateEmail");
				Student student = new Student(suname, spass, sname, sphone, semail);

				if (lc.update(suname, student)) {
					request.setAttribute("studentUpdate", "Successfully Updated.");
					RequestDispatcher dispatch = request.getRequestDispatcher("./Student/updateStudent.jsp");
					dispatch.forward(request, response);
				} else {
					session.setAttribute("message", "Update Unsuccessful.");// use
																			// this
																			// attribute
																			// to
																			// abstract
																			// info
					session.setAttribute("message1", "");// use this attribute
															// to abstract info
					response.sendRedirect("./lost.jsp");
				}
			} catch (ClassNotFoundException | SQLException e) {
				session.setAttribute("message", "Server Error!!!");
				session.setAttribute("message1", "Please Contact The Administrator.");
				response.sendRedirect("./lost.jsp");
			}

		}

		else if (request.getParameter("result") != null)// check the parameter
														// name
		{
			HttpSession session = request.getSession(false);
			TestLogic lc = new TestLogic();
			String username = (String) session.getAttribute("sessionUsername");
			int subjectId = Integer.parseInt(request.getParameter("subjectId"));
			try {
				int res = lc.result(username, subjectId);
				if (res > 0) {
					request.setAttribute("testResult", res);// use this
															// attribute to
															// abstract info
					RequestDispatcher dispatch = request.getRequestDispatcher("./studentResult.jsp");
					dispatch.forward(request, response);
				} else {
					session.setAttribute("message", "");// use this attribute to
														// abstract info
					session.setAttribute("message1", "");// use this attribute
															// to abstract info
					response.sendRedirect("./lost.jsp");
				}
			} catch (ClassNotFoundException | SQLException e) {
				session.setAttribute("message", "Server Error!!!");
				session.setAttribute("message1", "Please Contact The Administrator.");
				response.sendRedirect("./lost.jsp");
			}

		}

		else if (request.getParameter("retrieve") != null) {
			HttpSession session = request.getSession(false);
			String studentName = (String) session.getAttribute("sessionUsername");
			try {
				Student student = lc.search(studentName);
				session.setAttribute("studentUpdate", student);
				response.sendRedirect("./Student/studentUpdateInfo.jsp");
			} catch (ClassNotFoundException | SQLException e) {
				session.setAttribute("message", "Server Error!!!");
				session.setAttribute("message1", "Please Contact The Administrator.");
				response.sendRedirect("./lost.jsp");
			}

		}

		else if (request.getParameter("login") != null) {
			HttpSession session = request.getSession();
			String user = request.getParameter("username");
			String pass = request.getParameter("password");
			Student student = new Student(user, pass, "mohit", "9987588772", "mohit.hukat@gmail.com");
			try {
				if (lc.check(user, pass)) {

					session.setAttribute("student", student);
					student = lc.search(user);
					session.setAttribute("sessionName", student.getName());
					session.setAttribute("sessionUsername", user);
//					response.sendRedirect("./Student/studentPreloader.jsp");
					response.sendRedirect("./Student/student.jsp");
				} else {
					session.setAttribute("message", "Invalid Credentials");
					session.setAttribute("message1", "Visit lenskart for better vision.");
					response.sendRedirect("./lost.jsp");
				}
			} catch (NullPointerException e) {
				request.setAttribute("message", "Invalid Credentials");
				request.setAttribute("message1", "Visit lenskart for better vision.");
				RequestDispatcher dispatch = request.getRequestDispatcher("./lost.jsp");
				dispatch.forward(request, response);
			} catch (ClassNotFoundException | SQLException e) {
				session.setAttribute("message", "Server Error!!!");
				session.setAttribute("message1", "Please Contact The Administrator.");
				response.sendRedirect("./lost.jsp");
			}

		} else if (request.getParameter("resultstudent") != null) {// check the
																	// parameter
																	// name
			HttpSession session = request.getSession(false);
			ResultLogic rc = new ResultLogic();
			String username = (String) session.getAttribute("sessionUsername");
			try {
				List<Result> result1 = rc.show(username);
				System.out.println(result1);
				List<Question> result2 = new ArrayList<>();
				if (!result1.equals(result2)) {
					session.setAttribute("testResult", result1);// use this
																// attribute to
																// display data
					response.sendRedirect("./Student/studentPrevResult.jsp");
				} else {
					session.setAttribute("message", "No Test Given");// use this
																		// attribute
																		// to
																		// abstract
																		// info
					session.setAttribute("message1", "Hence, No Result Found");// use
																				// this
																				// attribute
																				// to
																				// abstract
																				// info
					response.sendRedirect("./lost.jsp");
				}
			} catch (ClassNotFoundException | SQLException e) {
				session.setAttribute("message", "Server Error!!!");
				session.setAttribute("message1", "Please Contact The Administrator.");
				response.sendRedirect("./lost.jsp");
			}

		} else if (request.getParameter("printt") != null) {// check the
															// parameter name
			HttpSession session = request.getSession(false);
			if (session.getAttribute("giveTestSession") != null) {
				session.setAttribute("giveTestSession", null);
			}
			response.sendRedirect("./Student/student.jsp");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
