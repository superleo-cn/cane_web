package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import forms.CaneForm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "gz_contact")
public class Contact {

    final static Logger logger = LoggerFactory.getLogger(Contact.class);

    @Id
    private Long id;

    @Required(message = "Device Id cannot be empty")
    private String deviceId;

    private String name;

    private String cellNumber;

    private Boolean status;

    private Date created;


    public static List<Contact> findContactByDeviceId(String deviceId) {
        try {
            ExpressionList<Contact> expList = Ebean.find(Contact.class).where();
            if (StringUtils.isNotEmpty(deviceId)) {
                expList.where().eq("deviceId", deviceId);
                return expList.findList();
            }
            return null;
        } catch (Exception e) {
            logger.error("[findCraneById] -> [exception]", e);
        }
        return null;
    }

    public static boolean update(CaneForm crane) {
        try {
            if (Cane.updateSos(crane)) {
                String arrayStr = crane.getPhone_num();
                if (StringUtils.isNotEmpty(arrayStr)) {
                    String[] contactStr = arrayStr.split(",");
                    if (contactStr != null && contactStr.length > 0) {
                        // delete first
                        List<Contact> list = Ebean.find(Contact.class).where().eq("deviceId", crane.getDevice_id()).findList();
                        Ebean.delete(list);
                        // insert
                        for (String contact : contactStr) {
                            String[] contactArr = contact.split(":");
                            if (contactArr != null && contactArr.length > 0) {
                                Contact newContact = new Contact();
                                newContact.setName(contactArr[0]);
                                newContact.setCellNumber(contactArr[1]);
                                newContact.setCreated(new Date());
                                newContact.setDeviceId(crane.getDevice_id());
                                newContact.setStatus(Boolean.TRUE);
                                Ebean.save(newContact);
                            }
                        }
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("[findCraneById] -> [exception]", e);
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
