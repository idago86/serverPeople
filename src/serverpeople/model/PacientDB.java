/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpeople.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Israel Dago
 */
@Entity
@Table(name = "pacient")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PacientDB.findAll", query = "SELECT p FROM PacientDB p"),
    @NamedQuery(name = "PacientDB.findById", query = "SELECT p FROM PacientDB p WHERE p.id = :id"),
    @NamedQuery(name = "PacientDB.findByNume", query = "SELECT p FROM PacientDB p WHERE p.nume = :nume"),
    @NamedQuery(name = "PacientDB.findByCnp", query = "SELECT p FROM PacientDB p WHERE p.cnp = :cnp")})
public class PacientDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nume")
    private String nume;
    @Basic(optional = false)
    @Column(name = "cnp")
    private String cnp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pacient")
    private Collection<RegistruDB> registruDBCollection;

    public PacientDB() {
    }

    public PacientDB(Integer id) {
        this.id = id;
    }

    public PacientDB(Integer id, String nume, String cnp) {
        this.id = id;
        this.nume = nume;
        this.cnp = cnp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    @XmlTransient
    public Collection<RegistruDB> getRegistruDBCollection() {
        return registruDBCollection;
    }

    public void setRegistruDBCollection(Collection<RegistruDB> registruDBCollection) {
        this.registruDBCollection = registruDBCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PacientDB)) {
            return false;
        }
        PacientDB other = (PacientDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "serverpeople.model.PacientDB[ id=" + id + " ]";
    }
    
}
