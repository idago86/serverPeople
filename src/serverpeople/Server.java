/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpeople;

import java.net.ServerSocket;
import serverpeople.proxy.ServerPeopleProxy;

/**
 *
 * @author Israel Dago
 */
public class Server {
    private static ServerSocket ss;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ss = new ServerSocket(3233);
            while (true) {                
                new ServerPeopleProxy(ss.accept()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
