package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.web.servlet.DemoDataServletContextListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DemoDataServletContextListenerTest {

    @Mock
    private ProductDao productDao;
    @Mock
    private ServletContext servletContext;
    @Mock
    private ServletContextEvent event;

    @InjectMocks
    private final DemoDataServletContextListener servlet = new DemoDataServletContextListener();

    private final static String INIT_PARAM = "insertDemoData";

    @Before
    public void setup() {
        when(event.getServletContext()).thenReturn(servletContext);
    }

    @Test
    public void testEnableDemoData() {
        when(servletContext.getInitParameter(INIT_PARAM)).thenReturn("true");

        servlet.contextInitialized(event);

        verify(productDao, atLeast(1)).save(any());
        verify(productDao, times(1)).findProducts();
    }

    @Test
    public void testDisableDemoData() {
        when(servletContext.getInitParameter(INIT_PARAM)).thenReturn("false");

        servlet.contextInitialized(event);

        verify(productDao, never()).save(any());
        verify(productDao, never()).findProducts();
    }

}