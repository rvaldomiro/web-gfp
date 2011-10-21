package test.commons.persistence;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import test.commons.persistence.model.Model;

import commons.persistence.hibernate.transaction.TransactionSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "../config/applicationContext-commons.xml" })
public class AbstractEntityTest extends TransactionSupport {
	
	private Model model;
	
	@Before
	public void setup() throws Exception {
		this.model = new Model();
		this.model.deleteAll();
	}
	
	@Test
	public void testAll() throws Exception {
		new Model("test 1", false).save();
		new Model("test 2", false).save();
		new Model("test 3", true).save();
		
		assertEquals(3, this.model.all().size());
	}
	
	@Test
	public void testDelete() throws Exception {
		final Model m1 = new Model("test 1", false).save();
		final Model m2 = new Model("test 2", false).save();
		final Model m3 = new Model("test 3", true).save();
		
		m1.delete();
		assertEquals(2, this.model.all().size());
		
		m2.delete();
		assertEquals(1, this.model.all().size());
		
		m3.delete();
		assertEquals(0, this.model.all().size());
		
		try {
			this.model.delete(" ");
		} catch (final Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertEquals("Conditions cannot be blank", e.getMessage());
		}
	}
	
	@Test
	public void testDeleteAll() throws Exception {
		new Model("test 1", false).save();
		new Model("test 2", false).save();
		new Model("test 3", true).save();
		
		this.model.deleteAll();
		assertEquals(0, this.model.all().size());
	}
	
	@Test
	public void testFind() throws Exception {
		final Long id = new Model("test", false).save().getId();
		final Model result = this.model.find(id);
		assertNotNull(result);
		assertEquals("test", result.getDescription());
	}
	
	@Test
	public void testSave() throws Exception {
		new Model("test 1", false).save();
		assertEquals(1, this.model.all().size());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testTransaction() throws Exception {
		try {
			beginTransaction();
			
			new Model("test 1", true).save();
			new Model("test 2", true).save();
			assertEquals(2, this.model.all().size());
			
			this.model.save();
			
			commitTransaction();
		} catch (final Exception e) {
			rollbackTransaction();
			throw e;
		} finally {
			assertEquals(0, this.model.all().size());
		}
	}
	
	@Test
	public void testWhere() throws Exception {
		try {
			this.model.where(null);
		} catch (final Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertEquals("Conditions cannot be null", e.getMessage());
		}
		
		try {
			this.model.where(" ");
		} catch (final Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertEquals("Conditions cannot be blank", e.getMessage());
		}
		
		assertEquals(0, this.model.where("enabled is true").size());
		assertEquals(0, this.model.where("enabled is false").size());
		
		new Model("test 1", false).save();
		new Model("test 2", false).save();
		new Model("test 3", true).save();
		
		assertEquals(1, this.model.where("enabled is true").size());
		assertEquals(2, this.model.where("enabled is false").size());
		assertEquals(1, this.model.where("description = ?1", "test 2").size());
		assertEquals(
				2,
				this.model.where("description = ?1 or enabled = ?2", "test 1",
						true).size());
		
		final List<Object> params = new ArrayList<Object>();
		params.add("test 1");
		params.add(true);
		assertEquals(2,
				this.model.where("description = ?1 or enabled = ?2", params)
						.size());
	}
	
}
