package com.ign4ci0.spring.boot.web.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * @author ignacio
 * @since Nov 28, 2018
 *
 */
@Component
public class WebServiceRepository
{
    private static final Logger             s_logger = LoggerFactory.getLogger ( WebServiceRepository.class );
    
    @Value ( "${ws.file:wsData.txt}" )
    private String                          wsFileName;
    
    @Autowired
    ResourceLoader                          resourceLoader;
    
    private Map < String, String > _ipTable = null;
    private Map < String, String > _phoneTable = null;
    
    @PostConstruct
    public void initData ( ) throws IOException
    {
        File wsFile = resourceLoader.getResource ( "file:" + wsFileName ).getFile ( );
        _ipTable = new HashMap <> ( );
        _phoneTable = new HashMap<> ( );
        
        if ( wsFile.exists ( ) && wsFile.isFile ( ) && wsFile.canRead ( ) )
        {
            Properties p = new Properties ( );
            p.load ( new FileInputStream ( wsFile ) );
            Enumeration < ? > props = p.propertyNames ( );
            while ( props.hasMoreElements ( ) )
            {
                String key = props.nextElement ( ).toString ( );
                String value = p.getProperty ( key );
                
                _phoneTable.put ( value, key );
                _ipTable.put ( key, value );
            }
            s_logger.info ( "Cargados {} entradas de {}", _ipTable.size ( ), wsFileName );
            s_logger.debug ( "wsFile.lastModified={}", wsFile.lastModified ( ) );
        }
        else
        {
            s_logger.warn ( "wsFile({}) doesn't exist, isn't a file or can't be read!", wsFileName );
        }
    }
    
    /**
     * @param ip
     * @return
     */
    public String generate ( String ip )
    {
        return _ipTable.get ( ip ) != null ? _ipTable.get ( ip ) : "-";
    }

    /**
     * @param phoneNumber
     * @return
     */
    public String generateFromPhone ( String phoneNumber )
    {
        return _phoneTable.get ( phoneNumber ) != null ? _phoneTable.get ( phoneNumber ) : "-";
    }
}
