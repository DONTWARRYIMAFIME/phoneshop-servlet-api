package com.es.phoneshop.web.filter;

import com.es.phoneshop.service.impl.secure.DefaultDosProtectionService;
import com.es.phoneshop.service.security.DosProtectionService;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DosFilter implements Filter {

    private DosProtectionService dosProtectionService;

    @Override
    public void init(FilterConfig filterConfig) {
        dosProtectionService = DefaultDosProtectionService.getInstance();
    }

    @Override
    public void destroy() {
        dosProtectionService.shutdown();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (dosProtectionService.isAllowed(request.getRemoteAddr())) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse)response).setStatus(429);
        }
    }
}
