/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpeople.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Israel Dago
 */
@Entity
@Table(name = "registru")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegistruDB.findAll", query = "SELECT r FROM RegistruDB r"),
    @NamedQuery(name = "RegistruDB.findById", query = "SELECT r FROM RegistruDB r WHERE r.id = :id")})
public class RegistruDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "medic", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MedicDB medic;
    @JoinColumn(name = "pacient", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PacientDB pacient;

    public RegistruDB() {
    }

    public RegistruDB(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MedicDB getMedic() {
        return medic;
    }

    public void setMedic(MedicDB medic) {
        this.medic = medic;
    }

    public PacientDB getPacient() {
        return pacient;
    }

    public void setPacient(PacientDB pacient) {
        this.pacient = pacient;
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
        if (!(object instanceof RegistruDB)) {
            return false;
        }
        RegistruDB other = (RegistruDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "serverpeople.model.RegistruDB[ id=" + id + " ]";
    }
    
}
