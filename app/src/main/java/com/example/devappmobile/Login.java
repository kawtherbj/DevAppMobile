package com.example.devappmobile;

public class Login {

    private String grant_type ;
    private String  scope;
    private String client_id;
    private String client_secret;
    private String username;
    private String password;
    private String refresh_token;


    public Login(String g, String s, String c , String cs, String u, String p){

        this.grant_type=g ;
        this.scope=s;
        this.client_id=c;
        this.client_secret=cs;
        this.username=u;
        this.password=p;

    }

    public Login(String g){

        this.refresh_token=g;
        this.grant_type="refresh_token" ;
        this.scope="*";
        this.client_id="SJGZDWXOPLJZLBDQGACCAGAVWSHORHJK";
        this.client_secret="6734914665b5258c7a2eb01077382585";

    }
}
