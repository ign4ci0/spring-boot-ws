package com.ign4ci0.spring.boot.web.service;

import java.util.Random;
import java.util.StringTokenizer;

import org.springframework.stereotype.Component;

/**
 * @author ignacio
 * @since Nov 28, 2018
 *
 */
@Component
public class WSGenerator
{
    public String generate ( String ip )
    {
        String concatIp = concatAndFillIp ( ip );
        String phoneNumber = "9".concat ( concatIp.substring ( concatIp.length ( ) - 8, concatIp.length ( ) ) );
        
        return phoneNumber;
    }
    
    /**
     * Concats ip blocks into one <code>String</code> filling every one to reach
     * a length of 3 characters
     * 
     * @param ip
     *            Ip to concat and fill
     * @return <code>String</code> result
     */
    protected synchronized String concatAndFillIp ( String ip )
    {
        String res = "";
        String delimiter = "";
        StringTokenizer st = new StringTokenizer ( ip, "." );
        
        // For each token
        while ( st.hasMoreElements ( ) )
        {
            String token = st.nextElement ( ).toString ( );
            // If token has less than 3 characters, fill it
            // Concat token to result
            res = res.concat ( String.format ( "%03d", Integer.parseInt ( token ) ) ).concat ( delimiter );
            delimiter = ".";
        }
        
        return res;
    }
    
    /**
     * @param phoneNumber
     * @return
     */
    public String generateFromPhone ( String phoneNumber )
    {
        Random r = new Random ( Long.parseLong ( phoneNumber ) );
        return r.nextInt ( 256 ) + "." + r.nextInt ( 256 ) + "." + r.nextInt ( 256 ) + "." + r.nextInt ( 256 );
    }
}
