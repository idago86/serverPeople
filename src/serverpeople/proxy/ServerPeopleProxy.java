/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpeople.proxy;

import dto.MedicDTO;
import dto.PacientDTO;
import dto.Request;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import serverpeople.service.ServerPeopleServices;

/**
 *
 * @author Israel Dago
 */
public class ServerPeopleProxy extends Thread{
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ServerPeopleProxy(Socket socket) throws IOException {
        this.socket = socket;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input= new ObjectInputStream(socket.getInputStream());
    }
    
    @Override
    public void run(){
        try {
            Request request = null;
            while (true) {                
                
                //add Medic
                if(request.getCod() == Request.ADD_MEDIC){
                    MedicDTO medic = (MedicDTO) input.readObject();
                    ServerPeopleServices.getInstance().adaugaMedic(medic);                
                }
                
                //add Pacient
                if(request.getCod() == Request.ADD_PACIENT){
                    PacientDTO pacient = (PacientDTO) input.readObject();
                    ServerPeopleServices.getInstance().adaugaPacient(pacient);
                }
                
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    
}
