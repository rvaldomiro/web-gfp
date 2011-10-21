package test.gfp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.gfp.model.LancamentoTest;
import test.gfp.service.LancamentoServiceTest;
import test.gfp.service.UsuarioServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ LancamentoServiceTest.class, UsuarioServiceTest.class,
		LancamentoTest.class })
public class AllTestsGfp {
	//
}
