package commons.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class AbstractFilter implements Filter {
	
	@Override
	public void destroy() {
		//
	}
	
	@Override
	public abstract void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException;
	
	@Override
	public void init(final FilterConfig arg0) throws ServletException {
		//
	}
	
}
