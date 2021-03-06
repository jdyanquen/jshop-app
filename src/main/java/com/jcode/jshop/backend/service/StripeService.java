package com.jcode.jshop.backend.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcode.jshop.exceptions.StripeException;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;
import com.stripe.model.Token;

@Service
public class StripeService {

	/** The application logger */
	private static final Logger LOG = LoggerFactory.getLogger(StripeService.class);
	
	@Autowired
	private String stripeKey;
	
	public String createCustomer(Map<String, Object> tokenParams, Map<String, Object> customerParams) {
		Stripe.apiKey = stripeKey;
		
		String stripeCustomerId = null;
		try {
			Token token = Token.create(tokenParams);
			customerParams.put("source", token.getId());
			Customer customer = Customer.create(customerParams);
			stripeCustomerId = customer.getId();
			
		} catch (AuthenticationException e) {
			LOG.error("An error authentication exception occured while creating the Stripe customer", e);
			throw new StripeException();
		} catch(InvalidRequestException e) {
			LOG.error("An invalid request exception occured while creating the Stripe customer", e);
			throw new StripeException();
		} catch(APIConnectionException e) {
			LOG.error("An API connection exception occured while creating the Stripe customer", e);
			throw new StripeException();
		} catch(CardException e) {
			LOG.error("An Credit Card exception occured while creating the Stripe customer", e);
			throw new StripeException();
		} catch(APIException e) {
			LOG.error("An API exception occured while creating the Stripe customer", e);
			throw new StripeException();
		}
		return stripeCustomerId;
	}
}
