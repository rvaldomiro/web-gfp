package gfp.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import logus.commons.persistence.ApplicationContext;
import logus.commons.web.AbstractFilter;

public class GfpFilter extends AbstractFilter {
	
	@Override
	public void doFilter(final ServletRequest arg0, final ServletResponse arg1,
			final FilterChain arg2) throws IOException, ServletException {
		try {
			arg2.doFilter(arg0, arg1);
		} finally {
			ApplicationContext.closeAllCurrentSessions();
		}
	}
	
}