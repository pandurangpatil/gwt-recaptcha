package com.sfeir.captcha.client.ui;

import java.util.Properties;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.sfeir.captcha.shared.CaptchaResult;

/**
 * Simple Widget displaying a captcha.<br />
 * This widget will create a div element and inject the captcha within.
 * 
 * @author François LAROCHE
 */
public class Captcha extends Composite {
	
	/**
	 * default configuration for ReCaptcha, uses the the 'white'
	 */
	private static final Properties DEFAULLT_CONFIGURATION;
	
	static {
		DEFAULLT_CONFIGURATION = new Properties();
		DEFAULLT_CONFIGURATION.setProperty("theme", "white");
		DEFAULLT_CONFIGURATION.setProperty("callback", "Recaptcha.focus_response_field");
	}

	/**
	 * id used to identify in a unic way the div in which the captcha is injected.<br />
	 * Event if it is very unlikely, it is possible to have several captchas on the same page
	 */
	private final String divId;
	
	/**
	 * the public key for ReCaptcha
	 */
	private final String key;
	
	/**
	 * Configuration of the captcha, see the official site for more information
	 */
	private final Properties configuration;

	/**
	 * Constructor of the widget, using the default configuration for the captcha
	 * 
	 * @param key the public key associated with the deployment
	 */
	public Captcha (String key) {
		this(key, DEFAULLT_CONFIGURATION);
	}

	/**
	 * Constructor of the widget
	 * 
	 * @param key the public key associated with the deployment
	 * @param configuration the captcha configuration
	 */
	public Captcha(String key, Properties configuration) {
		this.divId = "captcha-" + System.currentTimeMillis();
		this.key = key;
		this.configuration = configuration;

		HTML content = new HTML("<div id='" + divId + "'></div>");
		initWidget(content);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		injectCaptcha(this.key, this.divId, this.configuration);
	}
	
	/**
	 * Validates and reloads Captcha. <br />
	 * If you need to change your captcha, you call this method.
	 * 
	 * @return the result of the captcha, containong all information for its validation
	 */
	public CaptchaResult validateCaptcha() {
		CaptchaResult result = new CaptchaResult(this.key, getCaptchaChallenge(), getCaptchaResponse());
		injectCaptcha(this.key, this.divId, this.configuration);
		return result;
	}

	/**
	 * Inject captcha into its div
	 * 
	 * @param key the public key
	 * @param divId the id of the div in which to inject the captcha
	 */
	private static native void injectCaptcha(String key, String divId, Properties configuration) /*-{
		var myDiv = $doc.getElementById(divId);
		Recaptcha = $wnd.Recaptcha;
		Recaptcha.create(key, myDiv, configuration);
			// {
			//	theme: "white",
			//	callback: Recaptcha.focus_response_field
			// } 
		// );
	}-*/;

	/**
	 * returns the challenge of the captcha. <br >
	 * A challenge corresponds to the text that needs to be guessed by the client. <br />
	 * The returned String is an encrypted version of the challenge, it is impossible to have a bot guess it automatically
	 * 
	 * @return the challenge of the captcha
	 */
	private static native String getCaptchaChallenge() /*-{ 
		Recaptcha = $wnd.Recaptcha;
		return Recaptcha.get_challenge();
	}-*/;

	/**
	 * returns the response of the captcha.<br />
	 * A response is the text entered by the client. It isn't encrypted
	 * 
	 * @return the response to the captcha entered by the client
	 */
	private static native String getCaptchaResponse() /*-{ 
		Recaptcha = $wnd.Recaptcha;
		return Recaptcha.get_response();
	}-*/;

}