package gfp.service;

import java.io.IOException;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

import commons.file.ConfigProperty;

@Service
@RemotingDestination
public class ApplicationService {
	
	private static String CURRENT_VERSION = null;
	
	@RemotingInclude
	public String obterVersaoAtual() throws IOException {
		if (CURRENT_VERSION == null) {
			CURRENT_VERSION = new ConfigProperty("gfp/config/gfp.properties")
					.getProperty("current-version");
		}
		
		return CURRENT_VERSION;
	}
	
}
