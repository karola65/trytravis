package com.example.paymeback;

import java.util.ArrayList;

/**
 *
 * @author Wayne
 */
public class Balance {
    
    private User pov_user;
    private User other_user;
    private ArrayList<Transaction> transaction_list;
    private Currency currency;
    
    Balance (User pov_user, User other_user, ArrayList<Transaction> transaction_list, Currency currency){
        this.pov_user = pov_user;
        this.other_user = other_user;
        this.transaction_list = transaction_list;
        this.currency = currency;
    }
    
    public double displayBalance (){
        double balance = 0;
        for (Transaction t :transaction_list){
            if (t.isPayer(this.pov_user)) {
                balance += t.getAmountPerson();
            }
            else {
                balance -= t.getAmountPerson();
            }
        }
        return balance;
    }

    public User getPOVuser () {
        return this.pov_user;
    }
    public User getOtheruser() {
        return this.other_user;
    }
    public ArrayList<Transaction> getTransactionlist() {
        return this.transaction_list;
    }
    public Currency getCurrency() {
        return this.currency;
    }
    public void setPOVuser (User pov) {
        this.pov_user = pov;
    }
    public void setOtheruser(User other) {
        this.other_user = other;
    }
    public void setTransactionlist(ArrayList<Transaction> transactionlist) {this.transaction_list = transactionlist; }
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }   
       
}
