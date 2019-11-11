package com.example.paymeback;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Trip group of users.
 *
 * A trip group stores users (User) objects and transactions
 * (Transaction) objects. The user who entered can see the member of groups
 * and the transactions that s/he is involved with.
 *
 * @see User
 * @see Transaction
 *
 * @author Berke
 */
@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class TripGroup implements Serializable {

    /**
     * Name of the trip group.
     */
    private String name;

    /**
     * Members of the trip group.
     */
    private ArrayList<User> members;

    /**
     * List of transactions within the group.
     */
    private ArrayList<Transaction> transactions;

    /**
     * Currency used in transactions.
     */
    private Currency currency;

    /**
     * ID of the trip group.
     */
    private int id;

    /**
     * Initiates the object given its name
     *@param name name of the group
     */
    TripGroup(String name) {
        this.name = name;
        this.members = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    /**
     *Returns the name of the TripGroup object
     *
     * @return A String which is the name of the TripGroup object
     */
    public String getName() {
        return this.name;
    }

    /**
     *Changes the name of the group
     *
     * @param name The new name of the object
     */
    public void change_name(String name) {
        this.name = name;
    }

    /**
     * Sets the id of the TripGroup object
     *
     * @param id the new id of the TripGroup object
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the ID of the object
     *
     * @return an integer which is the ID of the object
     */
    public int getId() {
        return id;
    }

    /**
     * Adds a new User object to the users
     *
     * @param user a new user which will be added to the group
     */
    public void addMember(User user){
        members.add(user);
    }

    /**
     * Deletes a user from the group
     *
     * @param user the user who will be deleted
     */
    public void deleteMember(User user){
        if (this.members.contains(user)) {
            this.members.remove(user);
        }
        else {
            System.out.print("The user is not a member of this group!");
        }
    }

    /**
     * Returns a list of User objects who are members of the group
     *
     * @return a list of User objects who are members of the group
     */
    public ArrayList<User> getMembers() {
        return this.members;
    }

    /**
     * Sets the currency that will be used within the group
     *
     * @param currency the new currency of the group
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * Returns the currency of the group
     *
     * @return the current currency of the group
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Adds a new transaction object to transactions
     *
     * @param transaction a new transaction to be added to the group
     */
    public void addTransaction (Transaction transaction){
        transactions.add(transaction);
    }

    /**
     * Deletes the given transaction
     *
     * @param transaction a transaction object that will be deleted from the group
     */
    public void deleteTransaction(Transaction transaction){
        if (this.transactions.contains(transaction)) {
            this.transactions.remove(transaction);
        }
        else {
            System.out.print("The transaction is not within the transaction list!");
        }
    }

    /**
     * Given two different users, the function returns to the list of the transactions
     * that they are both involed.
     *
     * @param u1 First user
     * @param u2 Second user
     * @return a list of transaction objects which both u1 and u2 are involved
     */
    public ArrayList<Transaction> getTransactionList(User u1, User u2) {
        ArrayList<Transaction> res = new ArrayList<>();
        for(Transaction transaction: transactions) {
            if (transaction.isPayer(u1) && transaction.isPaybacker(u2)) {
                if (transaction.isPaid(u2)){
                    continue;
                }
                else {
                    res.add(transaction);
                }
            }
            else if (transaction.isPayer(u2) && transaction.isPaybacker(u1)) {
                if (transaction.isPaid(u1)){
                    continue;
                }
                else {
                    res.add(transaction);
                }
            }
        }
        return res;
    }

    /**
     * Returns the String version of the group's current currency
     *
     * @return a String of the group's current currency
     */
    public String getStringCurrency(){
        switch (this.currency){
            case Euro: return "Euro";
            case SGD: return "SGD";
            case USD: return "USD";
            case Pound: return "Pound";
            case TRY: return "TRY";
            default: return null;
        }
    }

    /**
     * Returns the transaction objects within the group
     *
     * @return an arraylist of transaction objects within the group
     */
    public ArrayList<Transaction> getTransaction() {
        return this.transactions;
    }

    /**
     * The function checks whether the given user is a member of the group or not
     *
     * @param u a user object
     * @return a boolean represents the information of u's presence in the group
     */
    public boolean isUserInGroup(User u) {
        for(User user: this.members) {
            if (u.equals(user)){
                return true;
            }
        }
        return false;
    }

    /**
     * Edits one transaction object within the transactions. It sets that
     * the given user has paid his/her debt.
     *
     * @param index index of the transaction in the transactions list
     * @param u a user object who paid his debt
     */
    public void editTransaction(int index, User u) {
        Transaction t = this.transactions.get(index);
        t.setPaid(u);
        this.transactions.set(index,t);
    }

    /**
     * This function creates a string version of the TripGroup object.
     *
     * @return a String version of the object
     */
    public String toString(){
        String s="";
        s+=this.name;
        //for the members
        for(int i=0; i<members.size(); i++){
            s+=members.get(i).getFirstName()+"\n"+
                    members.get(i).getLastName()+"\n"+
                    members.get(i).getEmail()+"\n"+
                    members.get(i).getAnswer()+"\n"+
                    members.get(i).getPassword()+"\n";
        }
        //for the transactions
        for(int i=0; i<transactions.size(); i++){
            s+=transactions.get(i).getName()+"\n"+
                    transactions.get(i).getAmount()+"\n"+
                    transactions.get(i).getPayer().getFirstName()+"\n"+
                    transactions.get(i).getPaidOrNot().get(0)+"\n"+
                    transactions.get(i).getPaybackers().get(0).getFirstName()+"\n";
        }
        return s;
    }
}
