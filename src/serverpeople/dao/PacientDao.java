/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpeople.dao;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import serverpeople.model.RegistruDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import serverpeople.dao.exceptions.IllegalOrphanException;
import serverpeople.dao.exceptions.NonexistentEntityException;
import serverpeople.model.PacientDB;

/**
 *
 * @author Israel Dago
 */
public class PacientDao implements Serializable {

    public PacientDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PacientDB pacientDB) {
        if (pacientDB.getRegistruDBCollection() == null) {
            pacientDB.setRegistruDBCollection(new ArrayList<RegistruDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<RegistruDB> attachedRegistruDBCollection = new ArrayList<RegistruDB>();
            for (RegistruDB registruDBCollectionRegistruDBToAttach : pacientDB.getRegistruDBCollection()) {
                registruDBCollectionRegistruDBToAttach = em.getReference(registruDBCollectionRegistruDBToAttach.getClass(), registruDBCollectionRegistruDBToAttach.getId());
                attachedRegistruDBCollection.add(registruDBCollectionRegistruDBToAttach);
            }
            pacientDB.setRegistruDBCollection(attachedRegistruDBCollection);
            em.persist(pacientDB);
            for (RegistruDB registruDBCollectionRegistruDB : pacientDB.getRegistruDBCollection()) {
                PacientDB oldPacientOfRegistruDBCollectionRegistruDB = registruDBCollectionRegistruDB.getPacient();
                registruDBCollectionRegistruDB.setPacient(pacientDB);
                registruDBCollectionRegistruDB = em.merge(registruDBCollectionRegistruDB);
                if (oldPacientOfRegistruDBCollectionRegistruDB != null) {
                    oldPacientOfRegistruDBCollectionRegistruDB.getRegistruDBCollection().remove(registruDBCollectionRegistruDB);
                    oldPacientOfRegistruDBCollectionRegistruDB = em.merge(oldPacientOfRegistruDBCollectionRegistruDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PacientDB pacientDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PacientDB persistentPacientDB = em.find(PacientDB.class, pacientDB.getId());
            Collection<RegistruDB> registruDBCollectionOld = persistentPacientDB.getRegistruDBCollection();
            Collection<RegistruDB> registruDBCollectionNew = pacientDB.getRegistruDBCollection();
            List<String> illegalOrphanMessages = null;
            for (RegistruDB registruDBCollectionOldRegistruDB : registruDBCollectionOld) {
                if (!registruDBCollectionNew.contains(registruDBCollectionOldRegistruDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RegistruDB " + registruDBCollectionOldRegistruDB + " since its pacient field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<RegistruDB> attachedRegistruDBCollectionNew = new ArrayList<RegistruDB>();
            for (RegistruDB registruDBCollectionNewRegistruDBToAttach : registruDBCollectionNew) {
                registruDBCollectionNewRegistruDBToAttach = em.getReference(registruDBCollectionNewRegistruDBToAttach.getClass(), registruDBCollectionNewRegistruDBToAttach.getId());
                attachedRegistruDBCollectionNew.add(registruDBCollectionNewRegistruDBToAttach);
            }
            registruDBCollectionNew = attachedRegistruDBCollectionNew;
            pacientDB.setRegistruDBCollection(registruDBCollectionNew);
            pacientDB = em.merge(pacientDB);
            for (RegistruDB registruDBCollectionNewRegistruDB : registruDBCollectionNew) {
                if (!registruDBCollectionOld.contains(registruDBCollectionNewRegistruDB)) {
                    PacientDB oldPacientOfRegistruDBCollectionNewRegistruDB = registruDBCollectionNewRegistruDB.getPacient();
                    registruDBCollectionNewRegistruDB.setPacient(pacientDB);
                    registruDBCollectionNewRegistruDB = em.merge(registruDBCollectionNewRegistruDB);
                    if (oldPacientOfRegistruDBCollectionNewRegistruDB != null && !oldPacientOfRegistruDBCollectionNewRegistruDB.equals(pacientDB)) {
                        oldPacientOfRegistruDBCollectionNewRegistruDB.getRegistruDBCollection().remove(registruDBCollectionNewRegistruDB);
                        oldPacientOfRegistruDBCollectionNewRegistruDB = em.merge(oldPacientOfRegistruDBCollectionNewRegistruDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pacientDB.getId();
                if (findPacientDB(id) == null) {
                    throw new NonexistentEntityException("The pacientDB with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PacientDB pacientDB;
            try {
                pacientDB = em.getReference(PacientDB.class, id);
                pacientDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pacientDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<RegistruDB> registruDBCollectionOrphanCheck = pacientDB.getRegistruDBCollection();
            for (RegistruDB registruDBCollectionOrphanCheckRegistruDB : registruDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PacientDB (" + pacientDB + ") cannot be destroyed since the RegistruDB " + registruDBCollectionOrphanCheckRegistruDB + " in its registruDBCollection field has a non-nullable pacient field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pacientDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PacientDB> findPacientDBEntities() {
        return findPacientDBEntities(true, -1, -1);
    }

    public List<PacientDB> findPacientDBEntities(int maxResults, int firstResult) {
        return findPacientDBEntities(false, maxResults, firstResult);
    }

    private List<PacientDB> findPacientDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PacientDB.class));
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

    public PacientDB findPacientDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PacientDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacientDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PacientDB> rt = cq.from(PacientDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
