package edu.nju.exam.servlets;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Created by zhouxiaofan on 2016/12/20.
 */
@WebServlet(name = "LoginServlet",urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        boolean cookieFound = false;

        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            // Look through all the cookies and see if the
            // cookie with the login info is there.
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals("userName")) {
                    cookieFound = true;
                    break;
                }
            }
        }

        String psw = request.getParameter("psw");
        String name = request.getParameter("name");
        if (cookieFound) { // If the cookie exists update the value only
            // if changed
            if (!name.equals(cookie.getValue())) {
                cookie.setValue(name);
                response.addCookie(cookie);
            }
        } else {
            // If the cookie does not exist, create it and set value
            cookie = new Cookie("userName", name);
            cookie.setMaxAge(Integer.MAX_VALUE);
            System.out.println("Add cookie");
            response.addCookie(cookie);
        }
        //验证成功
        if(varifyUser(request,name,psw)){
            response.sendRedirect("/show");
        }//验证失败
        else {
            loginFail(request,response);
        }

    }
    private void loginFail(HttpServletRequest request, HttpServletResponse response) {
//        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter pw=null;
        try {
            pw = response.getWriter();
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>登录失败</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("未知的用户id");
            pw.println("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (null == request.getParameter("Logout")) {
//            pageCounter++;
//            Context.setAttribute("webCounter", Integer.toString(pageCounter));
        }

        String login="";
        HttpSession session = request.getSession(true);
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            // Look through all the cookies and see if the
            // cookie with the login info is there.
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals("userName")) {
                    login=cookie.getValue();
                    break;
                }
            }
        }
        request.setAttribute("login",login);
        // Logout action removes session, but the cookie remains
        if (null != request.getParameter("Logout")) {
            if (null != session) {
                session.invalidate();
                session = null;
                session = request.getSession(true);
            }
        }

        ServletContext Context= getServletContext();
        int pageCounter= (Integer) Context.getAttribute("webCounter");
        int userCounter= (Integer) Context.getAttribute("userCounter");
        request.setAttribute("counter",pageCounter);
        request.setAttribute("userCounter",userCounter);

        RequestDispatcher dispatcher =request.getRequestDispatcher("/login.jsp");
        if (dispatcher!= null)
            dispatcher.include(request,response);

    }
    private boolean varifyUser(HttpServletRequest request,String name, String psw) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet result = null;
        try {
            connection = datasource.getConnection();
            stmt = connection.createStatement();
            result=stmt.executeQuery("select id from `user` where name=\""+name+"\" and password=\""+psw+"\";");
            System.out.println(name+" + "+psw);
            if (!result.next())
                return false ;

            //加session
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", result.getInt("id"));

            result.close();
            stmt.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }
}
