package com.bastion.master.protocol;

public class Packet {

    private PacketType type;
    private String from;
    private String data;

    public Packet(PacketType type, String from, String data) {
        this.type = type;
        this.from = from;
        this.data = data;
    }

    //getter
    public PacketType getType(){
        return type;
    };

    public String getFrom(){
        return from;
    };

    public String getData(){
        return data;
    }

    //setters
    public void setType(PacketType type){
        // object's field gets incoming parameter
        this.type = type;
    };
    public void setFrom(String from){
        this.from = from;
    };
    public void setData(String data){
        this.data = data;
    };
    
}
