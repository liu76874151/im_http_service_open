package com.qunar.qchat.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create by hubo.hu (lex) at 2018/6/19
 */
public final class PostDataDumpFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(getClass());
    private FilterConfig filterConfig = null;

    public void destroy() {
        this.filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            request = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
        }
        if(response instanceof HttpServletResponse) {
            response = new ResponseWrapper((HttpServletResponse) response);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            String param = this.getBodyString(request.getReader());
            request.setAttribute("postdata", param);

//            logger.info("请求参数" + param);
//            byte[] data = ((ResponseWrapper)response).toByteArray();
//            String result = new String(data,"UTF-8");
            String result = ((ResponseWrapper)response).getTextContent();
//            logger.info("返回结果" + result);
            request.setAttribute("result", result);
        }
    }


    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    private String getBodyString(BufferedReader br) {
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return str;
    }
}