package com.es.phoneshop.web.filter;

import com.es.phoneshop.service.security.DosProtectionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private DosProtectionService dosProtectionService;

    @InjectMocks
    private DosFilter filter = new DosFilter();

    private final static String TEST_IP = "127.0.0.1";

    @Before
    public void setup() {
        when(request.getRemoteAddr()).thenReturn(TEST_IP);
    }

    @Test
    public void testFilterForAllowedIp() throws ServletException, IOException {
        when(dosProtectionService.isAllowed(TEST_IP)).thenReturn(true);
        filter.doFilter(request,response,filterChain);
        verify(filterChain, times(1)).doFilter(request,response);
    }

    @Test
    public void testFilterForBannedIp() throws ServletException, IOException {
        when(dosProtectionService.isAllowed(TEST_IP)).thenReturn(false);
        filter.doFilter(request, response, filterChain);
        verify(filterChain, never()).doFilter(request, response);
        verify(response, times(1)).setStatus(429);
    }

}
