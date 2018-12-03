package com.ign4ci0.spring.boot.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.ign4ci0.spring.boot.web.service.model.GetIpAddressByPhoneRequest;
import com.ign4ci0.spring.boot.web.service.model.GetIpAddressByPhoneResponse;
import com.ign4ci0.spring.boot.web.service.model.GetPhoneNumberByIpAddressRequest;
import com.ign4ci0.spring.boot.web.service.model.GetPhoneNumberByIpAddressResponse;

/**
 * @author ignacio
 * @since Nov 27, 2018
 *
 */
@Endpoint
public class WebServiceEndpoint
{
    private static final Logger s_logger = LoggerFactory.getLogger ( WebServiceEndpoint.class );
    
    @Value( "${ws.source:generate}" )
    private String wsSource;
    
    private WebServiceRepository     wsRepository;
    private WSGenerator     wsGenerator;
    
    @Autowired
    public WebServiceEndpoint ( WebServiceRepository wsRepository, WSGenerator wsGenerator )
    {
        this.wsRepository = wsRepository;
        this.wsGenerator = wsGenerator;
    }
    
    @PayloadRoot ( namespace = WebServiceConfig.NAMESPACE_URI, localPart = "getPhoneNumberByIpAddressRequest" )
    @ResponsePayload
    public GetPhoneNumberByIpAddressResponse getPhoneNumberByIpAddress (
            @RequestPayload GetPhoneNumberByIpAddressRequest request )
    {
        s_logger.info ( "getPhoneNumberByIpAddress(ip={}): wsSource={}", request.getIpAddress ( ), wsSource );
        Assert.notNull ( request.getIpAddress ( ), "Ip Address can't be null!!" );
        
        GetPhoneNumberByIpAddressResponse response = new GetPhoneNumberByIpAddressResponse ( );
        String phoneNumber = "-";
        
        switch ( wsSource )
        {
            case "generate" :
                phoneNumber = wsGenerator.generate ( request.getIpAddress ( ) );
                break;
                
            case "file" :
                phoneNumber = wsRepository.generate ( request.getIpAddress ( ) );
                break;
            
            default :
                s_logger.warn ( "Unknown ws source({}), nothing to do!!", wsSource );
                break;
        }
        
        response.setPhoneNumber ( phoneNumber );
        s_logger.info ( "getPhoneNumberByIpAddress.response={}", response );
        
        return response;
    }
    
    @PayloadRoot ( namespace = WebServiceConfig.NAMESPACE_URI, localPart = "getIpAddressByPhoneRequest" )
    @ResponsePayload
    public GetIpAddressByPhoneResponse getIpAddressByPhone (
            @RequestPayload GetIpAddressByPhoneRequest request )
    {
        s_logger.info ( "getIpAddressByPhone(ip={}): sidiSource={}", request.getPhoneNumber ( ), wsSource );
        
        GetIpAddressByPhoneResponse response = new GetIpAddressByPhoneResponse ( );
        String ipAddress = "-";
        
        switch ( wsSource )
        {
            case "generate" :
                ipAddress = wsGenerator.generateFromPhone ( request.getPhoneNumber ( ) );
                break;
                
            case "file" :
                ipAddress = wsRepository.generateFromPhone ( request.getPhoneNumber ( ) );
                break;
            
            default :
                s_logger.warn ( "Unknown sidi source({}), nothing to do!!", wsSource );
                break;
        }
        response.setIpAddress ( ipAddress );
        s_logger.info ( "getIpAddressByPhone.response={}", response );
        
        return response;
    }
}
