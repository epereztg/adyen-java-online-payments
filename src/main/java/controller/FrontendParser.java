package controller;

import com.adyen.model.PaymentResult;
import com.adyen.model.checkout.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.PaymentMethodDetailsDeserializer;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class FrontendParser {

	// Deserialize payment information passed from frontend. Requires TypeAdapter for PaymentMethodDetails interface
	public static PaymentsRequest parsePayment(String body) {

		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(PaymentMethodDetails.class, new PaymentMethodDetailsDeserializer());
		Gson gson = builder.create();
		PaymentsRequest paymentsRequest = gson.fromJson(body, PaymentsRequest.class);
		return paymentsRequest;


	}

	// Deserialize PaymentDetails generated by component
	public static PaymentsDetailsRequest parseDetails(String body) {

		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(PaymentMethodDetails.class, new PaymentMethodDetailsDeserializer());
		Gson gson = builder.create();
		PaymentsDetailsRequest paymentDetailsRequest = gson.fromJson(body, PaymentsDetailsRequest.class);
		return paymentDetailsRequest;
	}

	// Format response being passed back to frontend. Only leave resultCode and action. Don't need to pass back
	// The rest of the information
	public static PaymentsResponse formatResponseForFrontend(PaymentsResponse unformattedResponse) throws IOException {

		PaymentsResponse.ResultCodeEnum resultCode = unformattedResponse.getResultCode();
		if (resultCode != null) {
			PaymentsResponse newPaymentsResponse = new PaymentsResponse();
			newPaymentsResponse.setResultCode(resultCode);

			CheckoutPaymentsAction action = unformattedResponse.getAction();
			if (action != null) {
				newPaymentsResponse.setAction(action);
			}
			return newPaymentsResponse;
		} else {
			throw new IOException();
		}
	}

	// Doesn't handle nested objects right now. Will see if neccessary
	public static List<NameValuePair> parseQueryParams(String queryString) {
		List<NameValuePair> params = URLEncodedUtils.parse(queryString, Charset.forName("UTF-8"));

		return params;
	}
}
