package com.giordanbetat.projectcloud.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class PosTimeElapsedFilter extends ZuulFilter{
	
	private static Logger log = LoggerFactory.getLogger(PosTimeElapsedFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		log.info("Entry post filter");
		
		Long initTime = (Long) request.getAttribute("initTime");
		Long finalTime = System.currentTimeMillis();
		Long totalTime = finalTime - initTime;
		
		log.info(String.format("Total Time  in seconds %s seg.", totalTime.doubleValue() / 1000.00));
		log.info(String.format("Total Time in milleseconds %s ml.", totalTime));
		
		request.setAttribute("initTime", initTime);
		
		return null;
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	
	
}
