package com.skingapp.commen.models;

import com.sking.lib.base.SKObject;
import com.sking.lib.exception.SKDataParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 支付宝交易签名
 */
public class AlipayTrade extends SKObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String alipaysign;

	public AlipayTrade(JSONObject jsonObject) throws SKDataParseException {
		if (jsonObject != null) {
			try {
				alipaysign = get(jsonObject, "alipaysign");

				log_i(toString());
			} catch (JSONException e) {
				throw new SKDataParseException(e);
			}
		}
	}

	@Override
	public String toString() {
		return "AlipayTrade [alipaysign=" + alipaysign + "]";
	}

	/**
	 * @return the alipaysign
	 */
	public String getAlipaysign() {
		return alipaysign;
	}

}
