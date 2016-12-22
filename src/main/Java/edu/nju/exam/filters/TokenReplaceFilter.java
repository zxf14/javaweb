package edu.nju.exam.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by zhouxiaofan on 2016/12/19.
 */
//@WebFilter(filterName = "TokenReplaceFilter")

public class TokenReplaceFilter implements Filter {
    private String replaceToken,replaceValue;
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        BufferedResponse resWrapper = new BufferedResponse((HttpServletResponse)resp);
        System.out.println("replace filter "+resWrapper.getContentType() );
        chain.doFilter(req, resp);
        if (resWrapper.getOutputType() == BufferedResponse.OT_WRITER) {
            String resBody = new String(resWrapper.toByteArray(),resWrapper.getCharacterEncoding());
            if (resWrapper.getContentType().equals("text/html")) {
                resBody = resBody.replaceAll("@" + replaceToken + "@", replaceValue);
                resp.setContentLength(resBody.length());
            }
            PrintWriter writer = resp.getWriter();
            writer.println(resBody);
        } else if (resWrapper.getOutputType() == BufferedResponse.OT_OUTPUT_STREAM) {
            ServletOutputStream out = resp.getOutputStream();
            out.write(resWrapper.toByteArray());
        }

        }

    public void init(FilterConfig config) throws ServletException {
        replaceToken=config.getInitParameter("token.name");
        replaceValue=config.getInitParameter("token.value");
        if (null==replaceToken){
            throw new ServletException("TokenReplacementFilter named " + config.getFilterName() +" missing token.name init parameter.");

        }
        if (null==replaceValue){
            throw new ServletException("TokenReplacementFilter named " + config.getFilterName() +" missing token.value init parameter.");
        }


    }

}
