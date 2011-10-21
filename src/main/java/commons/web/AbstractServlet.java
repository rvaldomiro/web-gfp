package commons.web;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class AbstractServlet implements Servlet {
	
	@Override
	public void destroy() {
		//
	}
	
	@Override
	public ServletConfig getServletConfig() {
		return null;
	}
	
	@Override
	public String getServletInfo() {
		return null;
	}
	
	@Override
	public abstract void init(ServletConfig arg0) throws ServletException;
	
	@Override
	public void service(final ServletRequest arg0, final ServletResponse arg1)
			throws ServletException, IOException {
		//
	}
	
}
