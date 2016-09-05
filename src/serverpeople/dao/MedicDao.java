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
import serverpeople.model.MedicDB;

/**
 *
 * @author Israel Dago
 */
public class MedicDao implements Serializable {

    public MedicDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MedicDB medicDB) {
        if (medicDB.getRegistruDBCollection() == null) {
            medicDB.setRegistruDBCollection(new ArrayList<RegistruDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<RegistruDB> attachedRegistruDBCollection = new ArrayList<RegistruDB>();
            for (RegistruDB registruDBCollectionRegistruDBToAttach : medicDB.getRegistruDBCollection()) {
                registruDBCollectionRegistruDBToAttach = em.getReference(registruDBCollectionRegistruDBToAttach.getClass(), registruDBCollectionRegistruDBToAttach.getId());
                attachedRegistruDBCollection.add(registruDBCollectionRegistruDBToAttach);
            }
            medicDB.setRegistruDBCollection(attachedRegistruDBCollection);
            em.persist(medicDB);
            for (RegistruDB registruDBCollectionRegistruDB : medicDB.getRegistruDBCollection()) {
                MedicDB oldMedicOfRegistruDBCollectionRegistruDB = registruDBCollectionRegistruDB.getMedic();
                registruDBCollectionRegistruDB.setMedic(medicDB);
                registruDBCollectionRegistruDB = em.merge(registruDBCollectionRegistruDB);
                if (oldMedicOfRegistruDBCollectionRegistruDB != null) {
                    oldMedicOfRegistruDBCollectionRegistruDB.getRegistruDBCollection().remove(registruDBCollectionRegistruDB);
                    oldMedicOfRegistruDBCollectionRegistruDB = em.merge(oldMedicOfRegistruDBCollectionRegistruDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MedicDB medicDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MedicDB persistentMedicDB = em.find(MedicDB.class, medicDB.getId());
            Collection<RegistruDB> registruDBCollectionOld = persistentMedicDB.getRegistruDBCollection();
            Collection<RegistruDB> registruDBCollectionNew = medicDB.getRegistruDBCollection();
            List<String> illegalOrphanMessages = null;
            for (RegistruDB registruDBCollectionOldRegistruDB : registruDBCollectionOld) {
                if (!registruDBCollectionNew.contains(registruDBCollectionOldRegistruDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RegistruDB " + registruDBCollectionOldRegistruDB + " since its medic field is not nullable.");
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
            medicDB.setRegistruDBCollection(registruDBCollectionNew);
            medicDB = em.merge(medicDB);
            for (RegistruDB registruDBCollectionNewRegistruDB : registruDBCollectionNew) {
                if (!registruDBCollectionOld.contains(registruDBCollectionNewRegistruDB)) {
                    MedicDB oldMedicOfRegistruDBCollectionNewRegistruDB = registruDBCollectionNewRegistruDB.getMedic();
                    registruDBCollectionNewRegistruDB.setMedic(medicDB);
                    registruDBCollectionNewRegistruDB = em.merge(registruDBCollectionNewRegistruDB);
                    if (oldMedicOfRegistruDBCollectionNewRegistruDB != null && !oldMedicOfRegistruDBCollectionNewRegistruDB.equals(medicDB)) {
                        oldMedicOfRegistruDBCollectionNewRegistruDB.getRegistruDBCollection().remove(registruDBCollectionNewRegistruDB);
                        oldMedicOfRegistruDBCollectionNewRegistruDB = em.merge(oldMedicOfRegistruDBCollectionNewRegistruDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = medicDB.getId();
                if (findMedicDB(id) == null) {
                    throw new NonexistentEntityException("The medicDB with id " + id + " no longer exists.");
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
            MedicDB medicDB;
            try {
                medicDB = em.getReference(MedicDB.class, id);
                medicDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<RegistruDB> registruDBCollectionOrphanCheck = medicDB.getRegistruDBCollection();
            for (RegistruDB registruDBCollectionOrphanCheckRegistruDB : registruDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MedicDB (" + medicDB + ") cannot be destroyed since the RegistruDB " + registruDBCollectionOrphanCheckRegistruDB + " in its registruDBCollection field has a non-nullable medic field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(medicDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MedicDB> findMedicDBEntities() {
        return findMedicDBEntities(true, -1, -1);
    }

    public List<MedicDB> findMedicDBEntities(int maxResults, int firstResult) {
        return findMedicDBEntities(false, maxResults, firstResult);
    }

    private List<MedicDB> findMedicDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MedicDB.class));
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

    public MedicDB findMedicDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MedicDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MedicDB> rt = cq.from(MedicDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
