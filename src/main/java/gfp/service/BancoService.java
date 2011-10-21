package gfp.service;

import gfp.model.Banco;

import java.util.List;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

@Service
@RemotingDestination
public class BancoService {
	
	@RemotingInclude
	public List<Banco> listarBancos() throws Exception {
		return Banco.listar();
	}
	
}
