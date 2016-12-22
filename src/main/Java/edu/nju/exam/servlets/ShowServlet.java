package edu.nju.exam.servlets;

import edu.nju.exam.Model.Score;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import static java.lang.Integer.parseInt;

/**
 * Created by zhouxiaofan on 2016/12/8.
 */

@WebServlet("/show")
public class ShowServlet extends javax.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;
    private DataSource datasource = null;
    public void init() {
        InitialContext jndiContext = null;
        Properties properties = new Properties();
        properties.put(javax.naming.Context.PROVIDER_URL, "jnp:///");
        properties.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        try {
            jndiContext = new InitialContext(properties);
            datasource = (DataSource) jndiContext.lookup("java:comp/env/jdbc/test");
            System.out.println("got context");
            System.out.println("About to get ds---ShowMyStock");
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        PrintWriter out=response.getWriter();

    }

    private void displayExamList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList examList= (ArrayList) request.getAttribute("list");

        //获得课程信息并展示
        PrintWriter out = response.getWriter();
        out.println("<html><body>");

        for (int i=0;i<examList.size();i++){
            Score s= (Score) examList.get(i);
            System.out.println(s.getEname()+"  "+s.getScore());
            out.print("<p>"+s.getEname()+"  ");
            out.println(s.getScore()+"  </p>");
        }
    }

    private void displayLogoutPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
//注销Logout
        out.println("<form method='GET' action='" + response.encodeURL(request.getContextPath() + "/login") + "'>");
        out.println("</p>");
        out.println("<input type='submit' name='Logout' value='Logout'>");
        out.println("</form>");
        out.print("</body></html>");

    }

    private void warnExam(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        PrintWriter pw=null;
        try {
            pw = response.getWriter();
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>测试未参加</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("还有测试: "+request.getAttribute("unfinished")+" 未参加");
            pw.println("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getExamList(HttpServletRequest request, HttpServletResponse response,int userId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        ArrayList list = new ArrayList();
        try {
            connection = datasource.getConnection();
            stmt = connection.prepareStatement("select exam.name,score.s from `exam` LEFT JOIN (SELECT * from score WHERE score.uid=?) as score ON exam.id=score.eid;");
            stmt.setInt(1, userId);
            result = stmt.executeQuery();
            while (result.next()) {
                String ename=result.getString("name");
                double score=result.getDouble(2);
                if (score==0){
                    //加没有考试的记录
                    String s= (String) request.getAttribute("unfinished");
                    if (s!=null)
                        request.setAttribute("unfinished", s+"  "+ename);
                    else
                        request.setAttribute("unfinished", ename);
                }
                else {

                    list.add(new Score(ename,score));
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        request.setAttribute("list", list);
    }




    protected void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws javax.servlet.ServletException, IOException {
        ServletContext Context= getServletContext();
        int pageCounter= (Integer) Context.getAttribute("webCounter");
        int userCounter= (Integer) Context.getAttribute("userCounter");

        HttpSession session = req.getSession(true);
        Integer userId= (Integer) session.getAttribute("userId");
        if (userId==null){
            req.getRequestDispatcher("/login").forward(req,res);
            return;
        }

        getExamList(req,res,userId);

        //有考试没有参加，警示
        if(req.getAttribute("unfinished")!=null){
            warnExam(req,res);
            System.out.println("unfinished  "+(String) req.getAttribute("unfinished"));
            return;
        }
        displayExamList(req, res);

        PrintWriter out = res.getWriter();
        out.println("</p>在线总人数： " + pageCounter);
        out.println("</p>已登录用户： " + userCounter);
        out.println("</p>游客人数： " + (pageCounter-userCounter));
        displayLogoutPage(req, res);
    }




}
