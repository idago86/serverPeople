/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpeople.service;

import dto.MedicDTO;
import dto.PacientDTO;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import serverpeople.dao.MedicDao;
import serverpeople.dao.PacientDao;
import serverpeople.model.MedicDB;
import serverpeople.model.PacientDB;

/**
 *
 * @author Israel Dago
 */
public class ServerPeopleServices {
    private EntityManagerFactory emf;
    private MedicDao medicDao;
    private PacientDao pacientDao;
    
    private ServerPeopleServices() {
        emf = Persistence.createEntityManagerFactory("serverPeoplePU");
        medicDao = new MedicDao(emf);
        pacientDao = new PacientDao(emf);
    }
    
    private static final class SingletonHolder{
        private static final ServerPeopleServices SINGLETON = new ServerPeopleServices();
    }
    
    public static ServerPeopleServices getInstance(){
        return SingletonHolder.SINGLETON;
    }
    
    public void adaugaMedic(MedicDTO medic){
        medicDao.create(new MedicDB(0, medic.getNume(), medic.getMatricula()));
    }
    
    public void adaugaPacient(PacientDTO pacient){
        pacientDao.create(new PacientDB(0, pacient.getNume(), pacient.getCnp()));
    }
    
    
    
    
}
