package com.greenapper.validators;

import com.greenapper.models.campaigns.CouponCampaign;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

@Component
public class CouponCampaignValidator implements Validator {

	@Resource
	private Validator campaignValidator;

	@Override
	public boolean supports(Class<?> clazz) {
		return CouponCampaign.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		campaignValidator.validate(target, errors);

		final CouponCampaign couponCampaign = (CouponCampaign) target;
		CampaignValidator.rejectStringIfPresentAndTooLong(couponCampaign.getCouponDescription(), "err.campaign.coupon.description", errors);
		CampaignValidator.rejectStringIfPresentAndTooLong(couponCampaign.getCampaignManagerName(), "err.campaign.coupon.managerName", errors);
		CampaignValidator.rejectStringIfPresentAndTooLong(couponCampaign.getCampaignManagerEmail(), "err.campaign.coupon.managerEmail", errors);
		CampaignValidator.rejectStringIfPresentAndTooLong(couponCampaign.getCampaignManagerAddress(), "err.campaign.coupon.managerAddress", errors);
	}
}
