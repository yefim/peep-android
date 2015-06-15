package com.chat.peep;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by geoff on 6/9/15.
 */
public class User {
    private ArrayList<Name> names;
    private Person user;

    public User() {
        this.names = new ArrayList<>();
        this.user = new Person();
    }

    public User(String token) {
        this.names = new ArrayList<>();
        this.user = new Person(token);
    }

    public User(Person u, ArrayList<Name> names) {
        this.names = names;
        this.user = u;
    }

    public ArrayList<Name> getNames() {
        return names;
    }

    public String getNumber() {
        return user.getNumber();
    }

    public String getUid() {
        return user.getUid();
    }

    public String getToken() {
        return user.getToken();
    }

    public int getStatus() {
        return user.getStatus();
    }

    public ArrayList<Contact> getContacts() {
        return user.getContacts();
    }

    public Person getPerson() {
        return user;
    }

    public void setNumber(String number) {
        user.setNumber(number);
    }

    public void setUid(String uid) {
        user.setUid(uid);
    }

    public void setToken(String token) {
        user.setToken(token);
    }

    public void setStatus(int status) {
        user.setStatus(status);
    }

    public void setContacts(ArrayList<Contact> contacts) {
        user.setContacts(contacts);
    }

    public void setNames(ArrayList<Name> names) {
        this.names = names;
    }

    public boolean addContact(String name, String number) {
        return user.addContact(new Contact(name, number));
    }

    public static User load(SharedPreferences prefs) {
        Gson gson = new Gson();
        Type contactsType = new TypeToken<ArrayList<Contact>>(){}.getType();
        Type namesType = new TypeToken<ArrayList<Name>>(){}.getType();

        String number = prefs.getString("number", "");
        String uid = prefs.getString("uid", "");
        String token = prefs.getString("token", "");
        int status = prefs.getInt("status", -1);
        ArrayList<Contact> contacts;
        String contactsString = prefs.getString("contacts", "");
        if (!contactsString.isEmpty()) {
            contacts = gson.fromJson(contactsString, contactsType);
        } else {
            contacts = new ArrayList<>();
        }
        ArrayList<Name> names;
        String namesString = prefs.getString("names", "");
        if (!namesString.isEmpty()) {
            names = gson.fromJson(namesString, namesType);
        } else {
            names = new ArrayList<>();
        }

        if (!number.isEmpty() && !uid.isEmpty() && !token.isEmpty() &&
                status > 0) {
            Person u = new Person(number, uid, token, status, contacts);
            return new User(u, names);
        } else {
            return null;
        }
    }

    public static void unload(SharedPreferences.Editor editor) {
        editor.remove("number");
        editor.remove("uid");
        editor.remove("token");
        editor.remove("status");
        editor.remove("contacts");
        editor.remove("names");

        editor.commit();
    }

    public void store(SharedPreferences.Editor editor) {
        Gson gson = new Gson();

        editor.putString("number", this.getNumber());
        editor.putString("uid", this.getUid());
        editor.putString("token", this.getToken());
        editor.putInt("status", this.getStatus());
        editor.putString("contacts", gson.toJson(this.getContacts()));
        editor.putString("names", gson.toJson(this.getNames()));

        editor.commit();
    }

    public void update(User other) {
        // Override properties
        if (other.getNumber() != null && !other.getNumber().isEmpty()) {
            this.setNumber(other.getNumber());
        }
        if (other.getUid() != null && !other.getUid().isEmpty()) {
            this.setUid(other.getUid());
        }
        if (other.getToken() != null && !other.getToken().isEmpty()) {
            this.setToken(other.getToken());
        }
        this.setStatus(other.getStatus());
        // Conditional override
        if (other.getContacts() != null && this.getContacts().size() < other.getContacts().size()) {
            this.setContacts(other.getContacts());
        }
        if (other.getNames() != null && this.getNames().size() < other.getNames().size()) {
            this.setNames(other.getNames());
        }
    }
}

class Person {
    private String number;
    private String uid;
    private ArrayList<Contact> contacts;
    private String token;
    private int status;

    Person() {
        this.contacts = new ArrayList<>();
    }

    Person(String token) {
        this();
        this.token = token;
    }

    Person(String number, String uid, String token, int status, ArrayList<Contact> contacts) {
        this.number = number;
        this.uid = uid;
        this.token = token;
        this.status = status;
        this.contacts = contacts;
    }

    String getNumber() {
        return number;
    }

    String getUid() {
        return uid;
    }

    String getToken() {
        return token;
    }

    int getStatus() {
        return status;
    }

    ArrayList<Contact> getContacts() {
        return contacts;
    }

    void setNumber(String number) {
        this.number = number;
    }

    void setUid(String uid) {
        this.uid = uid;
    }

    void setToken(String token) {
        this.token = token;
    }

    void setStatus(int status) {
        this.status = status;
    }

    void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    boolean addContact(Contact contact) {
        return this.contacts.add(contact);
    }
}

class Name {
    private String name;
    private String uid;

    Name(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    String getName() {
        return name;
    }

    String getUid() {
        return uid;
    }
}

class Contact {
    private String name;
    private String number;
    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
