package com.ign4ci0.spring.boot.web.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.ign4ci0.spring.boot.web.service.model.GetIpAddressByPhoneRequest;
import com.ign4ci0.spring.boot.web.service.model.GetIpAddressByPhoneResponse;
import com.ign4ci0.spring.boot.web.service.model.GetPhoneNumberByIpAddressRequest;
import com.ign4ci0.spring.boot.web.service.model.GetPhoneNumberByIpAddressResponse;

/**
 * @author ignacio
 * @since Nov 27, 2018
 *
 */

@RunWith ( SpringRunner.class )
@SpringBootTest ( webEnvironment = WebEnvironment.RANDOM_PORT )
@TestPropertySource(locations="classpath:test.properties")
public class WebServiceIntegrationTest
{
    private static final Logger s_logger = LoggerFactory.getLogger ( WebServiceIntegrationTest.class );
    private Jaxb2Marshaller marshaller = new Jaxb2Marshaller ( );
    
    @TestConfiguration
    static class WebServiceTestConfiguration
    {
        @PostConstruct
        public void init ( ) throws FileNotFoundException, IOException
        {
            String wsFileName = "/tmp/wsdata.properties";
            Properties properties = new Properties ( );
            properties.setProperty ( "1.1.173.1", "948948601" );
            properties.setProperty ( "1.1.173.2", "948948602" );
            properties.store ( new FileOutputStream ( wsFileName ), null );
            
            s_logger.info ( "wsFile ({}) created.", wsFileName );
        }
    }
    
    
    @LocalServerPort
    private int             port       = 0;
    
    @Before
    public void init ( ) throws Exception
    {
        marshaller.setPackagesToScan (
                        ClassUtils.getPackageName ( GetPhoneNumberByIpAddressRequest.class ),
                        ClassUtils.getPackageName ( GetIpAddressByPhoneRequest.class )
                        );
        marshaller.afterPropertiesSet ( );
    }
    
    @Test
    public void testGetPhoneNumberByIpAddressRequest ( )
    {
        WebServiceTemplate ws = new WebServiceTemplate ( marshaller );
        GetPhoneNumberByIpAddressRequest request = new GetPhoneNumberByIpAddressRequest ( );
        request.setIpAddress ( "1.1.173.1" );
        
        GetPhoneNumberByIpAddressResponse response = ( GetPhoneNumberByIpAddressResponse ) ws.marshalSendAndReceive (
                                                    "http://localhost:" + port + "/ws",
                                                    request
                                                    );
        
        assertThat ( response ).isNotNull ( );
        assertThat ( response.getPhoneNumber ( ) ).isEqualTo ( "948948601" );
    }
    
    @Test
    public void testGetIpAddressByPhoneRequest ( )
    {
        WebServiceTemplate ws = new WebServiceTemplate ( marshaller );
        GetIpAddressByPhoneRequest request = new GetIpAddressByPhoneRequest ( );
        request.setPhoneNumber ( "948948602" );
        
        GetIpAddressByPhoneResponse response = ( GetIpAddressByPhoneResponse ) ws.marshalSendAndReceive (
                                                    "http://localhost:" + port + "/ws",
                                                    request
                                                    );
        
        assertThat ( response ).isNotNull ( );
        assertThat ( response.getIpAddress ( ) ).isEqualTo ( "1.1.173.2" );
    }
}