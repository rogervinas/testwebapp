package com.rogervinas.testwebapp.app.route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogervinas.testwebapp.app.controller.PostParams;
import com.rogervinas.testwebapp.server.ServerRequest;
import com.sun.net.httpserver.HttpExchange;

public class PostParamsFilter extends AbstractFilter
{
	private static final Logger logger = LoggerFactory.getLogger(PostParamsFilter.class);

	@Override
	public Boolean call(ServerRequest request)
	{
		HttpExchange exchange = request.getExchange();
		List<String> contentType = exchange.getRequestHeaders().get("Content-type");
		if(contentType != null && contentType.size() > 0 && contentType.get(0).equals("application/x-www-form-urlencoded")) {
			if(request.contextGet(PostParams.class) == null) {
				PostParams params = new PostParams();
				request.contextPut(params, PostParams.class);
				try(BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
					String urlencoded = reader.readLine();
					if(urlencoded != null && urlencoded.trim().length() > 0) {
						List<NameValuePair> list = URLEncodedUtils.parse(urlencoded, Charset.defaultCharset());
						list.forEach(pair -> {
							params.put(pair.getName(), pair.getValue());
						});
					}
				} catch(IOException e) {
					logger.error("PostParamsFilter.call", e);
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
