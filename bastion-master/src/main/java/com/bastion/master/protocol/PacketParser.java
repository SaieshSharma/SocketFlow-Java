package com.bastion.master.protocol;

public class PacketParser {

    public Packet parse(String rawPacket){

        //regex escaping
        String[] incoming = rawPacket.split("\\|");

        String   type = null;
        String from = null;
        String data = null;

        for(String str : incoming){
            System.out.println(str);

            if(str.startsWith("TYPE:")){
                //by default assumes to go till end
                type = (str.substring("TYPE:".length()));
            }

            if(str.startsWith("FROM:")){
                //by default assumes to go till end
                from = (str.substring("FROM:".length()));
            }
            if(str.startsWith("DATA:")){
                //by default assumes to go till end
                data = (str.substring("DATA:".length()));
            }
        }

        PacketType validPacket;
        try{
           validPacket = PacketType.valueOf(type);
        }
        catch(InvalidPacketException e){
            throw new InvalidPacketException("Invalid packet type: " + type);
        };
        

       return new Packet(validPacket, from, data);

    }
    public static void main(String[] args) {
        
    }
    
}
