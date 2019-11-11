package com.example.paymeback;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.EditText;
import android.util.Log;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * The first intent that the application starts with
 *
 * @author Everyone
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The users who are registered to the app
     */
    ArrayList<User> allusers = new ArrayList<User>();

    /**
     * A memory object which stores the critical data
     */
    Memory mem;

    /**
     *  A tag to trace Log messages
     */
    private String TAG = "readXMLfromDatabase";

    /**
     *  A string to store XML
     */
    private String XML;

    /**
     * A Firebase database to store the critical data
     */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("data").document("XML2");

    /**
     * Connects to server, uploads the critical data, and initializes (Memor) mem object
     */
    Task<DocumentSnapshot> t = docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "Success");
                    Map<String,Object> m = document.getData();
                    XML = (String) m.get("name");
                    try {
                        mem = readXMLFile(XML);
                        allusers = mem.getUsers();
                        Log.d(TAG, "It works");
                    } catch (SAXException e) {
                        Log.d(TAG, e.toString());
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.d(TAG, e.toString());
                        e.printStackTrace();
                    } catch (TransformerException e) {
                        Log.d(TAG, e.toString());
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        Log.d(TAG, e.toString());
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        }
    });

    /**
     * A demo function which creates dummy Users and TripGroups
     */
    public void demo() {
        User u1 = new User("Berke", "Berke", "Onder", "Berke", "Berke","Berke", 1);
        User u2 = new User("Hasan", "Hasan", "Ekinci", "Hasan", "Hasan", "Hasan", 2);
        User u3 = new User("Oyku", "Oyku", "Yilmaz", "Oyku", "Oyku", "Oyku", 3);
        User u4 = new User("Anna", "Anna", "Barraque", "Anna", "Anna", "Anna", 4);
        User u5 = new User("Karolina", "Karolina", "Bargiel", "Karolina", "Karolina", "Karolina", 5);
        User u6 = new User("Wayne", "Wayne", "Toh", "Wayne", "Wayne", "Wayne", 6);
        User u7 = new User("Bruno", "Bruno", "Bodin", "Bruno", "Bruno", "Bruno", 7);
        User u8 = new User("Seda", "Seda", "Onder", "Seda", "Seda", "Seda", 8);
        User u9 = new User("Tuncer", "Tuncer", "Onder", "Tuncer", "Tuncer", "Tuncer", 9);
        TripGroup g1 = new TripGroup("Family");
        g1.setCurrency(Currency.TRY);
        g1.addMember(u1);g1.addMember(u8);g1.addMember(u9);
        ArrayList<User> p1 = new ArrayList<User>();
        p1.add(u1);p1.add(u8);
        Transaction t1 = new Transaction("Dinner at Uzman Kebap",u9, p1, 25);
        g1.addTransaction(t1);
        ArrayList<User> p2 = new ArrayList<User>();
        p2.add(u1);p2.add(u8);
        Transaction t2 = new Transaction("Lunch at McDonalds",u9, p2, 16);
        g1.addTransaction(t2);
        ArrayList<User> p3 = new ArrayList<User>();
        p3.add(u9);p3.add(u8);
        Transaction t3 = new Transaction("Travelling",u1, p3, 13.41);
        g1.addTransaction(t3);


        TripGroup g2 = new TripGroup("SoftEng Project");
        g2.setCurrency(Currency.Euro);
        g2.addMember(u1);g2.addMember(u4);g2.addMember(u5);g2.addMember(u6);
        ArrayList<User> p4 = new ArrayList<User>();
        p4.add(u4);p4.add(u5);p4.add(u6);
        Transaction t4 = new Transaction("Tissues for Crying",u1, p4, 16);
        g2.addTransaction(t4);
        ArrayList<User> p5 = new ArrayList<User>();
        p5.add(u1);p5.add(u5);p5.add(u6);
        Transaction t5 = new Transaction("Mental Health Medicine",u4, p5, 14.21);
        t5.setPaid(u1);
        g2.addTransaction(t5);

        TripGroup g3 = new TripGroup("Kula Lumpur");
        g3.setCurrency(Currency.SGD);
        g3.addMember(u1);g3.addMember(u4);g3.addMember(u5);g3.addMember(u6);
        ArrayList<User> p6 = new ArrayList<User>();
        p6.add(u1);p6.add(u2);p6.add(u3);
        Transaction t6 = new Transaction("Dinner",u1, p4, 22);
        g3.addTransaction(t6);
        ArrayList<User> p7 = new ArrayList<User>();
        p7.add(u1);p7.add(u5);p7.add(u6);
        Transaction t7 = new Transaction("Museum",u4, p5, 14.21);
        g3.addTransaction(t7);

        TripGroup g4 = new TripGroup("Poland");
        g4.setCurrency(Currency.Euro);
        g4.addMember(u1);g4.addMember(u4);g4.addMember(u5);g4.addMember(u6);
        ArrayList<User> p8 = new ArrayList<User>();
        p8.add(u4);p8.add(u5);p8.add(u6);
        Transaction t8 = new Transaction("Pierogies",u1, p4, 16);
        g4.addTransaction(t8);
        ArrayList<User> p9 = new ArrayList<User>();
        p9.add(u1);p9.add(u5);p9.add(u6);
        Transaction t9 = new Transaction("Food",u4, p5, 14.21);
        g4.addTransaction(t9);

        TripGroup g5 = new TripGroup("Poland");
        g5.setCurrency(Currency.Euro);
        g5.addMember(u1);g5.addMember(u4);g5.addMember(u5);g5.addMember(u6);
        g5.addTransaction(t8);
        g5.addTransaction(t9);
        TripGroup g6 = new TripGroup("Italy");
        g6.setCurrency(Currency.Euro);
        g6.addMember(u1);g6.addMember(u4);g6.addMember(u5);g6.addMember(u6);
        g6.addTransaction(t8);
        g6.addTransaction(t9);
        TripGroup g7 = new TripGroup("SG");
        g7.setCurrency(Currency.Euro);
        g7.addMember(u1);g7.addMember(u4);g7.addMember(u5);g7.addMember(u6);
        g7.addTransaction(t8);
        g7.addTransaction(t9);
        TripGroup g8 = new TripGroup("Jekarta");
        g8.setCurrency(Currency.Euro);
        g8.addMember(u1);g8.addMember(u4);g8.addMember(u5);g8.addMember(u6);
        g8.addTransaction(t8);
        g8.addTransaction(t9);
        TripGroup g9 = new TripGroup("Utown");
        g9.setCurrency(Currency.Euro);
        g9.addMember(u1);g9.addMember(u4);g9.addMember(u5);g9.addMember(u6);
        g9.addTransaction(t8);
        g9.addTransaction(t9);
        TripGroup g10 = new TripGroup("Elm");
        g10.setCurrency(Currency.Euro);
        g10.addMember(u1);g10.addMember(u4);g10.addMember(u5);g10.addMember(u6);
        g10.addTransaction(t8);
        g10.addTransaction(t9);
        mem = new Memory();
        mem.addGroup(g1); mem.addGroup(g2); mem.addGroup(g3); mem.addGroup(g4);mem.addGroup(g5);
        mem.addGroup(g6); mem.addGroup(g7);mem.addGroup(g8);mem.addGroup(g9);mem.addGroup(g10);
        mem.addUser(u1);mem.addUser(u2);mem.addUser(u3);mem.addUser(u4);mem.addUser(u5);
        mem.addUser(u6);mem.addUser(u7);mem.addUser(u8);mem.addUser(u9);
        allusers = mem.getUsers();
    }


    private EditText Username, Password;
    private Button Login,ForgetPassword,AddAccount;
    private int numberoftries = 5;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        demo();
        String xml = writeXml(mem);
        SaveXML(xml);
        //*/

        Username = (EditText)findViewById(R.id.username);
        Password = (EditText)findViewById(R.id.password);
        Login = (Button)findViewById(R.id.btnlogin);
        ForgetPassword = (Button)findViewById(R.id.btnforgetpassword);
        AddAccount = (Button)findViewById(R.id.btnaddAccount);
        builder = new AlertDialog.Builder(this);
        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Username.getText().toString(), Password.getText().toString());


            }
        });

        AddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Account.class);
                intent.putExtra("mem",mem);
                startActivity(intent);
            }
        });
        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgetPassword.class);
                intent.putExtra("mem",mem);
                startActivity(intent);
            }
        });
    }

    private void validate(String username, String password)
    {
        int i = 0;
        boolean userexist = true;
        while(userexist)
        {
            if (username.equals(allusers.get(i).userName)) {
                if (password.equals(allusers.get(i).password)) {
                    Intent intent = new Intent(MainActivity.this, AllGroupList.class);
                    mem.setMainuser(allusers.get(i));
                    intent.putExtra("mem",mem);
                    startActivity(intent);
                    userexist = false;
                }
                else
                {
                    numberoftries --;

                    String popupmesssgae = "Sorry wrong password you have " +numberoftries+ " tries left" ;
                    builder.setMessage(popupmesssgae);

                    AlertDialog alert = builder.create();
                    alert.setTitle("Ups!");
                    alert.show();
                    userexist = false;
                }


            }
            else {
                i = i+1;
            }
            if(i == allusers.size())
            {
                String popupmesssgae = "Sorry this username does not exists. Add account! " ;
                builder.setMessage(popupmesssgae);

                AlertDialog alert = builder.create();
                alert.setTitle("Ups!");
                alert.show();
                userexist = false;
            }
        }

        if(numberoftries == 0)
        {

            Login.setEnabled(false);
        }

    }

    public static String writeXml(Memory mem)
    {
        ArrayList<User> allusers = mem.getUsers();
        String xml = "<xml>\n";
        xml += xmlUserList(allusers, true);

        ArrayList<TripGroup> groups = mem.getGroups();
        xml += "<tripGroups>\n";
        for (TripGroup group: groups){
            xml += xmlTripGroup(group);
        }

        xml += "</tripGroups>\n";

        xml += "</xml>\n";
        Log.d("WriteXML",xml);
        return xml;

    }

    public static String xmlTripGroup(TripGroup g)
    {
        String xmlTripGroup = "<tripGroup ";
        String name = xmlpart("name", g.getName());
        String currency = xmlpart("currency", g.getCurrency().toString());
        xmlTripGroup = xmlTripGroup + name + currency + ">\n";
        String members = xmlUserList(g.getMembers(),false);
        xmlTripGroup = xmlTripGroup + members;
        String transactions = "<transactions>\n";

        for (int i =0; i<g.getTransaction().size(); i++) {
            String transaction = "<transaction ";
            String namet=xmlpart("name", g.getTransaction().get(i).getName());
            String amount = xmlpart("amount", Double.toString(g.getTransaction().get(i).getAmount()));
            transaction = transaction + namet + amount + ">\n";
            String payer = "<payer ";
            String userName = xmlpart("userName",g.getTransaction().get(i).getPayer().userName);
            String firstName = xmlpart("firstName",g.getTransaction().get(i).getPayer().firstName);
            String lastName = xmlpart("lastName",g.getTransaction().get(i).getPayer().lastName);;
            String password = xmlpart("password", g.getTransaction().get(i).getPayer().password);
            String email=xmlpart("email", g.getTransaction().get(i).getPayer().email);
            String answer = xmlpart("answer", g.getTransaction().get(i).getPayer().answer);
            String id = xmlpart("id", Integer.toString(g.getTransaction().get(i).getPayer().id));
            payer = payer + userName + firstName + lastName + password + email + answer + id + "></payer>\n" ;

            String paybackers = xmlUserList(g.getTransaction().get(i).getPaybackers(),false);

            String paidOrNot = "<paidOrNot>";
            for (int j = 0; j<g.getTransaction().get(i).getPaidOrNot().size(); j++)
            {
                paidOrNot =  paidOrNot + "<child>" + g.getTransaction().get(i).getPaidOrNot().get(j).toString() +"</child>\n";
            }
            paidOrNot = paidOrNot + "</paidOrNot>";
            transactions += transaction + payer + paybackers + paidOrNot + "</transaction>\n" ;
        }

        transactions = transactions + "</transactions>\n";

        xmlTripGroup = xmlTripGroup + transactions + "</tripGroup>\n";

        return xmlTripGroup;
    }

    public static String xmlUserList(ArrayList<User> a, boolean Main)
    {
        String xmlUserList;
        if (Main){
            xmlUserList = "<MainuserList>";
        }
        else{
            xmlUserList = "<userList>";
        }

        for (int i =0; i<a.size(); i++)
        {
            String userName = xmlpart("userName",a.get(i).userName);
            String firstName = xmlpart("firstName",a.get(i).firstName);
            String lastName = xmlpart("lastName",a.get(i).lastName);;
            String password = xmlpart("password", a.get(i).password);
            String email=xmlpart("email", a.get(i).email);
            String answer = xmlpart("answer", a.get(i).answer);
            String id = xmlpart("id", Integer.toString(a.get(i).id));
            String user = "<user " + userName + firstName + lastName + password + email + answer + id + "></user> \n";
            xmlUserList = xmlUserList + user;
        }

        if (Main){
            xmlUserList += "</MainuserList> \n";
        }
        else{
            xmlUserList += "</userList> \n";
        }
        return xmlUserList;
    }

    public static String xmlpart (String nameofxml, String inside)
    {
        String a = nameofxml +" = \"" + inside +"\" " ;
        return a;

    }

    public static Memory readXMLFile(String xmlSource) throws SAXException, IOException, TransformerException, ParserConfigurationException {

        try {
            Document doc = (Document) loadXMLFromString(xmlSource);
            //optional, but recommended: merges all the text lines in the current node as one single text node. But it can be dangerous because it could be different lines on purpose.
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
            //update mem --> when all is done change memory to mem
            Memory memory = new Memory();
            //for user
            NodeList mainusers = doc.getElementsByTagName("MainuserList");
            Element p = (Element) mainusers.item(0);
            NodeList users = p.getElementsByTagName("user");
            Log.d("tracexmlread", String.format("Number of users %d",users.getLength()));
            for (int i = 0; i < users.getLength(); i++) {
                Node usersNode = users.item(i);
                if (usersNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element userE = (Element) usersNode;
                    User u = readXMLUser(userE);
                    memory.addUser(u);
                }
            }

            //TripGroups
            NodeList allgroups = doc.getElementsByTagName("tripGroups");
            Element p1 = (Element) allgroups.item(0);
            NodeList groups = p1.getElementsByTagName("tripGroup");
            Log.d("tracexmlread", String.format("Number of groups %d",groups.getLength()));
            for (int j = 0; j < groups.getLength(); j++) {
                //for every trip group node
                Node groupNode = groups.item(j);
                Log.d("tracexmlread", groupNode.getNodeName());
                if (groupNode.getNodeType() == Node.ELEMENT_NODE) {
                    //create the element and get the attributes
                    Element groupE = (Element) groupNode;
                    String name = groupE.getAttribute("name");
                    Log.d("tracexmlread", name);
                    String currency = groupE.getAttribute("currency");
                    Log.d("tracexmlread", currency);
                    //create the trip group variables and instanciate it
                    TripGroup tripGroup = new TripGroup(name);
                    if (Currency.contains(currency) != null) {
                        tripGroup.setCurrency(Currency.contains(currency));
                    }
                    else {
                        Log.d("tracexmlread", "no currency WTF");
                    }
                    //get the child nodes of the trip group node
                    NodeList groupEChildNodes = groupE.getChildNodes();
                    for (int i = 0; i < groupEChildNodes.getLength(); i++) {
                        //for the members
                        if ("userList".equals(groupEChildNodes.item(i).getNodeName())) {
                            Node groupMemberNode = (Node) groupEChildNodes.item(i);
                            Element p2 = (Element) groupMemberNode;
                            NodeList users2 = p2.getElementsByTagName("user");
                            Log.d("tracexmlread", String.format("Number of users %d",users2.getLength()));
                            for (int k = 0; k < users2.getLength(); k++) {
                                Node usersNode = users2.item(k);
                                if (usersNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element userE = (Element) usersNode;
                                    User u = readXMLUser(userE);
                                    tripGroup.addMember(u);
                                }
                            }
                        }

                        //for the transactions
                        if ("transactions".equals(groupEChildNodes.item(i).getNodeName())) {
                            Node groupTransactionsNode = (Node) groupEChildNodes.item(i);
                            //get all the transaction child nodes of the node transactions
                            NodeList transactions = groupTransactionsNode.getChildNodes();
                            for (int i1 = 0; i1 < transactions.getLength(); i1++) {
                                Node transactionNode = (Node) transactions.item(i1);
                                if (transactionNode.getNodeType() == Node.ELEMENT_NODE) {
                                    //create the transaction variable and instanciate it with the attributes
                                    Element transactionE = (Element) transactionNode;
                                    String nameTransaction = transactionE.getAttribute("name");
                                    Double amount = Double.parseDouble(transactionE.getAttribute("amount"));
                                    User payer = null;
                                    ArrayList<User> paybackers = new ArrayList<User>();
                                    Log.d("tracexmlread", "nameT " + nameTransaction);
                                    Log.d("tracexmlread", String.format("Amount %f",amount));
                                    Transaction transaction = new Transaction(nameTransaction, payer, paybackers, amount);

                                    //get the child nodes of the transaction node
                                    NodeList transactionEChildNodes = transactionE.getChildNodes();
                                    for (int i2 = 0; i2 < transactionEChildNodes.getLength(); i2++) {
                                        //for the payer
                                        if ("payer".equals(transactionEChildNodes.item(i2).getNodeName())) {
                                            Node payerNode = transactionEChildNodes.item(i2);
                                            if (payerNode.getNodeType() == Node.ELEMENT_NODE) {
                                                Element payerE = (Element) payerNode;
                                                User payerTransaction = readXMLUser(payerE);
                                                transaction.setPayer(payerTransaction);
                                            }

                                        }
                                        //for the paybackers
                                        if ("userList".equals(transactionEChildNodes.item(i2).getNodeName())) {
                                            Node paybackersNode = (Node) transactionEChildNodes.item(i2);
                                            Element p2 = (Element) paybackersNode;
                                            NodeList paybackerNodeList = p2.getElementsByTagName("user");
                                            Log.d("tracexmlread", String.format("Number of paybackers %d",paybackerNodeList.getLength()));
                                            for (int k = 0; k < paybackerNodeList.getLength(); k++) {
                                                Node usersNode = paybackerNodeList.item(k);
                                                if (usersNode.getNodeType() == Node.ELEMENT_NODE) {
                                                    Element paybackerE = (Element) usersNode;
                                                    User paybacker = readXMLUser(paybackerE);
                                                    transaction.addMember(paybacker);
                                                }
                                            }
                                        }
                                        //for the paid or not
                                        if ("paidOrNot".equals(transactionEChildNodes.item(i2).getNodeName())){
                                            Node paidOrNotNode = (Node) transactionEChildNodes.item(i2);
                                            //for the childs
                                            NodeList paidOrNotNodeList = paidOrNotNode.getChildNodes();
                                            for (int i3 = 0; i3 < paidOrNotNodeList.getLength(); i3++) {
                                                Node childNode = paidOrNotNodeList.item(i3);
                                                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                                    Element paidOrNotE = (Element) childNode;
                                                    int n = Integer.parseInt(paidOrNotE.getTextContent());
                                                    transaction.setPaidOrNot(n);
                                                }
                                            }
                                        }

                                    }
                                    transaction.splitBill();
                                    tripGroup.addTransaction(transaction);
                                }
                            }
                        }
                    }
                    memory.addGroup(tripGroup);
                }

            }
            return memory;

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("tracexmlread", e.toString());
        } catch (SAXException e) {
            e.printStackTrace();
            Log.d("tracexmlread", e.toString());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            Log.d("tracexmlread", e.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.d("tracexmlread", e.toString());
        } catch (DOMException e) {
            e.printStackTrace();
            Log.d("tracexmlread", e.toString());
        }
        return null;
    }

    public static User readXMLUser(Element userE) {
        Log.d("tracexmlread", "readXMLUser");
        Log.d("tracexmlread", userE.getTagName());
        String username = userE.getAttribute("userName");
        Log.d("tracexmlread", username);
        String firstname = userE.getAttribute("firstName");
        String lastname = userE.getAttribute("lastName");
        String password = userE.getAttribute("password");
        String email = userE.getAttribute("email");
        String answer = userE.getAttribute("answer");
        String s_id = userE.getAttribute("id");
        int id = Integer.parseInt(s_id);
        return new User(username, firstname, lastname, password, email, answer, id);
    }

    public static Document loadXMLFromString(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = builder.parse(is);
        return doc;
    }

    public static void SaveXML(String XMLnew) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference cities = db.collection("data");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", XMLnew);

        cities.document("XML2").set(data1);

    }
}