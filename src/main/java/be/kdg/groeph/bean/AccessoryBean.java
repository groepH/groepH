package be.kdg.groeph.bean;

import be.kdg.groeph.model.Accessory;
import be.kdg.groeph.model.TripUser;
import be.kdg.groeph.service.AccessoryService;
import be.kdg.groeph.service.TripService;
import be.kdg.groeph.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

@Component
@Named
@RequestScoped
public class AccessoryBean implements Serializable {

    private String description;
    private TripUser user;
    private String userLastname;
    private Accessory currentAccessory;
    private boolean editableAccessory;
    private String newdescription;
    private Map<String, Boolean> editableAccessories;
    private Map<String, Boolean> addableAccessories;
    private ArrayList<TripUser> userlist;

    public AccessoryBean() {
        editableAccessories = new HashMap<String, Boolean>();
        addableAccessories = new HashMap<String, Boolean>();
        userlist = new ArrayList<TripUser>();
    }

    @Qualifier("tripBean")
        @Autowired
        TripBean tripBean;

    @Qualifier("loginBean")
        @Autowired
        LoginBean loginBean;

    @ManagedProperty(value = "#{accessoryService}")
    @Autowired
    AccessoryService accessoryService;

    @ManagedProperty(value = "#{tripService}")
    @Autowired
    TripService tripService;

    public void setDescription(String description) {
        this.description = description;
    }

    public String addAccessory() {
        if (!description.equals("")) {
            Accessory accessory = new Accessory(description, new ArrayList<TripUser>());
            tripBean.getCurrentTrip().addAccessory(accessory);
            setCurrentAccessory(accessory);

            if (accessoryService.addAccessory(currentAccessory)) {
                description = "";
                editableAccessories.clear();
                return Tools.SUCCESS;
            }
        }
        return Tools.FAILURE;

    }
    public List<TripUser> addableConfirmedTripUsers(Accessory accessory){
        userlist.clear();
        for(TripUser user:tripBean.currentTrip.getConfirmedTripUsers()){
           if(!accessory.getTripUsers().contains(user)){
               userlist.add(user);
           }
        }
        return userlist;
    }
    public boolean hasAddableTripUsers(Accessory accessory){
        if(addableConfirmedTripUsers(accessory).size()>0){
            return true;
        }               else{
            return false;
        }
    }

    public void addUser() {
        currentAccessory.addTripUser(user);
        user.addAccessory(currentAccessory);
        addableAccessories.clear();
        accessoryService.updateAccessory(currentAccessory);
    }
    public void removeUser(Accessory accessory) {
        System.out.println("remove user");
            setCurrentAccessory(accessory);
            currentAccessory.removeTripUser(user);
            accessoryService.updateAccessory(currentAccessory);
        }

    public String editAccessory(Accessory accessory) {
        System.out.println("edit acces");
        setCurrentAccessory(accessory);
        newdescription = currentAccessory.getDescription();
        editableAccessories.put(currentAccessory.getDescription(), true);
        return null;
    }

    public String changeAddUser(Accessory accessory) {
           setCurrentAccessory(accessory);
            addableAccessories.put(currentAccessory.getDescription(), true);
            return null;
        }

    public String updateAccessory() {
        editableAccessories.clear();
        addableAccessories.clear();
        currentAccessory.setDescription(newdescription);
        if (accessoryService.updateAccessory(currentAccessory)) {
            description = null;
            return Tools.SUCCESS;
        }
        return Tools.FAILURE;
    }

    public String cancel(Accessory accessory) {
        setCurrentAccessory(accessory);
        newdescription = "";
        editableAccessories.clear();
        addableAccessories.clear();
        return null;
    }

    public String deleteAccessory(Accessory accessory) {
        setCurrentAccessory(accessory);
        currentAccessory.getTripUsers().clear();
        editableAccessories.clear();
        tripBean.getCurrentTrip().deleteAccessory(currentAccessory);
        if (accessoryService.deleteAccessory(currentAccessory)) {

            return Tools.SUCCESS;
        }
        return Tools.FAILURE;
    }
    public void updateChecked(Accessory accessory) {
        if(accessory.isChecked()){
            accessory.setChecked(false);
        }else{
            accessory.setChecked(true);
        }
        accessoryService.updateAccessory(accessory) ;
        }

    public boolean isAddedConfirmedUser(Accessory accessory){
       if( accessory.getTripUsers().contains(loginBean.getUser())){
           return true;
       }else{
           return false;
       }
    }
    public boolean isOrganizer(){
        if(loginBean.getUser().getId() == tripBean.getCurrentTrip().getTripUser().getId() ){
            return true;
        } else{
            return false;
        }
        }

    public Boolean getEditable(Accessory accessory) {
        return editableAccessories.get(accessory.getDescription());
    }
    public Boolean getAdable(Accessory accessory) {

            return addableAccessories.get(accessory.getDescription());
        }

    public boolean getContainsEditable() {
        return editableAccessories.containsValue(true);
    }

    public List<Accessory> getTripaccessories() {
        return accessoryService.getAccessoriesByTrip(tripBean.getCurrentTrip());
    }

    public String getDescription() {
        return description;
    }

    public TripUser getUser() {
        return user;
    }

    public void setUser(TripUser user) {
        this.user = user;
    }

    public Accessory getCurrentAccessory() {
        return currentAccessory;
    }

    public void setCurrentAccessory(Accessory currentAccessory) {
        this.currentAccessory = currentAccessory;
    }

    public String getUserLastname() {
        return userLastname;
    }

    public void setUserLastname(String userLastname) {
        setUserByLastName(userLastname);
        this.userLastname = userLastname;
    }

    private void setUserByLastName(String userLastname) {
        for (TripUser user : tripBean.getCurrentTrip().getConfirmedTripUsers()) {
            if (user.getLastName().equals(userLastname)) {
                this.user = user;
            }
        }
    }

    public boolean isEditableAccessory() {
        return editableAccessory;
    }

    public void setEditableAccessory(boolean editableAccessory) {
        this.editableAccessory = editableAccessory;
    }

    public String getNewdescription() {
        return newdescription;
    }

    public void setNewdescription(String newdescription) {
        this.newdescription = newdescription;
    }

}