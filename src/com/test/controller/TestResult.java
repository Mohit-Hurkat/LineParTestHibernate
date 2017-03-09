package com.test.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.bean.Question;
import com.test.bean.Result;
import com.test.bean.PrintResult;
import com.test.bl.QuestionLogic;
import com.test.bl.ResultLogic;
import com.test.bl.TestLogic;

public class TestResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private QuestionLogic qLogic = new QuestionLogic();
	private ResultLogic rLogic = new ResultLogic();
	private TestLogic lc = new TestLogic();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String username = (String) session.getAttribute("sessionUsername");
		ArrayList<Question> ques = (ArrayList<Question>) session.getAttribute("Questions");
		List<PrintResult> resultList1 = new ArrayList<>();
		try {
			if (lc.result(username, ques.get(0).getSubject_Id())==0) {
				List<PrintResult> resultList = new ArrayList<>();
				int count = 0;
				Question questtt = null;
				Enumeration<String> questions = request.getParameterNames();
				while (questions.hasMoreElements()) {
					String question = questions.nextElement();
					int questionId = Integer.parseInt(question);
					questtt = qLogic.search(questionId);
					String ans = questtt.getAns();
					System.out.println(ans + " = " + request.getParameter(question));
					System.out.println(request.getParameter(question));
					PrintResult rest = new PrintResult(username, questtt.getSubject_Id(), questionId,
							questtt.getQuestion(), questtt.getAns(), request.getParameter(question));
					resultList.add(rest);
					if (request.getParameter(question).equals(ans)) {
						count++;
					}
					System.out.println(count);
				}
				String Coun = Integer.toString(count * 10).concat(" %");
				session.setAttribute("sessionResultSet", resultList);
				session.setAttribute("sessionResult", count);
				session.setAttribute("message", "You Scored");
				session.setAttribute("message1", Coun);
				Question questt = ques.get(0);
				int subId = questt.getSubject_Id();
				System.out.println(subId);
				Calendar currenttime = Calendar.getInstance();
			    Date sqldate = new Date((currenttime.getTime()).getTime());
				Result result=new Result(username, subId, count * 10,sqldate);
				System.out.println("hellol"+result);
				rLogic.set(result);
				if (session.getAttribute("dontGive") == null)
					response.sendRedirect("./Student/printResult.jsp");
				else {
					response.sendRedirect("./Student/printBack.jsp");
				}
			} else {
				session.setAttribute("message", "Test Already Given");
				session.setAttribute("message1", "Stop Using The Browser Back Button");
				response.sendRedirect("./lost.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}