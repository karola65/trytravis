package com.example.paymeback;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A memory storage object which keeps all the information and makes sure that it passes
 * from one intent to another.
 *
 * The memory object is used in all intents and its the primary thing that is used for
 * storing the information. Memory stores the information of who is logged in. It stores
 * the information of all the users and the trip groups. It also has an additional int
 * attribute which stores the index of the group when the user wants to get more information
 * about a group. The memory object is stored in the database as an XML format.
 *
 * @author Berke
 */
public class Memory implements Serializable {

    /**
     * The user who logged in to the app
     */
    private User mainuser;

    /**
     * The users who are registered to the app
     */
    private ArrayList<User> users;

    /**
     * All tripgroups which are created by users
     */
    private ArrayList<TripGroup> groups;

    /**
     * One specific group index used for Groupview
     *
     * @see GroupView
     */
    private int onegroup;

    /**
     * A constructor for memory object
     */
    Memory() {
        this.users = new ArrayList<>();
        this.groups = new ArrayList<>();
    }

    /**
     * Returns the groups which are created by users
     *
     * @return an arraylist of groups which are created by users
     */
    public ArrayList<TripGroup> getGroups() {
        return groups;
    }

    /**
     * Returns the users who are registered to the app
     *
     * @return an arraylist of users who are registered to the app
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Returns the user object who is logged in to the app
     *
     * @return a user object who is logged in to the app
     */
    public User getMainuser() {
        return mainuser;
    }

    /**
     * Returns the index information of the desired TripGroup object
     *
     * @return an integer which is the index of the desired TripGroup object
     */
    public int getOnegroup() {
        return onegroup;
    }

    /**
     * Adds a new TripGroup object to groups
     *
     * @param group a new group to be added
     */
    public void addGroup(TripGroup group) {
        this.groups.add(group);
    }

    /**
     * Sets mainuser who is logged in to the app
     *
     * @param mainuser the user who is logged in to the app
     */
    public void setMainuser(User mainuser) {
        this.mainuser = mainuser;
    }

    /**
     * Adds a new user to the registered users list
     *
     * @param user a new user to be added
     */
    public void addUser(User user) {
        this.users.add(user);
    }

    /**
     * Sets the index value of the desired group
     *
     * @param onegroup the index value of the desired group
     */
    public void setOnegroup(int onegroup) {
        this.onegroup = onegroup;
    }

    /**
     * Deletes the given group from groups
     *
     * @param index the index of group to be deleted
     */
    public void deleteGroup(int index) {
        this.groups.remove(index);
    }

    /**
     * Updates an existing TripGroup object.
     *
     * @param index posotion in the groups arraylist
     * @param t Updated TripGroup object
     */
    public void setGroup(int index, TripGroup t) {
        this.groups.set(index,t);
    }

    /**
     * Creates a string version of the memory object. It does not print
     * the main user and the index.
     *
     * @return A String version of the memory object
     */
    public String toString(){
        String s="";
        for (int i=0; i<users.size(); i++){
            s+="user n_"+i+" "+users.get(i).toString()+"\n";
        }
        for (int i=0; i<groups.size(); i++){
            s+="group n_"+i+" "+groups.get(i).toString()+"\n";
        }
        return s;
    }
}
