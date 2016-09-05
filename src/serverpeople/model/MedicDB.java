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
@Table(name = "medic")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MedicDB.findAll", query = "SELECT m FROM MedicDB m"),
    @NamedQuery(name = "MedicDB.findById", query = "SELECT m FROM MedicDB m WHERE m.id = :id"),
    @NamedQuery(name = "MedicDB.findByNume", query = "SELECT m FROM MedicDB m WHERE m.nume = :nume"),
    @NamedQuery(name = "MedicDB.findByMatricula", query = "SELECT m FROM MedicDB m WHERE m.matricula = :matricula")})
public class MedicDB implements Serializable {

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
    @Column(name = "matricula")
    private String matricula;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medic")
    private Collection<RegistruDB> registruDBCollection;

    public MedicDB() {
    }

    public MedicDB(Integer id) {
        this.id = id;
    }

    public MedicDB(Integer id, String nume, String matricula) {
        this.id = id;
        this.nume = nume;
        this.matricula = matricula;
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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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
        if (!(object instanceof MedicDB)) {
            return false;
        }
        MedicDB other = (MedicDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "serverpeople.model.MedicDB[ id=" + id + " ]";
    }
    
}
