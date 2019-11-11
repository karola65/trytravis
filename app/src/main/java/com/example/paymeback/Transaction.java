package com.example.paymeback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Anna
 */
@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Transaction implements Serializable {
    /**
     * ID of the transaction.
     */
    private int id;
    /**
     * Keep track on the number of transactions, for the transactionID.
     */
    private ArrayList<Integer> paidOrNot;
    /**
     * Keep track of which user paid the owned money.
     */
    public static int nb=0;
    /**
     * The payer is the one who paid for the members of the group.
     */
    private User payer;
    /**
     * Paybackers are the people who have to pay back the payer.
     */
    private ArrayList<User> paybackers = new ArrayList <User>();
    /**
     * The total amount of the transaction.
     */
    private double amount;
    /**
     * The amount per person, after splitting the bill.
     */
    private double amountPerson;

    private String name;

    /**
     * Constructor with the payer and the pay
     * @param payer
     * @param paybackers
     * @param amount
     */
    public Transaction(String name, User payer, ArrayList<User> paybackers, double amount){
        this.name = name;
        this.id=++nb;
        this.payer = payer;
        this.paybackers = paybackers;
        this.amount = amount;
        int length_of_paybackers = paybackers.size();
        this.paidOrNot = new ArrayList<Integer> (Collections.nCopies(length_of_paybackers, 0));;
        this.splitBill();
    }
    /**
     * Add a user who has to payback.
     * @param member
     */
    public void addMember(User member){
        paybackers.add(member);
    }
    /**
     * Delete a member who has already paid.
     * @param member
     */
    public void deleteMember(User member){
        for(int i=0; i<paybackers.size();i++){
            if(paybackers.get(i).getID() == member.getID()) {
                paybackers.remove(i);
            }
        }
    }
    /**
     * Split equally the amount of the bill between the paybackers and the payer.
     */
    public void splitBill(){
        double temp = (double) this.amount/(paybackers.size()+1);
        this.amountPerson = (double) Math.round(temp * 100.0) / 100.0;
    }

    public User getPayer(){
        return this.payer;
    }

    public double getAmountPerson() {
        return this.amountPerson;
    }

    public ArrayList<User> getPaybackers() {
        return this.paybackers;
    }

    public ArrayList<Integer> getPaidOrNot() { return this.paidOrNot;}

    public void setPaidOrNot (int n) {
        this.paidOrNot.add(n);
    }

    public boolean isPayer(User user) {
        int id = user.getID();
        if(id == this.payer.getID()) {
            return true;
        }
        return false;
    }

    public boolean isPaybacker(User user) {
        int id = user.getID();
        for (User u: paybackers) {
            if (id == u.getID()) {
                return true;
            }
        }
        return false;
    }

    public boolean isInTransaction(User user) {
        if(isPayer(user) || isPaybacker(user)) {
            return true;
        }
        else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    /* We are sure that the input user is within paybackers, so we do not check whether the ınput ıs wıthın the object or not*/
    public int getPaidIndex(User u) {
        int i = 0;
        for(;i<this.paybackers.size();i++) {
            if (u.equals(paybackers.get(i))) {
                break;
            }
        }
        return i;
    }

    public boolean isPaid(User u) {
        int index = getPaidIndex(u);
        if (this.paidOrNot.get(index) == 0) {
            return false;
        }
        else{
            return true;
        }
    }

    public void setPaid(User u) {
        int index = getPaidIndex(u);
        this.paidOrNot.set(index,1);
    }

    public void setPayer(User payer){
        this.payer=payer;
    }
}


