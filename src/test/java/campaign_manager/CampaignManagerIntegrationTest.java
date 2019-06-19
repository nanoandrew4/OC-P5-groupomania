package campaign_manager;

import com.greenapper.Main;
import com.greenapper.controllers.CampaignManagerController;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.PasswordUpdate;
import com.greenapper.services.CampaignManagerService;
import com.greenapper.services.SessionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = Main.class
)
public class CampaignManagerIntegrationTest {
	@Autowired
	private CampaignManagerController campaignManagerController;

	@Autowired
	private CampaignManagerService campaignManagerService;

	@Autowired
	private SessionService sessionService;

	@Test
	public void updatePasswordOldPasswordInvalid() {
		final CampaignManager campaignManager = campaignManagerService.getByUsername("admin").orElse(null);

		assertNotNull(campaignManager);

		sessionService.setSessionUser(campaignManager);

		final PasswordUpdate passwordUpdate = new PasswordUpdate();
		final BindingResult bindingResult = new BeanPropertyBindingResult(passwordUpdate, "passwordUpdate");
		passwordUpdate.setOldPassword("wrongpass");
		passwordUpdate.setNewPassword("12345678");

		final String ret = campaignManagerController.updatePassword(passwordUpdate, bindingResult);

		assertEquals(ret, CampaignManagerController.PASSWORD_UPDATE_FORM);
		assertEquals(1, bindingResult.getErrorCount());
		assertEquals("err.password.mismatch", bindingResult.getAllErrors().get(0).getCode());
	}

	@Test
	public void updatePasswordLessThan6Chars() {
		final CampaignManager campaignManager = campaignManagerService.getByUsername("admin").orElse(null);

		assertNotNull(campaignManager);

		sessionService.setSessionUser(campaignManager);

		final PasswordUpdate passwordUpdate = new PasswordUpdate();
		final BindingResult bindingResult = new BeanPropertyBindingResult(passwordUpdate, "passwordUpdate");
		passwordUpdate.setOldPassword("testing");
		passwordUpdate.setNewPassword("12345");

		final String ret = campaignManagerController.updatePassword(passwordUpdate, bindingResult);

		assertEquals(ret, CampaignManagerController.PASSWORD_UPDATE_FORM);
		assertEquals(1, bindingResult.getErrorCount());
		assertEquals("err.password.length", bindingResult.getAllErrors().get(0).getCode());
	}

	@Test
	public void updatePasswordSamePassword() {
		final CampaignManager campaignManager = campaignManagerService.getByUsername("admin").orElse(null);

		assertNotNull(campaignManager);

		sessionService.setSessionUser(campaignManager);

		final PasswordUpdate passwordUpdate = new PasswordUpdate();
		final BindingResult bindingResult = new BeanPropertyBindingResult(passwordUpdate, "passwordUpdate");
		passwordUpdate.setOldPassword("testing");
		passwordUpdate.setNewPassword("testing");

		final String ret = campaignManagerController.updatePassword(passwordUpdate, bindingResult);

		assertEquals(CampaignManagerController.PASSWORD_UPDATE_FORM, ret);
		assertEquals(1, bindingResult.getErrorCount());
		assertEquals("err.password.samepassword", bindingResult.getAllErrors().get(0).getCode());
	}

	@Test
	@DirtiesContext
	public void updatePasswordSuccessfully() {
		final CampaignManager campaignManager = campaignManagerService.getByUsername("admin").orElse(null);

		assertNotNull(campaignManager);

		sessionService.setSessionUser(campaignManager);

		final PasswordUpdate passwordUpdate = new PasswordUpdate();
		final BindingResult bindingResult = new BeanPropertyBindingResult(passwordUpdate, "passwordUpdate");
		passwordUpdate.setOldPassword("testing");
		passwordUpdate.setNewPassword("newpassword");

		final String ret = campaignManagerController.updatePassword(passwordUpdate, bindingResult);

		assertEquals(CampaignManagerController.PASSWORD_UPDATE_SUCCESS_REDIRECT, ret);
		assertFalse(bindingResult.hasErrors());
	}
}
