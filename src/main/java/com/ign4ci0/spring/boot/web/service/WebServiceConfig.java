package com.ign4ci0.spring.boot.web.service;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * @author ignacio
 * @since Nov 27, 2018
 *
 */
@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter
{
    public static final String NAMESPACE_URI = "http://ign4ci0.com/spring/boot/web/service/model";
    
    @Bean
    public ServletRegistrationBean < MessageDispatcherServlet > messageDispatcherServlet (
            ApplicationContext applicationContext )
    {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet ( );
        servlet.setApplicationContext ( applicationContext );
        servlet.setTransformWsdlLocations ( true );
        return new ServletRegistrationBean < MessageDispatcherServlet > ( servlet, "/ws/*" );
    }
    
    @Bean ( name = "ws" )
    public DefaultWsdl11Definition defaultWsdl11Definition ( XsdSchema wsSchema )
    {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition ( );
        wsdl11Definition.setPortTypeName ( "wsPort" );
        wsdl11Definition.setLocationUri ( "/ws" );
        wsdl11Definition.setTargetNamespace ( NAMESPACE_URI );
        wsdl11Definition.setSchema ( wsSchema );
        return wsdl11Definition;
    }
    
    @Bean
    public XsdSchema wsSchema ( )
    {
        return new SimpleXsdSchema ( new ClassPathResource ( "service.xsd" ) );
    }
}
