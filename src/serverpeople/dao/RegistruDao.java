/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpeople.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import serverpeople.dao.exceptions.NonexistentEntityException;
import serverpeople.model.MedicDB;
import serverpeople.model.PacientDB;
import serverpeople.model.RegistruDB;

/**
 *
 * @author Israel Dago
 */
public class RegistruDao implements Serializable {

    public RegistruDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RegistruDB registruDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MedicDB medic = registruDB.getMedic();
            if (medic != null) {
                medic = em.getReference(medic.getClass(), medic.getId());
                registruDB.setMedic(medic);
            }
            PacientDB pacient = registruDB.getPacient();
            if (pacient != null) {
                pacient = em.getReference(pacient.getClass(), pacient.getId());
                registruDB.setPacient(pacient);
            }
            em.persist(registruDB);
            if (medic != null) {
                medic.getRegistruDBCollection().add(registruDB);
                medic = em.merge(medic);
            }
            if (pacient != null) {
                pacient.getRegistruDBCollection().add(registruDB);
                pacient = em.merge(pacient);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RegistruDB registruDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RegistruDB persistentRegistruDB = em.find(RegistruDB.class, registruDB.getId());
            MedicDB medicOld = persistentRegistruDB.getMedic();
            MedicDB medicNew = registruDB.getMedic();
            PacientDB pacientOld = persistentRegistruDB.getPacient();
            PacientDB pacientNew = registruDB.getPacient();
            if (medicNew != null) {
                medicNew = em.getReference(medicNew.getClass(), medicNew.getId());
                registruDB.setMedic(medicNew);
            }
            if (pacientNew != null) {
                pacientNew = em.getReference(pacientNew.getClass(), pacientNew.getId());
                registruDB.setPacient(pacientNew);
            }
            registruDB = em.merge(registruDB);
            if (medicOld != null && !medicOld.equals(medicNew)) {
                medicOld.getRegistruDBCollection().remove(registruDB);
                medicOld = em.merge(medicOld);
            }
            if (medicNew != null && !medicNew.equals(medicOld)) {
                medicNew.getRegistruDBCollection().add(registruDB);
                medicNew = em.merge(medicNew);
            }
            if (pacientOld != null && !pacientOld.equals(pacientNew)) {
                pacientOld.getRegistruDBCollection().remove(registruDB);
                pacientOld = em.merge(pacientOld);
            }
            if (pacientNew != null && !pacientNew.equals(pacientOld)) {
                pacientNew.getRegistruDBCollection().add(registruDB);
                pacientNew = em.merge(pacientNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = registruDB.getId();
                if (findRegistruDB(id) == null) {
                    throw new NonexistentEntityException("The registruDB with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RegistruDB registruDB;
            try {
                registruDB = em.getReference(RegistruDB.class, id);
                registruDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The registruDB with id " + id + " no longer exists.", enfe);
            }
            MedicDB medic = registruDB.getMedic();
            if (medic != null) {
                medic.getRegistruDBCollection().remove(registruDB);
                medic = em.merge(medic);
            }
            PacientDB pacient = registruDB.getPacient();
            if (pacient != null) {
                pacient.getRegistruDBCollection().remove(registruDB);
                pacient = em.merge(pacient);
            }
            em.remove(registruDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RegistruDB> findRegistruDBEntities() {
        return findRegistruDBEntities(true, -1, -1);
    }

    public List<RegistruDB> findRegistruDBEntities(int maxResults, int firstResult) {
        return findRegistruDBEntities(false, maxResults, firstResult);
    }

    private List<RegistruDB> findRegistruDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RegistruDB.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public RegistruDB findRegistruDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RegistruDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getRegistruDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RegistruDB> rt = cq.from(RegistruDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
